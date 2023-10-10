plugins {
    id("messydiet.android.library")
    id("messydiet.android.hilt")
}

android {
    namespace = "org.calamarfederal.messydiet.feature.search.data"
}

kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(platform(project(":app-platform")))
                implementation(project(":core:remote:food-data-central"))
                implementation(project(":core:android:hilt"))
                implementation(libs.measure)
                implementation(libs.nutrition)
                implementation(project(":feature:meal:data"))
            }
        }

        val test by getting {
            dependencies {
                implementation(project(":core:test:remote:food-data-central"))
                implementation(libs.mockk)
                implementation(libs.junit)
            }
        }
    }
}

dependencies {
    debugImplementation(libs.bundles.compose.debug)
}
