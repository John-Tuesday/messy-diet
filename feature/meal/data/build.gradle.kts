plugins {
    id("messydiet.android.library")
    id("messydiet.android.hilt")
    id("messydiet.android.room")
}

android {
    namespace = "org.calamarfederal.messydiet.feature.meal.data"
}

kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(platform(project(":app-platform")))
                implementation(project(":feature:measure"))
                implementation(libs.measure)
                implementation(libs.nutrition)
                implementation(project(":core:android:hilt"))
                implementation(libs.lifecycle.compose.utils)
                implementation(libs.bundles.compose.implementation)
                implementation(libs.androidx.activity.compose)
                implementation(libs.navigation.compose)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(libs.bundles.compose.androidTest)
            }
        }
    }
}

dependencies {
    debugImplementation(libs.bundles.compose.debug)
}
