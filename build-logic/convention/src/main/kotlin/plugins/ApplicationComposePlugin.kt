package plugins

import com.android.build.api.dsl.ApplicationExtension
import common.configureCommonExtensionCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class ApplicationComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")

            val extension = extensions.getByType<ApplicationExtension>()
            configureCommonExtensionCompose(extension)
        }
    }
}
