plugins {
    id("messydiet.android.application")
    id("messydiet.android.application.compose")
    id("messydiet.android.application.flavors")
    id("messydiet.android.hilt")
}

android {
    namespace = "org.calamarfederal.messydiet"

    defaultConfig {
        applicationId = "org.calamarfederal.messydiet"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(platform(project(":app-platform")))
                implementation(project(":feature:bmi:data"))
                implementation(project(":feature:bmi:presentation"))
                implementation(project(":feature:meal:presentation"))
                implementation(project(":feature:search:presentation"))

                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.lifecycle)
                implementation(libs.androidx.activity.compose)
                implementation(libs.bundles.compose.implementation)
                implementation(libs.navigation.compose)

                implementation(libs.hilt.android)
            }
        }

        val test by getting {
            dependencies {
                implementation(libs.junit)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(libs.bundles.compose.androidTest)
                implementation(libs.hilt.android.testing)
            }
        }
    }
}

dependencies {
    debugImplementation(libs.bundles.compose.debug)
    ksp(libs.hilt.compiler)
    kspAndroidTest(libs.hilt.compiler)
}
