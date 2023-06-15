pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.51.0"
}

rootProject.name = "ProxyToggle"

include(":app")
include(":feature:manager")
include(":feature:tile")
include(":feature:widget")
include(":repository")
include(":core:ui")
include(":core:common")
include(":test-utils")
