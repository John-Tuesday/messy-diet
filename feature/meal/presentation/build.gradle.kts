plugins {
    id("messydiet.android.feature")
    id("messydiet.android.room")
}

android {
    namespace = "org.calamarfederal.messydiet.feature.meal.presentation"
}

kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(platform(project(":app-platform")))
                implementation(project(":feature:meal:data"))
                implementation(project(":core:measure"))
                implementation(libs.nutrition)
                implementation(libs.measure)
                implementation(libs.lifecycle.compose.utils)
                implementation(libs.bundles.compose.implementation)
                implementation(libs.androidx.activity.compose)
                implementation(libs.navigation.compose)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(libs.bundles.compose.androidTest)
                implementation(libs.nutrition.test)
            }
        }

        val androidTestDemo by getting {
            dependsOn(androidTest)
            dependencies {
                implementation(project(":screenshot"))
            }
        }
    }
}

dependencies {
    debugImplementation(libs.bundles.compose.debug)
}
