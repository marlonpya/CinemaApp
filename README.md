# 🎬🍔 CineFood App

> **Proyecto de aprendizaje Android · Android Learning Project**

---

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-blue?style=for-the-badge)
![Clean Architecture](https://img.shields.io/badge/Clean-Architecture-orange?style=for-the-badge)
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
- Comprender la arquitectura **Clean Architecture + MVVM**
- Practicar con **Coroutines**, **Retrofit**, **LiveData** y **RecyclerView**
- Ver cómo integrar APIs REST reales de forma limpia y segura

---

## ✨ Funcionalidades

- 🎬 **Explorar películas y series** — Lista horizontal con pósters reales desde TMDb
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

Este proyecto implementa **Clean Architecture** dividida en tres capas:

```
┌─────────────────────────────────────────────┐
│            PRESENTATION LAYER               │  ← Activities, ViewModels, Adapters
│        (Lo que el usuario ve y toca)        │
├─────────────────────────────────────────────┤
│               DOMAIN LAYER                  │  ← Casos de uso (Use Cases)
│          (Las reglas del negocio)           │
├─────────────────────────────────────────────┤
│                DATA LAYER                   │  ← Modelos, Repositorios, APIs, DTOs
│         (De dónde vienen los datos)         │
│   remote/ → RetrofitClient, ApiServices     │
│   dto/    → MovieDto, MealDto               │
│   mapper/ → toDomain() extension functions  │
└─────────────────────────────────────────────┘
```

### ¿Por qué Clean Architecture?

- Cada capa tiene **una sola responsabilidad** (Principio S de SOLID)
- Las capas internas **no conocen** a las capas externas
- El código es más **fácil de probar** y mantener
- Cambiar la fuente de datos (ej. de una API a otra) **no afecta** la UI

### Patrón MVVM con Coroutines

```
VISTA (Activity)  ──observa──▶  VIEWMODEL  ──suspend──▶  USE CASE  ──suspend──▶  REPOSITORY
       │                          viewModelScope                                      │
       │                          launch { }                                    Retrofit API
       └──────── eventos ──────────────┘
```

- **View** (Activity): Solo muestra datos y captura eventos del usuario
- **ViewModel**: Lanza coroutines, mantiene `isLoading` y `error` como LiveData
- **Use Case**: `suspend operator fun invoke()` — delega al repositorio
- **Repository**: Llama a la API con `suspend fun`, mapea DTOs al dominio

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/microsol/myappzegel/
│
├── 📂 data/
│   ├── 📂 model/
│   │   ├── Movie.kt              ← Data class de película + value class MovieId
│   │   └── Food.kt               ← Data class de comida + getter personalizado
│   ├── 📂 remote/
│   │   ├── RetrofitClient.kt     ← Singleton con dos instancias Retrofit (TMDb + MealDB)
│   │   ├── TmdbApiService.kt     ← Interfaz Retrofit para TMDb
│   │   ├── MealDbApiService.kt   ← Interfaz Retrofit para TheMealDB
│   │   └── 📂 dto/
│   │       ├── MovieDto.kt       ← DTOs: MovieDto, TvShowDto, GenreDto, CreditsResponse
│   │       └── MealDto.kt        ← DTO: MealDto, MealListResponse
│   ├── 📂 mapper/
│   │   ├── MovieMapper.kt        ← MovieDto/TvShowDto.toDomain()
│   │   └── MealMapper.kt         ← MealDto.toDomain()
│   └── 📂 repository/
│       ├── MovieRepository.kt        ← Interfaz del repositorio (suspend fun)
│       ├── FoodRepository.kt         ← Interfaz del repositorio (suspend fun)
│       ├── MovieRepositoryImpl.kt    ← Llama a TMDb API con coroutines
│       └── FoodRepositoryImpl.kt     ← Llama a TheMealDB API con coroutines
│
├── 📂 domain/
│   └── 📂 usecase/
│       ├── GetMoviesUseCase.kt   ← suspend operator fun invoke()
│       └── GetFoodsUseCase.kt    ← suspend operator fun invoke()
│
├── 📂 presentation/
│   ├── 📂 home/
│   │   ├── HomeViewModel.kt          ← viewModelScope.launch, isLoading, error
│   │   ├── HomeViewModelFactory.kt
│   │   └── 📂 adapter/
│   │       ├── MovieAdapter.kt   ← Carga pósters con Coil
│   │       └── FoodAdapter.kt    ← Carga imágenes con Coil
│   ├── 📂 moviedetail/
│   │   ├── MovieDetailActivity.kt    ← Carga imagen con Coil, observa error/loading
│   │   ├── MovieDetailViewModel.kt
│   │   └── MovieDetailViewModelFactory.kt
│   └── 📂 fooddetail/
│       ├── FoodDetailActivity.kt     ← Carga imagen con Coil, observa error/loading
│       ├── FoodDetailViewModel.kt
│       └── FoodDetailViewModelFactory.kt
│
└── MainActivity.kt   ← Pantalla principal (Home)
```

---

## 🧩 Conceptos Kotlin Aplicados

| Concepto | Dónde se usa | Descripción breve |
|---|---|---|
| `data class` | `Movie.kt`, `Food.kt`, DTOs | Clase que solo guarda datos; genera `equals()`, `toString()`, `copy()` automáticamente |
| `value class` | `MovieId`, `FoodId` | Envuelve un primitivo sin costo extra en memoria (type-safety) |
| `val` vs `var` | En todos los archivos | `val` = inmutable (no cambia), `var` = mutable (puede cambiar) |
| Custom getter | `Food.formattedPrice` | Propiedad calculada que se computa cada vez que se accede |
| `interface` | `MovieRepository`, `FoodRepository` | Define un contrato que las clases deben cumplir |
| Lambda `(T) -> Unit` | Adapters (onClick) | Función anónima pasada como parámetro para manejar clics |
| Destructuring | Dentro de adapters | Descompone un `data class` en variables: `val (id, title) = movie` |
| `suspend fun` | Repositorios, Use Cases | Función que puede pausarse sin bloquear el hilo principal |
| `operator fun invoke()` | Use cases | Permite llamar un objeto como función: `getMoviesUseCase()` |
| `viewModelScope.launch` | ViewModels | Lanza una coroutine ligada al ciclo de vida del ViewModel |
| `coroutineScope` + `async` | RepositoryImpl | Ejecuta llamadas a la API en paralelo y espera todas con `await()` |
| `companion object` | Activities de detalle | Equivale a los campos `static` de Java |
| `@SerializedName` | DTOs | Mapea campos JSON con nombres distintos a propiedades Kotlin |
| Extension function | Mappers (`toDomain()`) | Añade métodos a clases existentes sin modificarlas |
| `object` singleton | `RetrofitClient` | Una sola instancia compartida en toda la app |
| `by lazy` | `RetrofitClient` | Inicialización diferida: se crea solo cuando se necesita por primera vez |

---

## 🚀 Cómo Ejecutar el Proyecto

### Requisitos previos

- ✅ Android Studio **Meerkat** (2025.1) o superior
- ✅ JDK 17 o superior
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

Este proyecto fue creado para que los estudiantes puedan:

- 🏗️ **Entender la arquitectura** de una app Android real con Clean Architecture + MVVM
- 🔤 **Practicar Kotlin** con ejemplos concretos de cada concepto del lenguaje
- 📱 **Conocer los componentes** principales: `RecyclerView`, `ViewModel`, `LiveData`, `ViewBinding`
- 🌐 **Integrar APIs REST** usando Retrofit, OkHttp y Coroutines
- 🔄 **Ver cómo fluye la información** desde la red hasta la pantalla
- 🧱 **Aplicar principios SOLID** de forma natural en el código

### Lo que aprenderás leyendo este código

1. Cómo separar responsabilidades en capas
2. Por qué el ViewModel sobrevive a la rotación de pantalla
3. Cómo las interfaces permiten cambiar implementaciones sin tocar el resto del código
4. La diferencia entre una `data class` y una clase normal
5. Cómo pasar datos entre pantallas con `Intent` extras
6. Cómo hacer llamadas de red asíncronas con `suspend fun` y `viewModelScope`
7. Cómo mapear respuestas JSON (DTOs) a modelos de dominio limpios
8. Cómo cargar imágenes desde URLs con Coil

---

## 🔮 Mejoras Futuras

Estas son ideas para extender el proyecto una vez que domines los conceptos básicos:

- [x] 🌐 Conectar a una API real — **¡Completado! TMDb + TheMealDB integrados**
- [ ] 🔍 Implementar la barra de búsqueda funcional con el endpoint `/search/multi`
- [ ] 🗄️ Agregar persistencia local con **Room Database**
- [ ] 🔑 Implementar autenticación con **Firebase Auth**
- [ ] 🧭 Migrar la navegación a **Jetpack Navigation Component**
- [ ] 💉 Implementar inyección de dependencias con **Hilt**
- [ ] 🧪 Agregar pruebas unitarias con **JUnit** y **MockK**
- [ ] 🎨 Migrar la UI a **Jetpack Compose**
- [ ] 🛒 Implementar un carrito de pedidos funcional con **StateFlow**
- [ ] 📄 Agregar paginación con **Paging 3**

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
- Understand **Clean Architecture + MVVM**
- Practice with **Coroutines**, **Retrofit**, **LiveData**, and **RecyclerView**
- See how to integrate real REST APIs cleanly and securely

---

## ✨ Features

- 🎬 **Browse movies & TV shows** — Horizontal list with real posters from TMDb
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

This project implements **Clean Architecture** divided into three layers:

```
┌─────────────────────────────────────────────┐
│            PRESENTATION LAYER               │  ← Activities, ViewModels, Adapters
│           (What the user sees)              │
├─────────────────────────────────────────────┤
│               DOMAIN LAYER                  │  ← Use Cases
│             (Business rules)                │
├─────────────────────────────────────────────┤
│                DATA LAYER                   │  ← Models, Repositories, APIs, DTOs
│          (Where data comes from)            │
│   remote/ → RetrofitClient, ApiServices     │
│   dto/    → MovieDto, MealDto               │
│   mapper/ → toDomain() extension functions  │
└─────────────────────────────────────────────┘
```

### Why Clean Architecture?

- Each layer has **a single responsibility** (SOLID's S principle)
- Inner layers **do not know** about outer layers
- Code is easier to **test and maintain**
- Swapping data sources **does not affect** the UI

### MVVM Pattern with Coroutines

```
VIEW (Activity)  ──observes──▶  VIEWMODEL  ──suspend──▶  USE CASE  ──suspend──▶  REPOSITORY
      │                          viewModelScope                                       │
      │                          launch { }                                     Retrofit API
      └────────── events ──────────────┘
```

- **View** (Activity): Only displays data and captures user events
- **ViewModel**: Launches coroutines, exposes `isLoading` and `error` as LiveData
- **Use Case**: `suspend operator fun invoke()` — delegates to the repository
- **Repository**: Calls the API with `suspend fun`, maps DTOs to domain models

---

## 📁 Project Structure

```
app/src/main/java/com/microsol/myappzegel/
│
├── 📂 data/
│   ├── 📂 model/
│   │   ├── Movie.kt              ← Movie data class + MovieId value class
│   │   └── Food.kt               ← Food data class + custom getter
│   ├── 📂 remote/
│   │   ├── RetrofitClient.kt     ← Singleton with two Retrofit instances (TMDb + MealDB)
│   │   ├── TmdbApiService.kt     ← Retrofit interface for TMDb
│   │   ├── MealDbApiService.kt   ← Retrofit interface for TheMealDB
│   │   └── 📂 dto/
│   │       ├── MovieDto.kt       ← DTOs: MovieDto, TvShowDto, GenreDto, CreditsResponse
│   │       └── MealDto.kt        ← DTO: MealDto, MealListResponse
│   ├── 📂 mapper/
│   │   ├── MovieMapper.kt        ← MovieDto/TvShowDto.toDomain()
│   │   └── MealMapper.kt         ← MealDto.toDomain()
│   └── 📂 repository/
│       ├── MovieRepository.kt        ← Repository interface (suspend fun)
│       ├── FoodRepository.kt         ← Repository interface (suspend fun)
│       ├── MovieRepositoryImpl.kt    ← Calls TMDb API with coroutines
│       └── FoodRepositoryImpl.kt     ← Calls TheMealDB API with coroutines
│
├── 📂 domain/
│   └── 📂 usecase/
│       ├── GetMoviesUseCase.kt   ← suspend operator fun invoke()
│       └── GetFoodsUseCase.kt    ← suspend operator fun invoke()
│
├── 📂 presentation/
│   ├── 📂 home/
│   │   ├── HomeViewModel.kt          ← viewModelScope.launch, isLoading, error
│   │   ├── HomeViewModelFactory.kt
│   │   └── 📂 adapter/
│   │       ├── MovieAdapter.kt   ← Loads posters with Coil
│   │       └── FoodAdapter.kt    ← Loads food images with Coil
│   ├── 📂 moviedetail/
│   │   ├── MovieDetailActivity.kt    ← Loads image with Coil, observes error/loading
│   │   ├── MovieDetailViewModel.kt
│   │   └── MovieDetailViewModelFactory.kt
│   └── 📂 fooddetail/
│       ├── FoodDetailActivity.kt     ← Loads image with Coil, observes error/loading
│       ├── FoodDetailViewModel.kt
│       └── FoodDetailViewModelFactory.kt
│
└── MainActivity.kt   ← Home screen entry point
```

---

## 🧩 Kotlin Concepts Applied

| Concept | Where used | Brief description |
|---|---|---|
| `data class` | `Movie.kt`, `Food.kt`, DTOs | Class that only holds data; auto-generates `equals()`, `toString()`, `copy()` |
| `value class` | `MovieId`, `FoodId` | Wraps a primitive without memory overhead (type-safety) |
| `val` vs `var` | Throughout all files | `val` = immutable (read-only), `var` = mutable (can change) |
| Custom getter | `Food.formattedPrice` | Computed property re-evaluated every time it is accessed |
| `interface` | `MovieRepository`, `FoodRepository` | Defines a contract that implementing classes must fulfill |
| Lambda `(T) -> Unit` | Adapters (onClick) | Anonymous function passed as a parameter to handle clicks |
| Destructuring | Inside adapters | Unpacks a `data class` into variables: `val (id, title) = movie` |
| `suspend fun` | Repositories, Use Cases | Function that can pause without blocking the main thread |
| `operator fun invoke()` | Use cases | Allows calling an object like a function: `getMoviesUseCase()` |
| `viewModelScope.launch` | ViewModels | Launches a coroutine tied to the ViewModel lifecycle |
| `coroutineScope` + `async` | RepositoryImpl | Runs API calls in parallel and awaits all results |
| `companion object` | Detail activities | Equivalent to Java's `static` fields |
| `@SerializedName` | DTOs | Maps JSON fields with underscore names to Kotlin properties |
| Extension function | Mappers (`toDomain()`) | Adds methods to existing classes without modifying them |
| `object` singleton | `RetrofitClient` | One shared instance across the entire app |
| `by lazy` | `RetrofitClient` | Deferred initialization: created only when first needed |

---

## 🚀 How to Run

### Prerequisites

- ✅ Android Studio **Meerkat** (2025.1) or higher
- ✅ JDK 17 or higher
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

This project was created so students can:

- 🏗️ **Understand architecture** of a real Android app using Clean Architecture + MVVM
- 🔤 **Practice Kotlin** with concrete examples of each language concept
- 📱 **Learn key components**: `RecyclerView`, `ViewModel`, `LiveData`, `ViewBinding`
- 🌐 **Integrate REST APIs** using Retrofit, OkHttp, and Coroutines
- 🔄 **See how data flows** from the network all the way to the screen
- 🧱 **Apply SOLID principles** naturally within the codebase

### What you will learn by reading this code

1. How to separate responsibilities into layers
2. Why the ViewModel survives screen rotation
3. How interfaces let you swap implementations without touching other code
4. The difference between a `data class` and a regular class
5. How to pass data between screens using `Intent` extras
6. How to make async network calls with `suspend fun` and `viewModelScope`
7. How to map JSON responses (DTOs) to clean domain models
8. How to load images from URLs with Coil

---

## 🔮 Future Improvements

Ideas to extend the project once you have mastered the basics:

- [x] 🌐 Connect to a real API — **Done! TMDb + TheMealDB integrated**
- [ ] 🔍 Implement functional search using the `/search/multi` endpoint
- [ ] 🗄️ Add local persistence with **Room Database**
- [ ] 🔑 Implement authentication with **Firebase Auth**
- [ ] 🧭 Migrate navigation to **Jetpack Navigation Component**
- [ ] 💉 Implement dependency injection with **Hilt**
- [ ] 🧪 Add unit tests with **JUnit** and **MockK**
- [ ] 🎨 Migrate the UI to **Jetpack Compose**
- [ ] 🛒 Build a functional shopping cart with **StateFlow**
- [ ] 📄 Add pagination with **Paging 3**

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
