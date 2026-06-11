# Prompt 2 — Crear Feature completo en la app consumidora (`mobilebanking`)

> **Uso:** Pega este prompt en el asistente de IA del proyecto `pe.com.interbank.mobilebanking`
> junto con la imagen del diseño, **después** de haber generado el Composable con el Prompt 1.

---

## PROMPT

Eres un experto en Android con Jetpack Compose y arquitectura MVI / Clean Architecture.

Antes de generar cualquier código, **explora y analiza este proyecto** para entender:

1. **Estructura de un feature existente** — localiza al menos un feature completo en el proyecto y observa cómo están organizadas sus carpetas y archivos (nombres, niveles de package, subcarpetas como `interactor/`, `domain/`, etc.).
2. **Arquitectura y patrón de estado** — identifica el patrón exacto usado:
   - Cómo se modela el estado de UI (`data class`, `sealed class`, campos individuales en el ViewModel, etc.)
   - Cómo se modelan los intents / acciones del usuario
   - Cómo se modelan los eventos de un solo disparo (navegación, snackbars, diálogos)
3. **ViewModel** — observa:
   - Qué expone (`StateFlow`, `SharedFlow`, `LiveData`, `Channel`, etc.)
   - Cómo recibe las acciones del usuario (función `handleIntent`, funciones individuales, etc.)
   - Cómo mutua el estado (`.update { }`, `.value =`, etc.)
   - Cómo se inyectan las dependencias (Hilt, Koin, manual, factory, etc.)
4. **Screen Composable** — observa cómo el Screen:
   - Recoge el estado del ViewModel (`collectAsStateWithLifecycle`, `observeAsState`, etc.)
   - Consume los eventos de un solo disparo (`LaunchedEffect`, `DisposableEffect`, etc.)
   - Delega la renderización al Composable del design system
5. **Inyección de dependencias** — identifica el framework o patrón usado (Hilt `@HiltViewModel`, Koin `viewModel { }`, `ViewModelFactory` manual, etc.) y réplicalo exactamente.
6. **Nomenclatura** — prefijos, sufijos o convenciones de nombres de clases, funciones y archivos usados en los features existentes.

---

Ya existe un Composable en el design system `pe.interbank.ads.mobile.nodo.ui` que representa la UI de la imagen adjunta. Ahora genera **todos los archivos del feature** para esta pantalla en este proyecto, siguiendo **exactamente** los mismos patrones, convenciones y estilo de código encontrados.

### Estructura de archivos a generar

Respeta la misma organización de carpetas que los features existentes. Como referencia orientativa (adáptala si el proyecto usa otra):

```
feature/<nombreFeature>/
├── <NombreFeature>ViewModel.kt
├── <NombreFeature>Screen.kt
└── interactor/
    ├── <NombreFeature>UiState.kt
    ├── <NombreFeature>UiEvent.kt
    └── <NombreFeature>UiIntent.kt
```

Infiere `<NombreFeature>` a partir del diseño de la imagen (PascalCase, sin prefijos).

### Contrato de cada archivo

#### `UiState`
- Modela todos los datos que la pantalla necesita renderizar.
- Sigue el mismo patrón de modelado de estado que el proyecto (data class con defaults, sealed class, etc.).
- Incluye siempre campos para indicar carga y error.

#### `UiIntent`
- Representa todas las **acciones posibles del usuario** en esta pantalla.
- Sigue el mismo patrón de intents del proyecto (`sealed class`, `sealed interface`, etc.).

#### `UiEvent`
- Representa **efectos de un solo disparo** (navegación, mensajes, diálogos).
- Sigue el mismo patrón de eventos del proyecto.

#### `ViewModel`
- Sigue el mismo patrón de ViewModel del proyecto (flows, observables, etc.).
- Inyecta dependencias usando el mismo framework/patrón encontrado.
- No inyectes repositorios directamente; pasa siempre por use cases.
- Infiere qué use cases son necesarios a partir del diseño.

#### `Screen`
- Orquesta el estado, los eventos y el Composable del design system.
- Sigue el mismo patrón de Screen encontrado en el proyecto.
- Importa el Composable desde `pe.interbank.ads.mobile.nodo.ui`.
- El Screen no contiene lógica de negocio ni lógica visual compleja.
- Maneja los estados de carga y error de la misma forma que los otros screens del proyecto.

### Restricciones

- No generes tests en esta entrega.
- No inventes patrones nuevos; replica fielmente los ya existentes en el proyecto.
- No importes la app consumidora desde el design system ni viceversa en direcciones incorrectas.

### Formato de entrega

Al inicio de tu respuesta:
- Lista los features existentes que analizaste como referencia.
- Resume brevemente el patrón arquitectónico encontrado (ViewModel pattern, DI framework, tipo de flows).

Luego genera los archivos en bloques de código separados, cada uno con su nombre como título, en el orden:
1. `<NombreFeature>UiState.kt`
2. `<NombreFeature>UiIntent.kt`
3. `<NombreFeature>UiEvent.kt`
4. `<NombreFeature>ViewModel.kt`
5. `<NombreFeature>Screen.kt`
