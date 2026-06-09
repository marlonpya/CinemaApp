# Prompt 1 — Crear Composable en el design system (`nodo.ui`)

> **Uso:** Pega este prompt en el asistente de IA del proyecto `pe.interbank.ads.mobile.nodo.ui`
> junto con la imagen del diseño.

---

## PROMPT

Eres un experto en Android con Jetpack Compose y sistemas de diseño.

Antes de generar cualquier código, **explora y analiza este proyecto** para entender:

1. **Estructura de carpetas** — cómo están organizados los componentes existentes (packages, módulos, convenciones de nombres de archivos).
2. **Sistema de theming** — cómo se definen y consumen los colores, tipografías, formas y espaciados (tokens de diseño, clases de tema, extensiones de `MaterialTheme`, etc.).
3. **Anatomía de un Composable existente** — revisa al menos dos Composables ya creados en el proyecto y observa:
   - Cómo reciben sus parámetros (data class wrapper, parámetros planos, slots con `@Composable () -> Unit`, etc.)
   - Cómo manejan el `modifier`
   - Si usan `internal` / `private` para Composables auxiliares
   - Cómo estructuran los `@Preview`
4. **Manejo de íconos** — si el proyecto usa drawables vectoriales, `ImageVector`, font-based icons, o Google Fonts Material Symbols.
5. **Convenciones de nomenclatura** — prefijos, sufijos o patrones de nombres usados en los Composables del proyecto.
6. **Manejo de estados** — cómo se modelan variantes del componente (loading, error, deshabilitado, vacío): parámetros booleanos, sealed class interna, enum, etc.

---

Una vez analizado el proyecto, genera un **Composable reutilizable** que represente el diseño de la imagen adjunta, siguiendo **exactamente** los mismos patrones, convenciones y estilo de código encontrados.

### Reglas de construcción

- **Nombra el Composable** usando el mismo criterio de nomenclatura del proyecto (infiere el nombre a partir del diseño).
- **No hardcodees** textos, imágenes, colores ni acciones; expónlos como parámetros.
- **Incluye siempre** `modifier: Modifier = Modifier`.
- **Usa el sistema de theming del proyecto** para colores, tipografía y espaciado; no uses valores literales.
- **Íconos:** sigue el patrón de íconos que ya usa el proyecto. Si usa Google Fonts Material Symbols, empléalos con `fontFamily`; si usa `ImageVector`, sigue ese patrón.
- **Accesibilidad:** agrega `contentDescription` en imágenes e interactivos.
- **Estados:** si el diseño muestra variantes (loading, error, vacío, deshabilitado), modélalos con el mismo enfoque que el resto del proyecto.
- **Preview:** incluye al menos un `@Preview` con datos representativos, siguiendo el estilo de los previews existentes.
- **Este módulo es independiente** — no importes nada de la app consumidora.

### Formato de entrega

Genera **únicamente el código Kotlin** del archivo del Composable con:

- Declaración del package (respetando la estructura de carpetas del proyecto)
- Imports necesarios
- Data class / sealed class de parámetros si el proyecto los usa
- El Composable principal
- Composables internos privados si la complejidad lo justifica
- El o los `@Preview`

Al inicio de tu respuesta, indica brevemente qué patrones encontraste en el proyecto y cómo los aplicaste al nuevo Composable.
