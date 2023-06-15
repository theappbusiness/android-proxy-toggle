plugins {
    id("proxytoggle.library")
    id("proxytoggle.library.compose")
    id("proxytoggle.library.jacoco")
    id("com.google.protobuf")
    id("proxytoggle.test")
    id("proxytoggle.hilt")
}

android {
    namespace = "com.kinandcarta.create.proxytoggle.repository"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.appcompat)
    implementation(libs.protobuf.javalite)

    implementation(libs.datastore)
    implementation(libs.datastore.preferences)

    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.test.ext.junit)
    testImplementation(libs.robolectric)
}

protobuf {
    protoc {
        artifact = libs.protoc.get().toString()
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
