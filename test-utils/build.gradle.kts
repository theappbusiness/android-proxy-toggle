plugins {
    id("proxytoggle.library")
    id("proxytoggle.library.jacoco")
}

android {
    namespace = "com.kinandcarta.create.proxytoggle.testutils"
}

dependencies {
    implementation(libs.mockk)
    implementation(libs.robolectric)
}
