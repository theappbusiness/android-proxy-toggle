package plugins

import com.android.build.gradle.internal.tasks.factory.dependsOn
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class DetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.getByType<DetektExtension>().apply {
                toolVersion = libs.findVersion("detekt").get().toString()
                config = files("$rootDir/detekt.yml")
                buildUponDefaultConfig = true
            }

            dependencies {
                add("detektPlugins", libs.findLibrary("detekt.formatting").get())
            }

            tasks.named("preBuild").dependsOn("detekt")
        }
    }
}
