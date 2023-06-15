plugins {
    id("proxytoggle.library")
    id("proxytoggle.library.compose")
    id("proxytoggle.library.jacoco")
}

android {
    namespace = "com.kinandcarta.create.proxytoggle.core.ui"
}

dependencies {
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.accompanist.systemuicontroller)
}
