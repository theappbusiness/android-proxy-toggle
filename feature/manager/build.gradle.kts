plugins {
    id("proxytoggle.library")
    id("proxytoggle.library.compose")
    id("proxytoggle.library.jacoco")
    id("app.cash.paparazzi")
    id("com.google.devtools.ksp")
    id("proxytoggle.test")
    id("proxytoggle.hilt")
}

android {
    namespace = "com.kinandcarta.create.proxytoggle.manager"
}

hilt {
    // gradlew tDUT gets stuck at hiltAggregateDepsDebugUnitTest if not for this...
    enableAggregatingTask = false
}

dependencies {
    implementation(project(":repository"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))

    implementation(libs.compose.foundation)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.material3)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.constraintlayout.compose)

    implementation(libs.showkase)
    ksp(libs.showkase.processor)

    // FIX: These 2 are added otherwise we get ClassNotFoundExceptions only for our Compose previews
    // eg see here for the 2nd: https://stackoverflow.com/a/72807850/4158051. Remove when/if fixed
    debugImplementation(libs.lifecycle.viewmodel.savedstate)
    debugImplementation(libs.customview.poolingcontainer)
    debugImplementation(libs.compose.ui.test.manifest)

    testImplementation(libs.core.testing)
    testImplementation(libs.compose.ui.test)
    testImplementation(libs.compose.ui.test.junit4)
    testImplementation(libs.test.ext.junit)
    testImplementation(libs.showkase.processor)
    testImplementation(libs.test.parameter.injector)
    testImplementation(libs.hamcrest.core)
    testImplementation(libs.robolectric)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    kspTest(libs.showkase.processor)
}
