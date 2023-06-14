plugins {
    id("proxytoggle.library")
    id("proxytoggle.library.compose")
    id("proxytoggle.library.jacoco")
    id("proxytoggle.test")
    id("proxytoggle.hilt")
}

android {
    namespace = "com.kinandcarta.create.proxytoggle.core.common"
}

dependencies {
    testImplementation(libs.test.ext.junit)
    testImplementation(libs.robolectric)
}
