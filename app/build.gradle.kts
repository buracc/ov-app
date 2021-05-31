plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(AndroidConfig.compileSdk)
    buildToolsVersion(AndroidConfig.buildToolsVersion)

    defaultConfig {
        applicationId = "dev.burak.ovapp"
        minSdkVersion(AndroidConfig.minSdk)
        targetSdkVersion(AndroidConfig.targetSdk)
        versionCode = AndroidConfig.versionCode
        versionName = AndroidConfig.versionName
        testInstrumentationRunner = AndroidConfig.androidTestInstrumentation
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    androidExtensions {
        isExperimental = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }
}

dependencies {
    // Core libraries
    implementation(Dependencies.appLibraries)
    testImplementation(Dependencies.testLibraries)
    androidTestImplementation(Dependencies.androidTestLibraries)

    // Material
    implementation(group = "com.google.android.material", name = "material", version = "1.3.0")

    // Room
    implementation(group = "androidx.room", name = "room-runtime", version = "2.3.0")
    kapt(group = "androidx.room", name = "room-compiler", version = "2.3.0")
    implementation(group = "androidx.room", name = "room-ktx", version = "2.3.0")

    // ViewModel and LiveData
    implementation(group = "androidx.lifecycle", name = "lifecycle-extensions", version = "2.2.0")
    implementation(group = "androidx.lifecycle", name = "lifecycle-viewmodel-ktx", version = "2.3.1")

    //noinspection GradleCompatible
    implementation(group = "com.android.support", name = "cardview-v7", version = "28.0.0")

    // Http
    implementation(group = "com.squareup.retrofit2", name = "retrofit", version = "2.9.0")
    implementation(group = "com.squareup.retrofit2", name = "converter-gson", version = "2.9.0")

    // Etc
    implementation(group = "com.ethlo.time", name = "itu", version = "1.3.0")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.8.6")

    // DI
    implementation(group = "com.google.dagger", name = "hilt-android", version = "2.35.1")
    kapt(group = "com.google.dagger", name = "hilt-compiler", version = "2.35.1")


    // Test
    androidTestImplementation(group = "com.google.dagger", name = "hilt-android-testing", version = "2.35.1")
    kaptAndroidTest(group = "com.google.dagger", name = "hilt-compiler", version = "2.35.1")

    testImplementation(group = "com.google.dagger", name = "hilt-android-testing", version = "2.35.1")
    kaptTest(group = "com.google.dagger", name = "hilt-compiler", version = "2.35.1")

    androidTestImplementation(group = "com.google.truth", name = "truth", version = "1.1.3")
    testImplementation(group = "com.google.truth", name = "truth", version = "1.1.3")
}
