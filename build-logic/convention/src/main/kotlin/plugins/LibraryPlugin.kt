package plugins

import com.android.build.gradle.LibraryExtension
import common.Versions
import common.configureCommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class LibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("proxytoggle.detekt")
            }

            extensions.configure<LibraryExtension> {
                configureCommonExtension(this)
                defaultConfig.targetSdk = Versions.TARGET_SDK
            }
        }
    }
}
