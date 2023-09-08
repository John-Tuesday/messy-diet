pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

includeBuild("platforms")

rootProject.name = "messy-diet"
include(":app-platform")
include(":core:test:common")
include(":core:android:hilt")
include(":core:measure")
include(":core:diet-model")
include(":core:remote:food-data-central")
include(":app")
include(":feature:bmi:presentation")
include(":feature:bmi:data")
include(":feature:measure")
include(":feature:meal:presentation")
include(":feature:meal:data")
include(":feature:search:presentation")
include(":feature:search:data")
