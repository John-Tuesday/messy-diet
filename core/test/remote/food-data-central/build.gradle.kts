plugins {
    id("messydiet.kotlin.library")
}

group = "org.calamarfederal.messydiet.test.remote"

kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(platform(project(":app-platform")))
                implementation(project(":core:remote:food-data-central"))
                implementation(project(":core:test:common"))
                implementation(libs.nutrition)
                implementation(libs.measure)
            }
        }
    }
}
