import kotlin.String

/**
 * Generated by https://github.com/jmfayard/buildSrcVersions
 *
 * Update this file with
 *   `$ ./gradlew buildSrcVersions`
 */
object Libs {
    /**
     * https://detekt.github.io/detekt
     */
    const val detekt_formatting: String = "io.gitlab.arturbosch.detekt:detekt-formatting:" +
            Versions.io_gitlab_arturbosch_detekt

    /**
     * https://detekt.github.io/detekt
     */
    const val detekt_gradle_plugin: String = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:" +
            Versions.io_gitlab_arturbosch_detekt

    /**
     * https://kotlinlang.org/
     */
    const val kotlin_android_extensions: String =
            "org.jetbrains.kotlin:kotlin-android-extensions:" + Versions.org_jetbrains_kotlin

    /**
     * https://kotlinlang.org/
     */
    const val kotlin_android_extensions_runtime: String =
            "org.jetbrains.kotlin:kotlin-android-extensions-runtime:" +
            Versions.org_jetbrains_kotlin

    /**
     * https://kotlinlang.org/
     */
    const val kotlin_annotation_processing_gradle: String =
            "org.jetbrains.kotlin:kotlin-annotation-processing-gradle:" +
            Versions.org_jetbrains_kotlin

    /**
     * https://kotlinlang.org/
     */
    const val kotlin_gradle_plugin: String = "org.jetbrains.kotlin:kotlin-gradle-plugin:" +
            Versions.org_jetbrains_kotlin

    /**
     * https://kotlinlang.org/
     */
    const val kotlin_stdlib: String = "org.jetbrains.kotlin:kotlin-stdlib:" +
            Versions.org_jetbrains_kotlin

    /**
     * https://developer.android.com/jetpack/androidx
     */
    const val lifecycle_livedata_ktx: String = "androidx.lifecycle:lifecycle-livedata-ktx:" +
            Versions.androidx_lifecycle

    /**
     * https://developer.android.com/jetpack/androidx
     */
    const val lifecycle_viewmodel_ktx: String = "androidx.lifecycle:lifecycle-viewmodel-ktx:" +
            Versions.androidx_lifecycle

    const val hilt_android: String = "com.google.dagger:hilt-android:" + Versions.com_google_dagger

    const val hilt_android_compiler: String = "com.google.dagger:hilt-android-compiler:" +
            Versions.com_google_dagger

    const val hilt_android_gradle_plugin: String = "com.google.dagger:hilt-android-gradle-plugin:" +
            Versions.com_google_dagger

    const val hilt_compiler: String = "androidx.hilt:hilt-compiler:" + Versions.androidx_hilt

    const val hilt_lifecycle_viewmodel: String = "androidx.hilt:hilt-lifecycle-viewmodel:" +
            Versions.androidx_hilt

    /**
     * https://developer.android.com/studio
     */
    const val com_android_tools_build_gradle: String = "com.android.tools.build:gradle:" +
            Versions.com_android_tools_build_gradle

    /**
     * https://developer.android.com/testing
     */
    const val androidx_test_ext_junit: String = "androidx.test.ext:junit:" +
            Versions.androidx_test_ext_junit

    /**
     * http://junit.org
     */
    const val junit_junit: String = "junit:junit:" + Versions.junit_junit

    const val de_fayard_buildsrcversions_gradle_plugin: String =
            "de.fayard.buildSrcVersions:de.fayard.buildSrcVersions.gradle.plugin:" +
            Versions.de_fayard_buildsrcversions_gradle_plugin

    /**
     * http://tools.android.com
     */
    const val constraintlayout: String = "androidx.constraintlayout:constraintlayout:" +
            Versions.constraintlayout

    /**
     * https://developer.android.com/testing
     */
    const val espresso_core: String = "androidx.test.espresso:espresso-core:" +
            Versions.espresso_core

    /**
     * https://developer.android.com/jetpack/androidx
     */
    const val fragment_ktx: String = "androidx.fragment:fragment-ktx:" + Versions.fragment_ktx

    /**
     * https://developer.android.com/studio
     */
    const val lint_gradle: String = "com.android.tools.lint:lint-gradle:" + Versions.lint_gradle

    const val viewbinding: String = "androidx.databinding:viewbinding:" + Versions.viewbinding

    /**
     * https://developer.android.com/jetpack/androidx
     */
    const val appcompat: String = "androidx.appcompat:appcompat:" + Versions.appcompat

    /**
     * https://developer.android.com/jetpack/androidx
     */
    const val core_ktx: String = "androidx.core:core-ktx:" + Versions.core_ktx

    /**
     * http://developer.android.com/tools/extras/support-library.html
     */
    const val material: String = "com.google.android.material:material:" + Versions.material

    /**
     * https://developer.android.com/studio
     */
    const val aapt2: String = "com.android.tools.build:aapt2:" + Versions.aapt2
}
