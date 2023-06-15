package common

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal fun Project.configureCommonExtensionCompose(
    commonExtension: CommonExtension<*, *, *, *>,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

    commonExtension.apply {
        buildFeatures.compose = true

        composeOptions.kotlinCompilerExtensionVersion =
            libs.findVersion("compose.compiler").get().toString()
    }

    dependencies {
        add("implementation", libs.findLibrary("compose.ui").get())
        add("implementation", libs.findLibrary("compose.ui.tooling.preview").get())
        add("debugImplementation", libs.findLibrary("compose.ui.tooling").get())
    }
}
