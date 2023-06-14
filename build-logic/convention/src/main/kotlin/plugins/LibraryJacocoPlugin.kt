package plugins

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import common.configureJacoco
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class LibraryJacocoPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.gradle.jacoco")
                apply("com.android.library")
            }
            val extension = extensions.getByType<LibraryAndroidComponentsExtension>()
            configureJacoco(extension)
        }
    }
}
