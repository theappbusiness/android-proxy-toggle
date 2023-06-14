plugins {
    id("proxytoggle.application")
    id("proxytoggle.application.compose")
    id("proxytoggle.application.jacoco")
    id("proxytoggle.test")
    id("proxytoggle.hilt")
}

android {
    namespace = "com.kinandcarta.create.proxytoggle"

    defaultConfig {
        applicationId = "com.kinandcarta.create.proxytoggle"

        versionName = "1.2.0"
        versionCode = common.versionCode(versionName)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                rootProject.file("proguard-rules.pro")
            )
        }
    }
}

dependencies {
    implementation(project(":feature:manager"))
    implementation(project(":feature:tile"))
    implementation(project(":feature:widget"))
    implementation(project(":repository"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.com.google.android.material.material)

    implementation(libs.compose.ui)
    implementation(libs.material3.window.size.clazz)
    implementation(libs.activity.compose)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}
