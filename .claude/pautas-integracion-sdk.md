# Pautas: Integrar `pe.guinea:facial-biometric-sdk:2.0.0-dev.5` en otro proyecto Android

Guía de consideraciones para agregar el SDK de biometría facial de Guinea a **otro proyecto Android**, usando este ejemplo (`guinea-biometric-android-example`) y `.claude/documentation.md` (spec v2) como referencia.

> **Aclaración v2 importante:** en v2 se eliminaron del API público las llaves de FaceTec, el token OAuth y `clientCertSha256`. El SDK las negocia solo contra el gateway al inicializar. Este ejemplo todavía conserva campos vestigiales (`faceTecProductionKey`, `faceTecDeviceKey`, `faceTecEncryptionKey`, `clientCertSha256`, `authorizationBearer` en `config.json` y `app/build.gradle.kts:53-58`) que **`MainActivity.kt` NO usa** — solo pasa `baseUrl`. **En el proyecto nuevo NO repliques esos campos.**

---

## 1. Distribución y wiring del repositorio (lo más crítico)

El SDK **no está en Maven Central ni en ningún repo público**. Se entrega como `facial-biometric-sdk-android-<version>.zip`, un repositorio Maven local con esta estructura:

```
m2/
├── pe/guinea/facial-biometric-sdk/2.0.0-dev.5/   (.aar, .pom, .module, -sources.jar)
└── com/facetec/facetec-sdk/9.7.91/               (.aar, .pom)
```

**Pasos en el proyecto nuevo:**

1. Copiar/descomprimir el contenido bajo `vendor/guinea-sdk/m2/` en la raíz del proyecto (mismo layout que aquí).
2. Registrar el repo Maven local en `settings.gradle.kts`, dentro de `dependencyResolutionManagement.repositories`, **junto con** `google()` y `mavenCentral()` (necesarios para las transitivas). Patrón de `settings.gradle.kts:8-16`:
   ```kotlin
   dependencyResolutionManagement {
       repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
       repositories {
           maven { url = uri("vendor/guinea-sdk/m2") }
           google()
           mavenCentral()
       }
   }
   ```
   - **Cuidado con `FAIL_ON_PROJECT_REPOS`:** si el proyecto nuevo declara repos en el `build.gradle` de módulo (estilo viejo), fallará. Migrar repos a `settings.gradle.kts` o cambiar el modo. Registrar el `maven {}` aquí, no en el módulo.
3. Agregar la dependencia en el `build.gradle.kts` del módulo app (ver `app/build.gradle.kts:87`):
   ```kotlin
   implementation("pe.guinea:facial-biometric-sdk:2.0.0-dev.5")
   ```

**Transitivas:** se resuelven automáticamente vía Gradle Module Metadata (FaceTec, OkHttp 4.12, kotlinx-serialization-json 1.6.2, AndroidX core/appcompat/lifecycle/activity-compose, Compose BOM 2024.01.00, Google Tink 1.10.0). **Cero configuración manual.** Por eso es obligatorio mantener `google()` + `mavenCentral()`.

> **El binario es específico del ambiente.** El SDK trae una keyset criptográfica embebida por ambiente (dev/cert/prod). El `2.0.0-dev.5` que está aquí es build **dev**; debe coincidir con el `baseUrl`/gateway del ambiente que uses. Para producción se necesita el zip del build prod correspondiente.

## 2. Requisitos de compilación del proyecto nuevo

Alinear `app/build.gradle.kts` y `build.gradle.kts` raíz (ver `documentation.md` §Requisitos):

- `minSdk = 24` (Android 7.0). Si el proyecto nuevo tiene `minSdk` menor, subirlo.
- `compileSdk = 34` (mínimo recomendado).
- Java/Kotlin target **17** (`sourceCompatibility`/`targetCompatibility` y `jvmTarget = "17"`, ver `app/build.gradle.kts:67-74`).
- Kotlin `1.9.22` (el SDK trae `kotlin-stdlib:1.9.22`). Usar la misma versión del plugin Kotlin para evitar choques de stdlib/metadata.
- AGP: este ejemplo usa `8.2.0` (`build.gradle.kts:2`). Funciona con AGP ≥ 8.x.
- `gradle.properties`: `android.useAndroidX=true` es obligatorio.

**Sobre Compose:** el SDK usa Compose internamente y lo trae como transitiva, pero el host **no necesita habilitar Compose** para el modo Guiado (se lanza vía Activity Result, el SDK dibuja su propia UI). Solo habilitar `buildFeatures { compose = true }` + `composeOptions` si tu propia UI usa Compose.

## 3. AndroidManifest del proyecto nuevo

Declarar los permisos (ver `app/src/main/AndroidManifest.xml`):

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
```

- El permiso de cámara en runtime lo solicita el propio SDK en modo Guiado (diálogo nativo; ante rechazo permanente deriva a Configuración). En modo Motor, manejar el permiso antes de `performLiveness`.
- `android:usesCleartextTraffic="true"` **solo** si el `baseUrl` es `http://` (dev). Con `https://` omitirlo.

## 4. Inicialización (una sola vez por proceso, antes de cualquier llamada)

Ver `MainActivity.kt:45-57` y `documentation.md` §Inicialización. En v2 solo se necesita `baseUrl`:

```kotlin
GuineaFacialBiometric.initialize(
    BiometricConfig(
        baseUrl = "https://<gateway-del-ambiente>",
        security = SecurityConfig.strict,           // strict para https/producción
        appearance = BiometricAppearance.interbank, // opcional: .guinea (default) | .interbank | custom
    ),
)
```

- `SecurityConfig.strict` para HTTPS (recomendado prod); `.permissive` solo para HTTP de dev.
- **NO** pasar llaves de FaceTec / OAuth / cert SHA256 (eliminados en v2).
- El `baseUrl` define el ambiente; debe ser coherente con el build del SDK (keyset embebida).
- **Reloj del dispositivo:** el handshake interno rechaza timestamps fuera de ±5 min → falla con 401 (`SecurityFailure`).

## 5. Modo de uso (elegir según necesidad de UX)

**Guiado (turnkey, menos código)** — el SDK posee toda la UI; el host pasa el DNI y recibe el `VerificationResult`. Patrón en `MainActivity.kt:63-72`:
```kotlin
val verify = registerForActivityResult(GuidedVerificationContract()) { result -> /* ... */ }
verify.launch("12345678")   // el host provee el documento; el SDK no lo recolecta
```

**Motor (API directa, UX propia)** — orquestas los 3 métodos suspend. Patrón en `MotorVerificationActivity.kt:291-310`:
```kotlin
val session  = GuineaFacialBiometric.createSession(dni).getOrThrow()
val liveness = GuineaFacialBiometric.performLiveness(activity, dni, session).getOrThrow()
val result   = GuineaFacialBiometric.submitValidation("dni", dni, session, liveness).getOrThrow()
```

## 6. Resultados y manejo de errores

- `VerificationResult`: `Verified(id, match, expiresAt)` / `Pending(id)` / `Failed(id?, reason)` / `Cancelled`. El `id` se usa luego como `biometric_validation_id`.
- `BiometricError`: mapear al menos `Cancelled`, `CaptureTimeout`, `TooManyAttempts`, `CameraPermissionDenied`, `LivenessFailure`, `ValidationFailure` (422 no-match RENIEC), `NetworkFailure`, `ServerFailure`, `SecurityFailure`, `UnsupportedEnvironment`. Tabla completa en `documentation.md` §Manejo de errores.
- **El SDK NO reintenta automáticamente**; la política de retry la implementa la app.

## 7. Dispositivo y limitaciones al probar

- **Dispositivo físico obligatorio.** FaceTec rechaza emuladores → `BiometricError.UnsupportedEnvironment` (atrapable, no bloqueante). Si se usa emulador para humo, debe ser **arm64-v8a** (el AAR de FaceTec no trae `x86_64`).
- Forma del óvalo de captura no customizable; idioma de FaceTec en Android sigue el locale del dispositivo (`stringOverrides` sí aplica).

## 8. R8 / ProGuard

Este ejemplo desactiva minify (`app/build.gradle.kts:62-64`). Si el proyecto nuevo habilita `isMinifyEnabled = true`, verificar el comportamiento con SDK + FaceTec + Tink/serialization y, de ser necesario, agregar reglas `keep` (FaceTec y kotlinx-serialization suelen requerirlas). Validar en un build release ofuscado antes de liberar.

---

## Resumen de archivos a tocar en el proyecto nuevo

| Archivo | Cambio |
|--------|--------|
| `vendor/guinea-sdk/m2/` | Copiar el repo Maven local del zip del SDK (SDK + FaceTec) |
| `settings.gradle.kts` | Registrar `maven { url = uri("vendor/guinea-sdk/m2") }` + `google()` + `mavenCentral()` |
| `app/build.gradle.kts` | `implementation("pe.guinea:facial-biometric-sdk:2.0.0-dev.5")`; alinear minSdk 24 / compileSdk 34 / Java 17 / Kotlin 1.9.22 |
| `app/src/main/AndroidManifest.xml` | Permisos `INTERNET` + `CAMERA` (y `usesCleartextTraffic` solo si http) |
| `gradle.properties` | `android.useAndroidX=true` |
| Código (Activity/Fragment) | `GuineaFacialBiometric.initialize(...)` + lanzar modo Guiado o Motor |

## Verificación

1. **Sync de Gradle:** resuelve SDK y FaceTec desde `vendor/guinea-sdk/m2` y baja transitivas de google/mavenCentral sin errores de repositorio.
2. **Build:** `./gradlew :app:assembleDebug` compila con Java 17 / Kotlin 1.9.22.
3. **Init:** `GuineaFacialBiometric.initialize(...)` se ejecuta una vez antes de cualquier llamada (logcat sin `SecurityFailure` por "SDK sin inicializar").
4. **Flujo en dispositivo físico:** `./gradlew :app:installDebug`, lanzar la verificación con un DNI y comprobar que el callback recibe `VerificationResult`. (En emulador esperar `UnsupportedEnvironment`.)
5. **Reloj:** la hora del dispositivo dentro de ±5 min para evitar 401 en el handshake.
