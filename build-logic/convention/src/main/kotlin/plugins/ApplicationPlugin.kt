package plugins

import com.android.build.api.dsl.ApplicationExtension
import common.Versions
import common.configureCommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("proxytoggle.detekt")
            }

            extensions.configure<ApplicationExtension> {
                configureCommonExtension(this)
                defaultConfig {
                    targetSdk = Versions.TARGET_SDK
                }

                // just for the app
                buildFeatures.buildConfig = true

                packagingOptions {
                    resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
        }
    }
}
