plugins {
    alias(libs.plugins.android.application)
    // NOTE: AGP 9.x bundles Kotlin support internally — do NOT apply
    // org.jetbrains.kotlin.android separately or it will conflict.
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

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
