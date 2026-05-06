import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    // NOTE: AGP 9.x bundles Kotlin support internally — do NOT apply
    // org.jetbrains.kotlin.android separately or it will conflict.
}

val localProps = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.microsol.myappzegel"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.microsol.myappzegel"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "TMDB_API_KEY", "\"${localProps["TMDB_API_KEY"]}\"")
        buildConfigField("String", "TMDB_READ_ACCESS_TOKEN", "\"${localProps["TMDB_READ_ACCESS_TOKEN"]}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        // AGP 9.x with bundled Kotlin infers the Kotlin JVM target
        // from these Java compatibility settings — no kotlinOptions needed.
    }

    // ViewBinding generates a binding class for each XML layout,
    // giving us type-safe access to views without findViewById().
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)

    // ViewModel + LiveData — core of MVVM
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // RecyclerView + CardView for lists
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)

    // Fragment support
    implementation(libs.androidx.fragment.ktx)

    // Retrofit + Gson converter
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp logging (debug only)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Coil — image loading from URLs
    implementation("io.coil-kt:coil:2.5.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
