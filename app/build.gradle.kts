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
}

dependencies {
    // Core libraries
    implementation(Dependencies.appLibraries)
    testImplementation(Dependencies.testLibraries)
    androidTestImplementation(Dependencies.androidTestLibraries)

    // Material
    implementation("com.google.android.material:material:1.3.0")

    // Room
    implementation("androidx.room:room-runtime:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")
    implementation("androidx.room:room-ktx:2.3.0")

    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

    //noinspection GradleCompatible
    implementation("com.android.support:cardview-v7:28.0.0")


    // Http
    implementation(group = "com.squareup.retrofit2", name = "retrofit", version = "2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Etc
    implementation(group = "com.ethlo.time", name = "itu", version = "1.3.0")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.8.6")

    // DI
    implementation("com.google.dagger:hilt-android:2.35.1")
    kapt("com.google.dagger:hilt-compiler:2.35.1")

    androidTestImplementation("com.google.dagger:hilt-android-testing:2.35.1")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.35.1")

    testImplementation("com.google.dagger:hilt-android-testing:2.35.1")
    kaptTest("com.google.dagger:hilt-compiler:2.35.1")
}
