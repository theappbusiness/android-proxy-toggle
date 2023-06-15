package common

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

internal fun Project.configureCommonExtension(
    commonExtension: CommonExtension<*, *, *, *>
) {
    commonExtension.apply {
        compileSdk = Versions.COMPILE_SDK

        defaultConfig.minSdk = Versions.MIN_SDK

        buildFeatures {
            aidl = false
            buildConfig = false
            renderScript = false
            shaders = false
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = Versions.JAVA_VERSION
            targetCompatibility = Versions.JAVA_VERSION
        }

        kotlinOptions {
            jvmTarget = Versions.JAVA_VERSION.toString()
            freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
        }

        kotlinExtension.jvmToolchain(Versions.JAVA_VERSION.majorVersion.toInt())

        testOptions.unitTests.isIncludeAndroidResources = true
    }

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("desugar.jdk.libs").get())
    }
}

fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}
