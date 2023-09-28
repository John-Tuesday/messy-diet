plugins {
    id("messydiet.android.application")
    id("messydiet.android.application.compose")
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

dependencies {
    implementation(platform(project(":app-platform")))
    implementation(project(":core:diet-model"))
    implementation(project(":feature:bmi:presentation"))
    implementation(project(":feature:meal:presentation"))
    implementation(project(":feature:search:presentation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.compose.implementation)
    implementation(libs.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.compose.androidTest)
    debugImplementation(libs.bundles.compose.debug)

    // Lifecycle
//    implementation(libs.lifecycle.compose.utils)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
}
