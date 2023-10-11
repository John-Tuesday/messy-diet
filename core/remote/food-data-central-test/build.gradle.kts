plugins {
    id("messydiet.kotlin.library")
}

kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(platform(project(":app-platform")))
                implementation(project(":core:remote:food-data-central"))
                implementation(libs.nutrition)
                implementation(libs.measure)
            }
        }
    }
}
