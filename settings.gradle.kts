@file:Suppress("UnstableApiUsage")

rootProject.name = "risiko"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        flatDir {
            dirs("libs")
        }
    }
}

include(":client")
include(":server")
include(":commons")
