# Prompt 2 — Crear Feature completo en `pe.com.interbank.mobilebanking`

> **Uso:** Envía este prompt junto con la **misma imagen del diseño** al asistente de IA, **después** de haber generado el Composable con el Prompt 1.

---

## PROMPT

Eres un experto en Android con Jetpack Compose y arquitectura MVI.

Analiza la imagen adjunta. Ya existe un Composable en el design system `pe.interbank.ads.mobile.nodo.ui` que representa la UI de esta pantalla. Ahora necesitas generar **todos los archivos restantes** del feature en `pe.com.interbank.mobilebanking`, siguiendo la arquitectura MVI del proyecto.

### Reglas del proyecto `pe.com.interbank.mobilebanking`

**Package base del feature:** `pe.com.interbank.mobilebanking.feature.<nombre_del_feature>`

### Estructura de archivos a generar

```
pe/com/interbank/mobilebanking/feature/<nombreFeature>/
├── <NombreFeature>ViewModel.kt
├── <NombreFeature>Screen.kt
└── interactor/
    ├── <NombreFeature>UiState.kt
    ├── <NombreFeature>UiEvent.kt
    └── <NombreFeature>UiIntent.kt
```

Infiere `<NombreFeature>` desde la imagen (usa PascalCase, sin prefijos, ej: `CardBenefit`, `TransactionDetail`, `PromoOffer`).

---

### Contrato de cada archivo

#### 1. `<NombreFeature>UiState.kt`
- `data class` con todos los campos que la pantalla necesita renderizar (textos, listas, flags de visibilidad, estados de carga).
- Incluye `val isLoading: Boolean = false` y `val errorMessage: String? = null` siempre.
- Valores por defecto para todos los campos.

```kotlin
// Ejemplo de referencia
data class ConfigurationUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // ... campos propios del feature
)
```

#### 2. `<NombreFeature>UiIntent.kt`
- `sealed class` con todas las **acciones del usuario** que la pantalla puede disparar (clics, swipes, inputs).
- Usa `object` para intents sin payload y `data class` para los que llevan datos.

```kotlin
// Ejemplo de referencia
sealed class ConfigurationUiIntent {
    object LoadData : ConfigurationUiIntent()
    data class OnItemSelected(val id: String) : ConfigurationUiIntent()
    object OnBackPressed : ConfigurationUiIntent()
}
```

#### 3. `<NombreFeature>UiEvent.kt`
- `sealed class` con **efectos de un solo disparo** que la pantalla debe consumir (navegación, snackbars, diálogos).
- Usa `object` o `data class` según necesite payload.

```kotlin
// Ejemplo de referencia
sealed class ConfigurationUiEvent {
    object NavigateBack : ConfigurationUiEvent()
    data class ShowError(val message: String) : ConfigurationUiEvent()
    data class NavigateToDetail(val id: String) : ConfigurationUiEvent()
}
```

#### 4. `<NombreFeature>ViewModel.kt`
- Extiende `ViewModel()`.
- Expone:
  - `val uiState: StateFlow<NombreFeatureUiState>` (usando `MutableStateFlow` privado).
  - `val uiEvent: SharedFlow<NombreFeatureUiEvent>` (usando `MutableSharedFlow` con `replay = 0`).
- Función pública `fun handleIntent(intent: NombreFeatureUiIntent)` que despacha cada intent con un `when`.
- Usa `viewModelScope.launch` para operaciones asíncronas.
- Inyecta los use cases necesarios por constructor (infiere cuáles hacen falta a partir del diseño).
- **No inyectes** repositorios directamente; siempre pasa por use cases.

```kotlin
// Ejemplo de referencia
class ConfigurationViewModel(
    private val getConfigurationUseCase: GetConfigurationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigurationUiState())
    val uiState: StateFlow<ConfigurationUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ConfigurationUiEvent>()
    val uiEvent: SharedFlow<ConfigurationUiEvent> = _uiEvent.asSharedFlow()

    fun handleIntent(intent: ConfigurationUiIntent) {
        when (intent) {
            is ConfigurationUiIntent.LoadData -> loadData()
            // ...
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // ...
        }
    }
}
```

#### 5. `<NombreFeature>Screen.kt`
- Composable `@Composable fun <NombreFeature>Screen(viewModel: <NombreFeature>ViewModel = viewModel(), ...)`.
- Recoge el estado con `val uiState by viewModel.uiState.collectAsStateWithLifecycle()`.
- Observa eventos de un solo disparo con `LaunchedEffect(Unit) { viewModel.uiEvent.collect { ... } }`.
- Llama al Composable de nodo usando el import: `import pe.interbank.ads.mobile.nodo.ui.components.<NombreComponente>.Nodo<NombreComponente>`.
- El `Screen` **solo orquesta**; toda la lógica visual está en el Composable de nodo.
- Maneja el estado `isLoading` con un indicador de progreso y `errorMessage` con un Snackbar o diálogo.
- Incluye un `@Preview` con un `ViewModel` de vista previa (o `fakeViewModel`).

---

### Restricciones

- No generes lógica de negocio en el `Screen`.
- No generes tests en esta entrega.
- Respeta los imports de ambos módulos: usa `pe.interbank.ads.mobile.nodo.ui.*` para UI y `pe.com.interbank.mobilebanking.*` para lógica.
- No uses `LiveData`; solo `StateFlow` y `SharedFlow`.
- Usa `kotlinx.coroutines.flow.update` para mutar el estado.

### Formato de entrega

Genera **5 bloques de código** separados, cada uno con su nombre de archivo como título, en el orden:
1. `<NombreFeature>UiState.kt`
2. `<NombreFeature>UiIntent.kt`
3. `<NombreFeature>UiEvent.kt`
4. `<NombreFeature>ViewModel.kt`
5. `<NombreFeature>Screen.kt`
