plugins {
    id("messydiet.android.feature")
}


android {
    namespace = "org.calamarfederal.messydiet.feature.search.presentation"
}

kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(platform(project(":app-platform")))
                implementation(project(":feature:search:data"))
                implementation(project(":core:measure"))
                implementation(libs.measure)
                implementation(libs.nutrition)
                implementation(libs.androidx.camera.core)
                implementation(libs.androidx.camera2)
                implementation(libs.androidx.camerax.lifecycle)
                implementation(libs.androidx.camerax.view)
                implementation(libs.androidx.camerax.extenstions)
                implementation(libs.mlkit.barcode)
                implementation(libs.androidx.activity.ktx)
                implementation(libs.androidx.activity)
                implementation(libs.lifecycle.compose.utils)
                implementation(libs.lifecycle.viewmodel.ktx)
                implementation(libs.bundles.compose.implementation)
                implementation(libs.androidx.activity.compose)
                implementation(libs.navigation.compose)
                implementation(libs.androidx.navigation.ui)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(libs.kotlin.test.junit)
                implementation(libs.bundles.compose.androidTest)
            }
        }
    }
}

dependencies {
    debugImplementation(libs.bundles.compose.debug)
}
