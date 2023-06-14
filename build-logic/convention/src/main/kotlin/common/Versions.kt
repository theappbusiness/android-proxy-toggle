package common

import org.gradle.api.JavaVersion

internal object Versions {
    const val COMPILE_SDK = 33
    const val MIN_SDK = 21
    const val TARGET_SDK = 33
    val JAVA_VERSION = JavaVersion.VERSION_11
}