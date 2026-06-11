# Prompt — Crear Composable en el Design System (`nodo.ui`)

> **Uso:** Pega este prompt en el asistente de IA junto con la imagen del diseño.

---

## PROMPT

Eres un experto en Android con Jetpack Compose y sistemas de diseño.

Antes de generar cualquier código, **explora y analiza este proyecto** para entender su arquitectura concreta. A continuación te doy el contexto base ya analizado, pero debes verificar los detalles en los archivos relevantes al componente que vas a crear.

---

### Contexto del proyecto `pe.interbank.ads.mobile.nodo.ui`

**Módulo principal:** `:ui` → `ui/src/main/java/pe/interbank/ads/mobile/nodo/ui/`

#### 1. Estructura de carpetas — patrón estricto por componente

```
<componente>/
├── PublicVariant1.kt        # API pública — solo expone parámetros de consumo
├── PublicVariant2.kt
├── internal/
│   ├── Base<Componente>.kt          # Implementación interna con toda la lógica
│   ├── Nodo<Componente>Defaults.kt  # Objeto internal con tokens de diseño (@Composable)
│   ├── <Componente>Typography.kt    # data class interna para estilos por tamaño
│   └── <Componente>Dimensions.kt   # data class interna para dimensiones por tamaño
└── previews/                        # o preview/ — archivos de @Preview
    └── <Componente>Preview.kt
```

Ejemplos reales: `button/`, `card/`, `chip/`, `checkbox/`, `listitem/`, `avatar/`, `badge/`, `tab/`, `textfield/`, `slider/`, `snackBar/`, `tooltip/`, `stepper/`, `switchcontrol/`

#### 2. Sistema de theming — acceso obligatorio vía `NodoTheme`

```kotlin
// Colores semánticos (nunca Color(0xFF...) ni MaterialTheme)
NodoTheme.colorScheme.primary
NodoTheme.colorScheme.onPrimary
NodoTheme.colorScheme.surface
NodoTheme.colorScheme.onSurface
NodoTheme.colorScheme.onSurfaceVariant
NodoTheme.colorScheme.outlineHigh
NodoTheme.colorScheme.error
NodoTheme.colorScheme.surfaceFixed
NodoTheme.colorScheme.onSurfaceFixed

// Tipografía — escala Nodo (nunca MaterialTheme.typography)
NodoTheme.typography.bodyMediumMedium
NodoTheme.typography.bodySmallRegular
NodoTheme.typography.bodyLargeMedium
NodoTheme.typography.labelLargeMedium

// Espaciado — base 4dp, multiplier 1–10 (nunca valores .dp literales en composables)
NodoTheme.spacing.spacing1  // 4dp
NodoTheme.spacing.spacing2  // 8dp
NodoTheme.spacing.spacing3  // 12dp
NodoTheme.spacing.spacing4  // 16dp
NodoTheme.spacing.spacing6  // 24dp
NodoTheme.spacing.get(factor)  // para valores personalizados

// Formas
NodoTheme.shapes.medium
NodoTheme.shapes.large
```

> **Excepción:** En objetos `internal Defaults` que NO son `@Composable` (fuera del árbol de composición), sí se usan `.dp` para constantes dimensionales — ver `NodoButtonDefaults` y `NodoCardDefaults` como referencia.

#### 3. Patrón de variantes — separar composables públicos por variante

```kotlin
// ✅ Correcto: composables separados por variante
@Composable
fun PrimaryButton(onClick: () -> Unit, ...) {
    BaseButton(colors = NodoButtonDefaults.primaryButtonColors(), ...)
}

@Composable
fun SecondaryButton(onClick: () -> Unit, ...) {
    BaseButton(colors = NodoButtonDefaults.secondaryButtonColors(), ...)
}

// ❌ Incorrecto: nunca pasar tipo/color como enum o Color directamente
fun Button(type: ButtonType, color: Color, ...)
```

Cuando un componente tiene variantes semánticas (Positive/Negative/Destructive, Elevated/Outlined, Filled/Outlined), se exponen como **composables públicos separados** o como **enum interno** sin exponer colores.

#### 4. Manejo de íconos

El proyecto usa la librería `nodo-icons`:

```kotlin
import pe.interbank.ads.mobile.nodo.icons.Icons

// Uso con painterResource (no ImageVector)
Icon(
    painter = painterResource(Icons.Filled.edit),
    contentDescription = null,
)
Icon(
    painter = painterResource(Icons.Outline.chevron_right),
    contentDescription = null,
    modifier = Modifier.size(24.dp),
)
```

Los slots de íconos se definen como `@Composable (() -> Unit)?` para máxima flexibilidad.

#### 5. Anatomía de un composable público

```kotlin
@Composable
fun PrimaryButton(
    onClick: () -> Unit,              // callbacks primero
    modifier: Modifier = Modifier,    // modifier siempre presente con default
    enabled: Boolean = true,
    isSkeleton: Boolean = false,      // soporte skeleton state
    type: ButtonType = ButtonType.Positive,
    size: ButtonSize = ButtonSize.Medium,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    trailingIcon: @Composable (() -> Unit)? = null,   // slots opcionales
    leadingIcon: @Composable (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,          // slot de contenido al final
) { ... }
```

**Reglas de firma:**
- `modifier: Modifier = Modifier` siempre presente con default
- Slots de contenido: `@Composable (() -> Unit)?` o `@Composable RowScope.() -> Unit`
- `interactionSource` cuando el componente es interactivo
- `isSkeleton: Boolean = false` si el componente soporta estado esqueleto
- `loading: Boolean = false` si el componente soporta estado de carga
- Trailing comma requerida en parámetros multilínea

#### 6. Objeto `internal Defaults`

```kotlin
internal object Nodo<Componente>Defaults {
    // Constantes dimensionales (pueden usar .dp directamente aquí)
    val borderWidth = 2.dp

    // Funciones @Composable que acceden a NodoTheme
    @Composable
    fun primaryColors() = ComponentColors(
        containerColor = NodoTheme.colorScheme.primary,
        contentColor = NodoTheme.colorScheme.onPrimary,
    )

    // Extensiones sobre ColorScheme para caching
    internal val ColorScheme.defaultComponentColors: ComponentColors
        get() { ... }
}
```

#### 7. Previews

```kotlin
// Importar la anotación interna del proyecto (siempre usar esta, no @Preview directamente)
import pe.interbank.ads.mobile.nodo.ui.internal.LightDarkPreview

@LightDarkPreview  // genera previews light + dark automáticamente
@Composable
internal fun MyComponentPreview() {
    NodoAppTheme {
        Surface(color = NodoTheme.colorScheme.surface) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                MyComponent(/* estado default */)
                MyComponent(/* estado loading */)
                MyComponent(/* estado disabled */)
            }
        }
    }
}
```

#### 8. Manejo de estados — patrón consistente

| Estado | Implementación |
|---|---|
| **Skeleton** | `isSkeleton: Boolean` + `Modifier.skeleton(isSkeleton, shape)` |
| **Loading** | `loading: Boolean` → muestra `DotsBouncing()` en lugar del contenido |
| **Disabled** | `enabled: Boolean` → opacidad via `colors.containerColor(enabled)` |
| **Error** | `isError: Boolean` → cambia colores vía `NodoTheme.colorScheme.error` |
| **Selected/Checked** | booleanos planos sin estado interno — **hoisting obligatorio** |

#### 9. `Surface` del proyecto

Usar `pe.interbank.ads.mobile.nodo.ui.surface.Surface` (wrapper interno) en lugar de `androidx.compose.material3.Surface` para componentes interactivos, ya que incluye la lógica de ripple/focus del sistema Nodo.

#### 10. Visibilidad

| Visibilidad | Aplicar a |
|---|---|
| `internal` | `Base<X>`, `Nodo<X>Defaults`, data classes de configuración, previews |
| `public` | Composables de variante (`PrimaryX`, `SecondaryX`), enums de tipo/tamaño expuestos en la API |
| `@Immutable` | Data classes de parámetros y enums |

---

### Tarea

Con base en el análisis anterior y la imagen adjunta, genera un **Composable reutilizable** siguiendo **exactamente** los mismos patrones.

#### Reglas de construcción

1. **Infiere el nombre** del componente a partir del diseño usando PascalCase y nomenclatura sustantiva (e.g., `SpotHero`, `AmountLabel`, `EmptyErrorState`).
2. **Crea la estructura de archivos completa** con package `pe.interbank.ads.mobile.nodo.ui.<nombre_componente>`:
   - Composables públicos de variante
   - `internal/Base<Componente>.kt`
   - `internal/Nodo<Componente>Defaults.kt`
   - Data classes de configuración internas si aplica
3. **No hardcodees** textos, colores, imágenes ni callbacks.
4. **Usa `NodoTheme`** para colores, tipografía y espaciado. Nunca `Color(0xFF...)`, `MaterialTheme.*` ni valores `.dp` directos en composables (solo en constantes del objeto `Defaults`).
5. **Íconos:** slots `@Composable (() -> Unit)?` o `painterResource(Icons.Filled/Outline.<nombre>)`.
6. **Accesibilidad:** `contentDescription` en todos los elementos visuales sin texto visible.
7. **Estados del diseño:** modélalos con el mismo patrón del proyecto (`enabled`, `loading`, `isSkeleton`, `isError`, `selected`).
8. **Preview:** usa `@LightDarkPreview` + `NodoAppTheme` mostrando todas las variantes visibles en el diseño.
9. **KDoc en composables públicos:** incluye descripción breve, link de Figma (placeholder si no lo tienes) y `@param` para cada parámetro.
10. **Trailing commas** en todas las listas de parámetros multilínea.

---

### Formato de entrega

Genera **únicamente código Kotlin**, un bloque por archivo, en este orden:

1. Tipos de soporte (`enum class`, `data class`) si el componente los requiere
2. `internal/Nodo<Componente>Defaults.kt` — tokens y colores
3. `internal/Base<Componente>.kt` — implementación completa
4. Composables públicos de variante (uno por archivo)
5. Previews al final del archivo público (o en archivo separado si la complejidad lo justifica)

Cada bloque debe incluir:
- Declaración de package
- Imports necesarios
- Código del archivo

**Al inicio de tu respuesta, indica brevemente:**
- Nombre inferido del componente y su package
- Variantes identificadas en el diseño
- Estados modelados
- Cómo mapeaste el diseño a los tokens de `NodoTheme`

