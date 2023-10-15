pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "messydiet"
include(":app-platform")
include(":app")

include(":core:remote:food-data-central")
include(":core:remote:food-data-central-test")
include(":core:measure")


include(":feature:bmi:presentation")
include(":feature:bmi:data")
include(":feature:meal:presentation")
include(":feature:meal:data")
include(":feature:search:presentation")
include(":feature:search:data")

include(":screenshot")
