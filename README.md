# 🎬🍔 CineFood App

> **Proyecto de aprendizaje Android · Android Learning Project**

---

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-blue?style=for-the-badge)
![Clean Architecture](https://img.shields.io/badge/Clean-Architecture-orange?style=for-the-badge)
![minSdk](https://img.shields.io/badge/minSdk-24-green?style=for-the-badge)

</div>

---

## 🌐 Idioma · Language

- 🇵🇪 [Español](#-español)
- 🇺🇸 [English](#-english)

---

---

# 🇵🇪 ESPAÑOL

---

## 📖 Descripción

**CineFood** es una aplicación Android de aprendizaje desarrollada con **Kotlin** que combina dos secciones: reseñas de películas y ofertas de comida.

El objetivo principal de este proyecto es que los estudiantes que están comenzando con el desarrollo Android puedan:

- Entender la estructura de un proyecto Android real
- Aplicar los conceptos fundamentales de Kotlin
- Comprender la arquitectura **Clean Architecture + MVVM**
- Practicar con componentes como `RecyclerView`, `ViewModel` y `LiveData`

> ⚠️ Este proyecto usa **datos hardcodeados** (sin conexión a backend) para mantener el enfoque en el aprendizaje de la arquitectura y el lenguaje.

---

## ✨ Funcionalidades

- 🎬 **Explorar películas** — Lista horizontal de películas destacadas en la pantalla principal
- 🍔 **Explorar comidas** — Lista vertical de ofertas de comida en la pantalla principal
- 🔍 **Barra de búsqueda** — Interfaz visual (sin funcionalidad en esta versión)
- 📄 **Detalle de película** — Pantalla con título, descripción, director, duración y calificación
- 🛒 **Detalle de comida** — Pantalla con nombre, precio, descripción y botón "Agregar al Pedido"
- ⭐ **Calificaciones** — Estrellas y puntuación para películas y comidas
- 🌙 **Tema oscuro** — Diseño inspirado en plataformas como Netflix

---

## 📸 Capturas de Pantalla

> 💡 Agrega aquí las capturas de pantalla de tu aplicación.

| Pantalla Principal | Detalle de Película | Detalle de Comida |
|:---:|:---:|:---:|
| _(captura aquí)_ | _(captura aquí)_ | _(captura aquí)_ |

---

## 🏛️ Arquitectura

Este proyecto implementa **Clean Architecture** dividida en tres capas:

```
┌─────────────────────────────────┐
│        PRESENTATION LAYER       │  ← Actividades, ViewModels, Adapters
│  (Lo que el usuario ve y toca)  │
├─────────────────────────────────┤
│          DOMAIN LAYER           │  ← Casos de uso (Use Cases)
│    (Las reglas del negocio)     │
├─────────────────────────────────┤
│           DATA LAYER            │  ← Modelos, Repositorios
│  (De dónde vienen los datos)    │
└─────────────────────────────────┘
```

### ¿Por qué Clean Architecture?

- Cada capa tiene **una sola responsabilidad** (Principio S de SOLID)
- Las capas internas **no conocen** a las capas externas
- El código es más **fácil de probar** y mantener
- Cambiar la fuente de datos (ej. pasar de datos locales a una API) **no afecta** la UI

### Patrón MVVM

```
VISTA (Activity)  ──observa──▶  VIEWMODEL  ──llama──▶  USE CASE  ──consulta──▶  REPOSITORY
       │                             │
       └──────── eventos ────────────┘
```

- **View** (Activity): Solo muestra datos y captura eventos del usuario
- **ViewModel**: Mantiene el estado de la UI y sobrevive a rotaciones de pantalla
- **Model** (Use Cases + Repository): Provee los datos

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/microsol/myappzegel/
│
├── 📂 data/
│   ├── 📂 model/
│   │   ├── Movie.kt          ← Data class de película + value class MovieId
│   │   └── Food.kt           ← Data class de comida + getter personalizado
│   └── 📂 repository/
│       ├── MovieRepository.kt        ← Interfaz del repositorio
│       ├── FoodRepository.kt         ← Interfaz del repositorio
│       ├── MovieRepositoryImpl.kt    ← Implementación con datos falsos
│       └── FoodRepositoryImpl.kt     ← Implementación con datos falsos
│
├── 📂 domain/
│   └── 📂 usecase/
│       ├── GetMoviesUseCase.kt   ← Caso de uso para películas
│       └── GetFoodsUseCase.kt    ← Caso de uso para comidas
│
├── 📂 presentation/
│   ├── 📂 home/
│   │   ├── HomeViewModel.kt          ← Estado de la pantalla principal
│   │   ├── HomeViewModelFactory.kt
│   │   └── 📂 adapter/
│   │       ├── MovieAdapter.kt   ← Adapter del RecyclerView de películas
│   │       └── FoodAdapter.kt    ← Adapter del RecyclerView de comidas
│   ├── 📂 moviedetail/
│   │   ├── MovieDetailActivity.kt
│   │   ├── MovieDetailViewModel.kt
│   │   └── MovieDetailViewModelFactory.kt
│   └── 📂 fooddetail/
│       ├── FoodDetailActivity.kt
│       ├── FoodDetailViewModel.kt
│       └── FoodDetailViewModelFactory.kt
│
└── MainActivity.kt   ← Pantalla principal (Home)
```

---

## 🧩 Conceptos Kotlin Aplicados

| Concepto | Dónde se usa | Descripción breve |
|---|---|---|
| `data class` | `Movie.kt`, `Food.kt` | Clase que solo guarda datos; genera `equals()`, `toString()`, `copy()` automáticamente |
| `value class` | `MovieId`, `FoodId` | Envuelve un primitivo sin costo extra en memoria (type-safety) |
| `val` vs `var` | En todos los archivos | `val` = inmutable (no cambia), `var` = mutable (puede cambiar) |
| Custom getter | `Food.formattedPrice` | Propiedad calculada que se computa cada vez que se accede |
| `interface` | `MovieRepository`, `FoodRepository` | Define un contrato que las clases deben cumplir |
| Lambda `(T) -> Unit` | Adapters (onClick) | Función anónima pasada como parámetro para manejar clics |
| Destructuring | Dentro de adapters | Descompone un `data class` en variables: `val (id, title) = movie` |
| `operator fun invoke()` | Use cases | Permite llamar un objeto como función: `getMoviesUseCase()` |
| `companion object` | Activities de detalle | Equivale a los campos `static` de Java |

---

## 🚀 Cómo Ejecutar el Proyecto

### Requisitos previos

- ✅ Android Studio **Ladybug** (2024.2) o superior
- ✅ JDK 11 o superior
- ✅ Dispositivo físico o emulador con **Android 7.0 (API 24)** o superior

### Pasos

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/cinefood-app.git
   ```

2. **Abre el proyecto en Android Studio**
   - Abre Android Studio
   - Selecciona `File → Open`
   - Navega hasta la carpeta del proyecto y selecciónala

3. **Sincroniza Gradle**
   - Android Studio sincronizará automáticamente
   - Si no, haz clic en `Sync Now` en la barra superior amarilla

4. **Ejecuta la app**
   - Conecta un dispositivo Android o inicia un emulador
   - Presiona el botón ▶️ `Run` (Shift + F10)

---

## 🎓 Propósito de Aprendizaje

Este proyecto fue creado para que los estudiantes puedan:

- 🏗️ **Entender la arquitectura** de una app Android real con Clean Architecture + MVVM
- 🔤 **Practicar Kotlin** con ejemplos concretos de cada concepto del lenguaje
- 📱 **Conocer los componentes** principales: `RecyclerView`, `ViewModel`, `LiveData`, `ViewBinding`
- 🔄 **Ver cómo fluye la información** desde los datos hasta la pantalla
- 🧱 **Aplicar principios SOLID** de forma natural en el código

### Lo que aprenderás leyendo este código

1. Cómo separar responsabilidades en capas
2. Por qué el ViewModel sobrevive a la rotación de pantalla
3. Cómo las interfaces permiten cambiar implementaciones sin tocar el resto del código
4. La diferencia entre una `data class` y una clase normal
5. Cómo pasar datos entre pantallas con `Intent` extras

---

## 🔮 Mejoras Futuras

Estas son ideas para extender el proyecto una vez que domines los conceptos básicos:

- [ ] 🌐 Conectar a una API real (ej. The Movie DB API, TheMealDB)
- [ ] 🗄️ Agregar persistencia local con **Room Database**
- [ ] 🔑 Implementar autenticación con **Firebase Auth**
- [ ] 🧭 Migrar la navegación a **Jetpack Navigation Component**
- [ ] 💉 Implementar inyección de dependencias con **Hilt**
- [ ] 🧪 Agregar pruebas unitarias con **JUnit** y **MockK**
- [ ] 🎨 Migrar la UI a **Jetpack Compose**
- [ ] 🛒 Implementar un carrito de pedidos funcional con **StateFlow**

---

## 📚 Recursos para Estudiantes

- [Documentación oficial de Kotlin](https://kotlinlang.org/docs/home.html)
- [Guías de Android Developers](https://developer.android.com/guide)
- [Codelabs de Android](https://developer.android.com/codelabs)
- [Arquitectura de apps Android](https://developer.android.com/topic/architecture)

---

---

# 🇺🇸 ENGLISH

---

## 📖 Description

**CineFood** is an Android learning app built with **Kotlin** that combines two sections: movie reviews and food offers.

The main goal of this project is to help students who are starting Android development to:

- Understand the structure of a real Android project
- Apply fundamental Kotlin concepts
- Understand **Clean Architecture + MVVM**
- Practice with components like `RecyclerView`, `ViewModel`, and `LiveData`

> ⚠️ This project uses **hardcoded data** (no backend connection) to keep the focus on learning architecture and language concepts.

---

## ✨ Features

- 🎬 **Browse movies** — Horizontal list of featured movies on the home screen
- 🍔 **Browse food offers** — Vertical list of food offers on the home screen
- 🔍 **Search bar** — Visual UI element (non-functional in this version)
- 📄 **Movie detail** — Screen with title, description, director, duration, and rating
- 🛒 **Food detail** — Screen with name, price, description, and "Add to Order" button
- ⭐ **Ratings** — Stars and scores for movies and food items
- 🌙 **Dark theme** — Design inspired by platforms like Netflix

---

## 📸 Screenshots

> 💡 Add your app screenshots here.

| Home Screen | Movie Detail | Food Detail |
|:---:|:---:|:---:|
| _(screenshot here)_ | _(screenshot here)_ | _(screenshot here)_ |

---

## 🏛️ Architecture

This project implements **Clean Architecture** divided into three layers:

```
┌─────────────────────────────────┐
│        PRESENTATION LAYER       │  ← Activities, ViewModels, Adapters
│     (What the user sees)        │
├─────────────────────────────────┤
│          DOMAIN LAYER           │  ← Use Cases
│       (Business rules)          │
├─────────────────────────────────┤
│           DATA LAYER            │  ← Models, Repositories
│      (Where data comes from)    │
└─────────────────────────────────┘
```

### Why Clean Architecture?

- Each layer has **a single responsibility** (SOLID's S principle)
- Inner layers **do not know** about outer layers
- Code is easier to **test and maintain**
- Swapping the data source (e.g. local data → API) **does not affect** the UI

### MVVM Pattern

```
VIEW (Activity)  ──observes──▶  VIEWMODEL  ──calls──▶  USE CASE  ──queries──▶  REPOSITORY
      │                              │
      └────────── events ────────────┘
```

- **View** (Activity): Only displays data and captures user events
- **ViewModel**: Holds UI state and survives screen rotations
- **Model** (Use Cases + Repository): Provides the data

---

## 📁 Project Structure

```
app/src/main/java/com/microsol/myappzegel/
│
├── 📂 data/
│   ├── 📂 model/
│   │   ├── Movie.kt          ← Movie data class + MovieId value class
│   │   └── Food.kt           ← Food data class + custom getter
│   └── 📂 repository/
│       ├── MovieRepository.kt        ← Repository interface
│       ├── FoodRepository.kt         ← Repository interface
│       ├── MovieRepositoryImpl.kt    ← Hardcoded data implementation
│       └── FoodRepositoryImpl.kt     ← Hardcoded data implementation
│
├── 📂 domain/
│   └── 📂 usecase/
│       ├── GetMoviesUseCase.kt   ← Use case for movies
│       └── GetFoodsUseCase.kt    ← Use case for food
│
├── 📂 presentation/
│   ├── 📂 home/
│   │   ├── HomeViewModel.kt          ← Home screen state
│   │   ├── HomeViewModelFactory.kt
│   │   └── 📂 adapter/
│   │       ├── MovieAdapter.kt   ← Movie RecyclerView adapter
│   │       └── FoodAdapter.kt    ← Food RecyclerView adapter
│   ├── 📂 moviedetail/
│   │   ├── MovieDetailActivity.kt
│   │   ├── MovieDetailViewModel.kt
│   │   └── MovieDetailViewModelFactory.kt
│   └── 📂 fooddetail/
│       ├── FoodDetailActivity.kt
│       ├── FoodDetailViewModel.kt
│       └── FoodDetailViewModelFactory.kt
│
└── MainActivity.kt   ← Home screen entry point
```

---

## 🧩 Kotlin Concepts Applied

| Concept | Where used | Brief description |
|---|---|---|
| `data class` | `Movie.kt`, `Food.kt` | Class that only holds data; auto-generates `equals()`, `toString()`, `copy()` |
| `value class` | `MovieId`, `FoodId` | Wraps a primitive without memory overhead (type-safety) |
| `val` vs `var` | Throughout all files | `val` = immutable (read-only), `var` = mutable (can change) |
| Custom getter | `Food.formattedPrice` | Computed property re-evaluated every time it is accessed |
| `interface` | `MovieRepository`, `FoodRepository` | Defines a contract that implementing classes must fulfill |
| Lambda `(T) -> Unit` | Adapters (onClick) | Anonymous function passed as a parameter to handle clicks |
| Destructuring | Inside adapters | Unpacks a `data class` into variables: `val (id, title) = movie` |
| `operator fun invoke()` | Use cases | Allows calling an object like a function: `getMoviesUseCase()` |
| `companion object` | Detail activities | Equivalent to Java's `static` fields |

---

## 🚀 How to Run

### Prerequisites

- ✅ Android Studio **Ladybug** (2024.2) or higher
- ✅ JDK 11 or higher
- ✅ Physical device or emulator running **Android 7.0 (API 24)** or higher

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/cinefood-app.git
   ```

2. **Open the project in Android Studio**
   - Open Android Studio
   - Select `File → Open`
   - Navigate to the project folder and select it

3. **Sync Gradle**
   - Android Studio will sync automatically
   - If not, click `Sync Now` in the yellow top bar

4. **Run the app**
   - Connect an Android device or start an emulator
   - Press the ▶️ `Run` button (Shift + F10)

---

## 🎓 Learning Purpose

This project was created so students can:

- 🏗️ **Understand architecture** of a real Android app using Clean Architecture + MVVM
- 🔤 **Practice Kotlin** with concrete examples of each language concept
- 📱 **Learn key components**: `RecyclerView`, `ViewModel`, `LiveData`, `ViewBinding`
- 🔄 **See how data flows** from the data layer all the way to the screen
- 🧱 **Apply SOLID principles** naturally within the codebase

### What you will learn by reading this code

1. How to separate responsibilities into layers
2. Why the ViewModel survives screen rotation
3. How interfaces let you swap implementations without touching other code
4. The difference between a `data class` and a regular class
5. How to pass data between screens using `Intent` extras

---

## 🔮 Future Improvements

Ideas to extend the project once you have mastered the basics:

- [ ] 🌐 Connect to a real API (e.g. The Movie DB API, TheMealDB)
- [ ] 🗄️ Add local persistence with **Room Database**
- [ ] 🔑 Implement authentication with **Firebase Auth**
- [ ] 🧭 Migrate navigation to **Jetpack Navigation Component**
- [ ] 💉 Implement dependency injection with **Hilt**
- [ ] 🧪 Add unit tests with **JUnit** and **MockK**
- [ ] 🎨 Migrate the UI to **Jetpack Compose**
- [ ] 🛒 Build a functional shopping cart with **StateFlow**

---

## 📚 Resources for Students

- [Official Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Android Developers Guides](https://developer.android.com/guide)
- [Android Codelabs](https://developer.android.com/codelabs)
- [Android App Architecture](https://developer.android.com/topic/architecture)

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Kotlin | 2.0+ | Programming language |
| Android Gradle Plugin | 9.1.0 | Build system |
| AndroidX AppCompat | 1.6.1 | Backward compatibility |
| Material Components | 1.10.0 | UI design system |
| ConstraintLayout | 2.1.4 | Flexible XML layouts |
| RecyclerView | 1.3.2 | Scrollable lists |
| CardView | 1.0.0 | Card UI component |
| Lifecycle ViewModel | 2.8.7 | MVVM support |
| Lifecycle LiveData | 2.8.7 | Observable data holder |

---

## 📝 License

This project is for **educational purposes only**.  
Este proyecto es solo con **fines educativos**.

```
MIT License — feel free to use, modify, and share for learning.
```

---

<div align="center">

Made with ❤️ for Android students · Hecho con ❤️ para estudiantes de Android

⭐ If this project helped you learn, give it a star! · ¡Si este proyecto te ayudó a aprender, dale una estrella!

</div>
