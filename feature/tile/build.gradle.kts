plugins {
    id("proxytoggle.library")
    id("proxytoggle.library.jacoco")
    id("proxytoggle.test")
    id("proxytoggle.hilt")
}

android {
    namespace = "com.kinandcarta.create.proxytoggle.tile"
}

dependencies {
    implementation(project(":repository"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    testImplementation(project(":test-utils"))

    testImplementation(libs.test.ext.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.kotlinx.coroutines.test)
}
