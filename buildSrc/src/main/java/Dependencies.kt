import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
    // Kotlin
    val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"

    //android ui
    private val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    private val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    private val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    //test libs
    private val junit = "junit:junit:${Versions.junit}"
    private val testRunner = "androidx.test:runner:${Versions.testRunner}"
    private val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"
    private val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    private val archCore = "androidx.arch.core:core-testing:${Versions.archCore}"

    val appLibraries = arrayListOf<String>().apply {
        add(kotlinStdLib)
        add(coreKtx)
        add(appcompat)
        add(constraintLayout)
    }

    val androidTestLibraries = arrayListOf<String>().apply {
        add(testRunner)
        add(extJUnit)
        add(espressoCore)
        add(junit)
        add(kotlinCoroutines)
        add(archCore)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(junit)
        add(kotlinCoroutines)
        add(archCore)
    }
}

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}
