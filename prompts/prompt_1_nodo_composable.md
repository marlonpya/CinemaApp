# Prompt 1 — Crear Composable en `pe.interbank.ads.mobile.nodo.ui`

> **Uso:** Envía este prompt junto con la imagen del diseño al asistente de IA.

---

## PROMPT

Eres un experto en Android con Jetpack Compose y sistemas de diseño.

Analiza la imagen adjunta y genera un **Composable reutilizable** para el módulo de diseño `pe.interbank.ads.mobile.nodo.ui`, que actúa como design system del banco.

### Reglas del proyecto `pe.interbank.ads.mobile.nodo.ui`

1. **Package base:** `pe.interbank.ads.mobile.nodo.ui.components.<nombre_del_componente>`
2. **Nomenclatura del Composable:** usa PascalCase con el prefijo `Nodo`, por ejemplo: `NodoCardBenefit`, `NodoBannerPromo`, `NodoTransactionItem`.
3. **Parámetros del Composable:**
   - Todos los textos, imágenes y acciones deben ser **parámetros externos** (no hardcodeados).
   - Incluye un parámetro `modifier: Modifier = Modifier` siempre.
   - Usa `NodoTheme` / tokens de diseño propios del design system (colores via `NodoTheme.colorScheme`, tipografía via `NodoTheme.typography`, espaciado via `NodoDimensions`).
4. **Íconos:** usa **Google Fonts Material Symbols** mediante `fontFamily` (no drawables locales):
   ```xml
   <!-- res/font/material_symbols_outlined.xml -->
   <font-family xmlns:app="http://schemas.android.com/apk/res-auto"
       app:fontProviderAuthority="com.google.android.gms.fonts"
       app:fontProviderPackage="com.google.android.gms"
       app:fontProviderQuery="Material Symbols Outlined"
       app:fontProviderCerts="@array/com_google_android_gms_fonts_certs" />
   ```
   Úsalos en Compose con `fontFamily = FontFamily(Font(R.font.material_symbols_outlined))`.
5. **Preview:** incluye al menos un `@Preview` con datos de ejemplo representativos del diseño.
6. **Accesibilidad:** agrega `contentDescription` en imágenes y elementos interactivos.
7. **Estados:** si el diseño muestra variantes (loading, error, vacío, deshabilitado), créalos como parámetros o como un `sealed class` de estado dentro del mismo archivo.
8. **No importes** nada de `pe.com.interbank.mobilebanking`. Este módulo es independiente.

### Estructura esperada del archivo

```
pe/interbank/ads/mobile/nodo/ui/components/<NombreComponente>/
└── Nodo<NombreComponente>.kt
```

### Formato de entrega

Genera **únicamente el código Kotlin** del archivo `Nodo<NombreComponente>.kt` con:
- Declaración del package
- Imports necesarios
- Data class / sealed class de parámetros si aplica
- El Composable principal
- Composables internos privados si la complejidad lo justifica
- El o los `@Preview`

Infiere el nombre del componente a partir del diseño de la imagen.
