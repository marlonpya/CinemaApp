# 🎬🍔 CineFood App

> **Proyecto de aprendizaje Android · Android Learning Project**

---

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-blue?style=for-the-badge)
![Etapa 1](https://img.shields.io/badge/Refactor-Etapa%201-orange?style=for-the-badge)
![minSdk](https://img.shields.io/badge/minSdk-24-green?style=for-the-badge)
![TMDb](https://img.shields.io/badge/API-TMDb-01B4E4?style=for-the-badge)
![TheMealDB](https://img.shields.io/badge/API-TheMealDB-F9A825?style=for-the-badge)

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

**CineFood** es una aplicación Android de aprendizaje desarrollada con **Kotlin** que combina dos secciones: exploración de películas/series y ofertas de comida. Los datos provienen de **APIs reales**:

- 🎬 **The Movie Database (TMDb)** — películas y series populares con imágenes reales
- 🍔 **TheMealDB** — recetas y comidas de todo el mundo

El objetivo principal es que los estudiantes que están comenzando con el desarrollo Android puedan:

- Entender la estructura de un proyecto Android real
- Aplicar los conceptos fundamentales de Kotlin
- Comprender la arquitectura **MVVM + Repository Pattern**
- Practicar con **Coroutines**, **Retrofit**, **LiveData** y **RecyclerView**
- Ver cómo integrar APIs REST reales de forma limpia y segura

---

> ### 📌 Estado actual: Refactor Etapa 1
>
> Este proyecto está en su versión **pedagógica simplificada (Etapa 1)**. El objetivo es que los estudiantes entiendan las bases antes de avanzar a arquitecturas más complejas.
>
> **Lo que se simplificó respecto a la versión anterior:**
> - Se eliminaron los **Use Cases** — el ViewModel habla directamente con el Repository
> - Se eliminaron los **Mappers** como archivos separados — el mapeo DTO → modelo vive en el RepositoryImpl
> - Se eliminaron las **value classes** (`MovieId`, `FoodId`) — los IDs son `Int` planos
> - Se reemplazó **ViewBinding** en el código por `findByViewId` — más explícito para aprender
> - Los **adapters** ahora usan `ListAdapter` con `DiffUtil` en lugar de `notifyDataSetChanged()`
> - Las interfaces de **Repository** viven en `domain/repository` (contrato de dominio)
> - Los **modelos de dominio** (`Movie`, `Food`) viven en `domain/model`
>
> **Lo que se mantiene igual:**
> - MVVM con ViewModel + LiveData
> - Repository Pattern
> - ViewModelFactory manual
> - Retrofit + Coroutines
> - Manejo de errores y estado de carga

---

## ✨ Funcionalidades

- 🎬 **Explorar películas** — Lista horizontal con pósters reales desde TMDb
- 🍔 **Explorar comidas** — Lista vertical con imágenes reales desde TheMealDB
- 🔍 **Barra de búsqueda** — Interfaz visual (preparada para futuras búsquedas)
- 📄 **Detalle de película** — Título, descripción, director, duración, género y calificación reales
- 🛒 **Detalle de comida** — Nombre, precio simulado, categoría, instrucciones y botón "Agregar al Pedido"
- ⭐ **Calificaciones** — Basadas en datos reales de TMDb (escala 0–5)
- 🌙 **Tema oscuro** — Diseño inspirado en plataformas como Netflix
- ⏳ **Estado de carga** — Indicador de progreso mientras se obtienen los datos
- ⚠️ **Manejo de errores** — Mensaje visible cuando hay problemas de red

---

## 📸 Capturas de Pantalla

> 💡 Agrega aquí las capturas de pantalla de tu aplicación.

| Pantalla Principal | Detalle de Película | Detalle de Comida |
|:---:|:---:|:---:|
| _(captura aquí)_ | _(captura aquí)_ | _(captura aquí)_ |

---

## 🏛️ Arquitectura

Este proyecto implementa **MVVM + Repository Pattern** (Etapa 1) dividido en tres capas:

```
┌─────────────────────────────────────────────┐
│            PRESENTATION LAYER               │  ← Activities, ViewModels, Adapters
│        (Lo que el usuario ve y toca)        │    findViewById · ListAdapter · LiveData
├─────────────────────────────────────────────┤
│               DOMAIN LAYER                  │  ← Modelos limpios + contratos Repository
│          (Las reglas del negocio)           │    domain/model · domain/repository
├─────────────────────────────────────────────┤
│                DATA LAYER                   │  ← Implementaciones + API + DTOs
│         (De dónde vienen los datos)         │
│   remote/ → RetrofitClient, ApiServices     │
│   dto/    → MovieDto, MealDto               │
│   repository/ → RepositoryImpl (mapeo aquí) │
└─────────────────────────────────────────────┘
```

### ¿Por qué esta arquitectura?

- Cada capa tiene **una sola responsabilidad** (Principio S de SOLID)
- Las capas internas **no conocen** a las capas externas
- El código es más **fácil de entender y mantener**
- Cambiar la fuente de datos (ej. de una API a otra) **no afecta** la UI

### Patrón MVVM con Repository (Etapa 1)

```
VISTA (Activity)  ──observa──▶  VIEWMODEL  ──suspend──▶  REPOSITORY
       │                          viewModelScope                │
       │                          launch { }             Retrofit API
       └──────── eventos ──────────────┘
```

- **View** (Activity): Solo muestra datos y captura eventos del usuario. Usa `findByViewId`.
- **ViewModel**: Lanza coroutines, mantiene `isLoading` y `error` como LiveData. Depende del Repository directamente.
- **Repository interface** (domain): Define el contrato de datos — qué puede pedir el ViewModel.
- **RepositoryImpl** (data): Llama a la API con `suspend fun`, mapea DTOs al modelo de dominio de forma inline.

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/microsol/myappzegel/
│
├── 📂 data/
│   ├── 📂 remote/
│   │   ├── RetrofitClient.kt         ← Singleton con dos instancias Retrofit (TMDb + MealDB)
│   │   ├── TmdbApiService.kt         ← Interfaz Retrofit para TMDb
│   │   ├── MealDbApiService.kt       ← Interfaz Retrofit para TheMealDB
│   │   └── 📂 dto/
│   │       ├── MovieDto.kt           ← DTOs: MovieDto, MovieDetailDto, TvShowDto, GenreDto
│   │       └── MealDto.kt            ← DTO: MealDto, MealListResponse
│   └── 📂 repository/
│       ├── MovieRepositoryImpl.kt    ← Llama a TMDb API · mapea DTO → Movie inline
│       └── FoodRepositoryImpl.kt     ← Llama a TheMealDB API · mapea DTO → Food inline
│
├── 📂 domain/
│   ├── 📂 model/
│   │   ├── Movie.kt                  ← data class Movie (id: Int)
│   │   └── Food.kt                   ← data class Food con formattedPrice y availabilityLabel
│   └── 📂 repository/
│       ├── MovieRepository.kt        ← Interfaz del contrato (suspend fun)
│       └── FoodRepository.kt         ← Interfaz del contrato (suspend fun)
│
├── 📂 presentation/
│   ├── 📂 home/
│   │   ├── HomeViewModel.kt          ← viewModelScope.launch · isLoading · error
│   │   ├── HomeViewModelFactory.kt   ← Recibe MovieRepository + FoodRepository
│   │   └── 📂 adapter/
│   │       ├── MovieAdapter.kt       ← ListAdapter + DiffUtil · findByViewId · Coil
│   │       └── FoodAdapter.kt        ← ListAdapter + DiffUtil · findByViewId · Coil
│   ├── 📂 moviedetail/
│   │   ├── MovieDetailActivity.kt    ← findByViewId · ViewModelProvider · Coil
│   │   ├── MovieDetailViewModel.kt   ← Recibe MovieRepository directamente
│   │   └── MovieDetailViewModelFactory.kt
│   └── 📂 fooddetail/
│       ├── FoodDetailActivity.kt     ← findByViewId · ViewModelProvider · Coil
│       ├── FoodDetailViewModel.kt    ← Recibe FoodRepository directamente
│       └── FoodDetailViewModelFactory.kt
│
└── MainActivity.kt   ← Pantalla principal (Home) · findByViewId · ViewModelProvider
```

---

## 🧩 Conceptos Kotlin Aplicados

| Concepto | Dónde se usa | Descripción breve |
|---|---|---|
| `data class` | `Movie.kt`, `Food.kt`, DTOs | Clase que solo guarda datos; genera `equals()`, `toString()`, `copy()` automáticamente |
| `val` vs `var` | En todos los archivos | `val` = inmutable (no cambia), `var` = mutable (puede cambiar) |
| Custom getter | `Food.formattedPrice`, `availabilityLabel` | Propiedad calculada que se computa cada vez que se accede, sin campo de respaldo |
| `interface` | `MovieRepository`, `FoodRepository` | Define un contrato que las clases deben cumplir |
| Lambda `(T) -> Unit` | Adapters (onClick) | Función anónima pasada como parámetro para manejar clics |
| `suspend fun` | Repositorios, ViewModels | Función que puede pausarse sin bloquear el hilo principal |
| `viewModelScope.launch` | ViewModels | Lanza una coroutine ligada al ciclo de vida del ViewModel |
| `try/catch` | ViewModels | Manejo de errores de red dentro de coroutines |
| `companion object` | Activities de detalle | Equivale a los campos `static` de Java |
| `@SerializedName` | DTOs | Mapea campos JSON con nombres distintos a propiedades Kotlin |
| `object` singleton | `RetrofitClient` | Una sola instancia compartida en toda la app |
| `by lazy` | `RetrofitClient` | Inicialización diferida: se crea solo cuando se necesita por primera vez |
| `DiffUtil.ItemCallback` | Adapters | Compara listas eficientemente para actualizar solo los items que cambiaron |
| `ListAdapter` | `MovieAdapter`, `FoodAdapter` | Adapter moderno que usa DiffUtil automáticamente al llamar `submitList()` |

---

## 🚀 Cómo Ejecutar el Proyecto

### Requisitos previos

- ✅ Android Studio **Meerkat** (2025.1) o superior
- ✅ JDK 21 o superior
- ✅ Dispositivo físico o emulador con **Android 7.0 (API 24)** o superior
- ✅ Conexión a Internet (para las APIs)

### Configuración de credenciales (importante)

Este proyecto consume la **API de TMDb**, que requiere una clave de acceso.

1. **Obtén tu clave en** [https://www.themoviedb.org/settings/api](https://www.themoviedb.org/settings/api) (registro gratuito)

2. **Abre `local.properties`** en la raíz del proyecto (Android Studio lo muestra en la vista de proyecto)

3. **Agrega tus credenciales** al final del archivo:
   ```properties
   TMDB_API_KEY=tu_api_key_aqui
   TMDB_READ_ACCESS_TOKEN=tu_read_access_token_aqui
   ```

> ⚠️ **Nunca** subas `local.properties` a Git. El archivo ya está en `.gitignore` por defecto en Android.

### Pasos

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/cinefood-app.git
   ```

2. **Configura las credenciales** (ver sección anterior)

3. **Abre el proyecto en Android Studio**
   - Abre Android Studio
   - Selecciona `File → Open`
   - Navega hasta la carpeta del proyecto y selecciónala

4. **Sincroniza Gradle**
   - Android Studio sincronizará automáticamente y descargará las dependencias
   - Si no, haz clic en `Sync Now` en la barra superior amarilla

5. **Ejecuta la app**
   - Conecta un dispositivo Android o inicia un emulador
   - Presiona el botón ▶️ `Run` (Shift + F10)

---

## 🎓 Propósito de Aprendizaje

Este proyecto (Etapa 1) fue creado para que los estudiantes puedan:

- 🏗️ **Entender MVVM** con ViewModel, LiveData y Repository Pattern
- 📱 **Conocer los componentes** principales: `RecyclerView`, `ViewModel`, `LiveData`, `findByViewId`
- 🌐 **Integrar APIs REST** usando Retrofit, OkHttp y Coroutines
- 🔄 **Ver cómo fluye la información** desde la red hasta la pantalla
- 🧱 **Aplicar principios SOLID** de forma natural en el código
- 🧩 **Entender el Repository Pattern** como abstracción de la fuente de datos

### Lo que aprenderás leyendo este código

1. Cómo separar responsabilidades entre Activity, ViewModel y Repository
2. Por qué el ViewModel sobrevive a la rotación de pantalla
3. Cómo las interfaces permiten cambiar implementaciones sin tocar el resto del código
4. La diferencia entre un DTO y un modelo de dominio
5. Cómo pasar datos entre pantallas con `Intent` extras
6. Cómo hacer llamadas de red asíncronas con `suspend fun` y `viewModelScope`
7. Cómo cargar imágenes desde URLs con Coil
8. Por qué `ListAdapter` con `DiffUtil` es más eficiente que `notifyDataSetChanged()`

---

## 🔮 Hoja de Ruta del Refactor

| Etapa | Estado | Contenido |
|---|---|---|
| **Etapa 1** | ✅ **Actual** | MVVM · Repository · ViewModelFactory manual · `findByViewId` · `ListAdapter` |
| Etapa 2 | ⏳ Pendiente | `sealed class UiState` · manejo de errores robusto · Use Cases · Mapper classes |
| Etapa 3 | ⏳ Pendiente | Hilt · Navigation Component · testing con fake repositories |
| Etapa 4 | ⏳ Pendiente | Jetpack Compose · Flow · Room |

### Mejoras adicionales sugeridas

- [ ] 🔍 Implementar la barra de búsqueda funcional con el endpoint `/search/multi`
- [ ] 🗄️ Agregar persistencia local con **Room Database**
- [ ] 🔑 Implementar autenticación con **Firebase Auth**
- [ ] 🧪 Agregar pruebas unitarias con **JUnit** y fake repositories
- [ ] 🛒 Implementar un carrito de pedidos funcional

---

## 📚 Recursos para Estudiantes

- [Documentación oficial de Kotlin](https://kotlinlang.org/docs/home.html)
- [Guías de Android Developers](https://developer.android.com/guide)
- [Codelabs de Android](https://developer.android.com/codelabs)
- [Arquitectura de apps Android](https://developer.android.com/topic/architecture)
- [Coroutines en Android](https://developer.android.com/kotlin/coroutines)
- [Documentación de Retrofit](https://square.github.io/retrofit/)
- [Documentación de Coil](https://coil-kt.github.io/coil/)
- [API de TMDb](https://developer.themoviedb.org/docs)
- [API de TheMealDB](https://www.themealdb.com/api.php)

---

---

# 🇺🇸 ENGLISH

---

## 📖 Description

**CineFood** is an Android learning app built with **Kotlin** that combines two sections: movie/series exploration and food offers. Data comes from **real APIs**:

- 🎬 **The Movie Database (TMDb)** — real movies and TV shows with actual poster images
- 🍔 **TheMealDB** — real recipes and meals from around the world

The main goal is to help students who are starting Android development to:

- Understand the structure of a real Android project
- Apply fundamental Kotlin concepts
- Understand **MVVM + Repository Pattern**
- Practice with **Coroutines**, **Retrofit**, **LiveData**, and **RecyclerView**
- See how to integrate real REST APIs cleanly and securely

---

> ### 📌 Current state: Refactor Stage 1
>
> This project is in its **simplified pedagogical version (Stage 1)**. The goal is for students to understand the fundamentals before advancing to more complex architectures.
>
> **What was simplified from the previous version:**
> - **Use Cases removed** — ViewModel talks directly to the Repository
> - **Mapper files removed** — DTO → model mapping lives inline inside RepositoryImpl
> - **Value classes removed** (`MovieId`, `FoodId`) — IDs are plain `Int`
> - **ViewBinding replaced** in code by `findByViewId` — more explicit for learning
> - **Adapters** now use `ListAdapter` with `DiffUtil` instead of `notifyDataSetChanged()`
> - **Repository interfaces** live in `domain/repository` (domain contract)
> - **Domain models** (`Movie`, `Food`) live in `domain/model`
>
> **What remains the same:**
> - MVVM with ViewModel + LiveData
> - Repository Pattern
> - Manual ViewModelFactory
> - Retrofit + Coroutines
> - Error handling and loading state

---

## ✨ Features

- 🎬 **Browse movies** — Horizontal list with real posters from TMDb
- 🍔 **Browse food** — Vertical list with real food images from TheMealDB
- 🔍 **Search bar** — Visual UI element (ready for future search implementation)
- 📄 **Movie detail** — Real title, description, director, runtime, genre, and rating
- 🛒 **Food detail** — Name, derived price, category, instructions, and "Add to Order" button
- ⭐ **Ratings** — Based on real TMDb data (scaled 0–5)
- 🌙 **Dark theme** — Design inspired by platforms like Netflix
- ⏳ **Loading state** — Progress indicator while data is being fetched
- ⚠️ **Error handling** — Visible message when network issues occur

---

## 📸 Screenshots

> 💡 Add your app screenshots here.

| Home Screen | Movie Detail | Food Detail |
|:---:|:---:|:---:|
| _(screenshot here)_ | _(screenshot here)_ | _(screenshot here)_ |

---

## 🏛️ Architecture

This project implements **MVVM + Repository Pattern** (Stage 1) divided into three layers:

```
┌─────────────────────────────────────────────┐
│            PRESENTATION LAYER               │  ← Activities, ViewModels, Adapters
│           (What the user sees)              │    findViewById · ListAdapter · LiveData
├─────────────────────────────────────────────┤
│               DOMAIN LAYER                  │  ← Clean models + Repository contracts
│             (Business rules)                │    domain/model · domain/repository
├─────────────────────────────────────────────┤
│                DATA LAYER                   │  ← Implementations + API + DTOs
│          (Where data comes from)            │
│   remote/ → RetrofitClient, ApiServices     │
│   dto/    → MovieDto, MealDto               │
│   repository/ → RepositoryImpl (maps here) │
└─────────────────────────────────────────────┘
```

### Why this architecture?

- Each layer has **a single responsibility** (SOLID's S principle)
- Inner layers **do not know** about outer layers
- Code is easier to **understand and maintain**
- Swapping data sources **does not affect** the UI

### MVVM Pattern with Repository (Stage 1)

```
VIEW (Activity)  ──observes──▶  VIEWMODEL  ──suspend──▶  REPOSITORY
      │                          viewModelScope                 │
      │                          launch { }              Retrofit API
      └────────── events ──────────────┘
```

- **View** (Activity): Only displays data and captures user events. Uses `findByViewId`.
- **ViewModel**: Launches coroutines, exposes `isLoading` and `error` as LiveData. Depends directly on the Repository.
- **Repository interface** (domain): Defines the data contract — what the ViewModel can request.
- **RepositoryImpl** (data): Calls the API with `suspend fun`, maps DTOs to domain models inline.

---

## 📁 Project Structure

```
app/src/main/java/com/microsol/myappzegel/
│
├── 📂 data/
│   ├── 📂 remote/
│   │   ├── RetrofitClient.kt         ← Singleton with two Retrofit instances (TMDb + MealDB)
│   │   ├── TmdbApiService.kt         ← Retrofit interface for TMDb
│   │   ├── MealDbApiService.kt       ← Retrofit interface for TheMealDB
│   │   └── 📂 dto/
│   │       ├── MovieDto.kt           ← DTOs: MovieDto, MovieDetailDto, TvShowDto, GenreDto
│   │       └── MealDto.kt            ← DTO: MealDto, MealListResponse
│   └── 📂 repository/
│       ├── MovieRepositoryImpl.kt    ← Calls TMDb API · maps DTO → Movie inline
│       └── FoodRepositoryImpl.kt     ← Calls TheMealDB API · maps DTO → Food inline
│
├── 📂 domain/
│   ├── 📂 model/
│   │   ├── Movie.kt                  ← data class Movie (id: Int)
│   │   └── Food.kt                   ← data class Food with formattedPrice + availabilityLabel
│   └── 📂 repository/
│       ├── MovieRepository.kt        ← Repository contract interface (suspend fun)
│       └── FoodRepository.kt         ← Repository contract interface (suspend fun)
│
├── 📂 presentation/
│   ├── 📂 home/
│   │   ├── HomeViewModel.kt          ← viewModelScope.launch · isLoading · error
│   │   ├── HomeViewModelFactory.kt   ← Takes MovieRepository + FoodRepository
│   │   └── 📂 adapter/
│   │       ├── MovieAdapter.kt       ← ListAdapter + DiffUtil · findByViewId · Coil
│   │       └── FoodAdapter.kt        ← ListAdapter + DiffUtil · findByViewId · Coil
│   ├── 📂 moviedetail/
│   │   ├── MovieDetailActivity.kt    ← findByViewId · ViewModelProvider · Coil
│   │   ├── MovieDetailViewModel.kt   ← Takes MovieRepository directly
│   │   └── MovieDetailViewModelFactory.kt
│   └── 📂 fooddetail/
│       ├── FoodDetailActivity.kt     ← findByViewId · ViewModelProvider · Coil
│       ├── FoodDetailViewModel.kt    ← Takes FoodRepository directly
│       └── FoodDetailViewModelFactory.kt
│
└── MainActivity.kt   ← Home screen · findByViewId · ViewModelProvider
```

---

## 🧩 Kotlin Concepts Applied

| Concept | Where used | Brief description |
|---|---|---|
| `data class` | `Movie.kt`, `Food.kt`, DTOs | Class that only holds data; auto-generates `equals()`, `toString()`, `copy()` |
| `val` vs `var` | Throughout all files | `val` = immutable (read-only), `var` = mutable (can change) |
| Custom getter | `Food.formattedPrice`, `availabilityLabel` | Computed property re-evaluated every time it is accessed, no backing field |
| `interface` | `MovieRepository`, `FoodRepository` | Defines a contract that implementing classes must fulfill |
| Lambda `(T) -> Unit` | Adapters (onClick) | Anonymous function passed as a parameter to handle clicks |
| `suspend fun` | Repositories, ViewModels | Function that can pause without blocking the main thread |
| `viewModelScope.launch` | ViewModels | Launches a coroutine tied to the ViewModel lifecycle |
| `try/catch` | ViewModels | Error handling for network failures inside coroutines |
| `companion object` | Detail activities | Equivalent to Java's `static` fields |
| `@SerializedName` | DTOs | Maps JSON fields with underscore names to Kotlin properties |
| `object` singleton | `RetrofitClient` | One shared instance across the entire app |
| `by lazy` | `RetrofitClient` | Deferred initialization: created only when first needed |
| `DiffUtil.ItemCallback` | Adapters | Efficiently compares old and new lists to update only changed items |
| `ListAdapter` | `MovieAdapter`, `FoodAdapter` | Modern adapter that uses DiffUtil automatically when `submitList()` is called |

---

## 🚀 How to Run

### Prerequisites

- ✅ Android Studio **Meerkat** (2025.1) or higher
- ✅ JDK 21 or higher
- ✅ Physical device or emulator running **Android 7.0 (API 24)** or higher
- ✅ Internet connection (for the APIs)

### Credential Setup (required)

This project consumes the **TMDb API**, which requires an access token.

1. **Get your key at** [https://www.themoviedb.org/settings/api](https://www.themoviedb.org/settings/api) (free registration)

2. **Open `local.properties`** at the project root (Android Studio shows it in the Project view)

3. **Add your credentials** at the end of the file:
   ```properties
   TMDB_API_KEY=your_api_key_here
   TMDB_READ_ACCESS_TOKEN=your_read_access_token_here
   ```

> ⚠️ **Never** commit `local.properties` to Git. The file is already in `.gitignore` by default in Android projects.

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/cinefood-app.git
   ```

2. **Set up credentials** (see section above)

3. **Open the project in Android Studio**
   - Open Android Studio
   - Select `File → Open`
   - Navigate to the project folder and select it

4. **Sync Gradle**
   - Android Studio will sync automatically and download all dependencies
   - If not, click `Sync Now` in the yellow top bar

5. **Run the app**
   - Connect an Android device or start an emulator
   - Press the ▶️ `Run` button (Shift + F10)

---

## 🎓 Learning Purpose

This project (Stage 1) was created so students can:

- 🏗️ **Understand MVVM** with ViewModel, LiveData and Repository Pattern
- 📱 **Learn key components**: `RecyclerView`, `ViewModel`, `LiveData`, `findByViewId`
- 🌐 **Integrate REST APIs** using Retrofit, OkHttp, and Coroutines
- 🔄 **See how data flows** from the network all the way to the screen
- 🧱 **Apply SOLID principles** naturally within the codebase
- 🧩 **Understand Repository Pattern** as an abstraction over the data source

### What you will learn by reading this code

1. How to separate responsibilities between Activity, ViewModel, and Repository
2. Why the ViewModel survives screen rotation
3. How interfaces let you swap implementations without touching other code
4. The difference between a DTO and a domain model
5. How to pass data between screens using `Intent` extras
6. How to make async network calls with `suspend fun` and `viewModelScope`
7. How to load images from URLs with Coil
8. Why `ListAdapter` with `DiffUtil` is more efficient than `notifyDataSetChanged()`

---

## 🔮 Refactor Roadmap

| Stage | Status | Contents |
|---|---|---|
| **Stage 1** | ✅ **Current** | MVVM · Repository · Manual ViewModelFactory · `findByViewId` · `ListAdapter` |
| Stage 2 | ⏳ Pending | `sealed class UiState` · robust error handling · Use Cases · Mapper classes |
| Stage 3 | ⏳ Pending | Hilt · Navigation Component · testing with fake repositories |
| Stage 4 | ⏳ Pending | Jetpack Compose · Flow · Room |

### Additional suggested improvements

- [ ] 🔍 Implement functional search using the `/search/multi` endpoint
- [ ] 🗄️ Add local persistence with **Room Database**
- [ ] 🔑 Implement authentication with **Firebase Auth**
- [ ] 🧪 Add unit tests with **JUnit** and fake repositories
- [ ] 🛒 Build a functional shopping cart

---

## 📚 Resources for Students

- [Official Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Android Developers Guides](https://developer.android.com/guide)
- [Android Codelabs](https://developer.android.com/codelabs)
- [Android App Architecture](https://developer.android.com/topic/architecture)
- [Coroutines on Android](https://developer.android.com/kotlin/coroutines)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Coil Documentation](https://coil-kt.github.io/coil/)
- [TMDb API Docs](https://developer.themoviedb.org/docs)
- [TheMealDB API Docs](https://www.themealdb.com/api.php)

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Kotlin | 2.0+ | Programming language |
| Android Gradle Plugin | 9.x | Build system |
| AndroidX AppCompat | latest | Backward compatibility |
| Material Components | latest | UI design system |
| ConstraintLayout | latest | Flexible XML layouts |
| RecyclerView | latest | Scrollable lists |
| CardView | latest | Card UI component |
| Lifecycle ViewModel KTX | latest | MVVM support |
| Lifecycle LiveData KTX | latest | Observable data holder |
| **Retrofit** | **2.9.0** | **HTTP client + REST adapter** |
| **Gson Converter** | **2.9.0** | **JSON → Kotlin deserialization** |
| **OkHttp Logging** | **4.12.0** | **HTTP request/response logging** |
| **Kotlin Coroutines** | **1.7.3** | **Async/concurrent programming** |
| **Coil** | **2.5.0** | **Image loading from URLs** |

---

## 🔐 API Credentials

| API | Auth method | Where to register |
|---|---|---|
| TMDb | `Authorization: Bearer <token>` header | [themoviedb.org](https://www.themoviedb.org/settings/api) |
| TheMealDB | None (public) | No registration needed |

Credentials are stored in `local.properties` (excluded from version control) and exposed via `BuildConfig` at compile time. They are **never hardcoded** in source files.

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
