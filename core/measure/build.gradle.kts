plugins {
    id("messydiet.android.feature")
}

android {
    namespace = "org.calamarfederal.messydiet.measure"
}

kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(platform(project(":app-platform")))
                implementation(libs.nutrition)
                implementation(libs.measure)
                implementation(libs.bundles.compose.implementation)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(libs.nutrition.test)
                implementation(libs.bundles.compose.androidTest)
            }
        }
    }
}

dependencies {
    debugImplementation(libs.bundles.compose.debug)
}
