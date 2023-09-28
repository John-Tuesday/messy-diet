plugins {
    id("messydiet.android.feature")
    id("messydiet.android.hilt")
    id("messydiet.android.room")
}

android {
    namespace = "org.calamarfederal.messydiet.feature.meal.presentation"
}

dependencies {
    implementation(platform("org.calamarfederal.platform:plugins-platform"))
    implementation(platform(project(":app-platform")))

    implementation(project(":feature:meal:data"))
    implementation(project(":feature:measure"))
    implementation(project(":core:diet-model"))
    implementation(project(":core:measure"))

    // Lifecycle
    implementation(libs.lifecycle.compose.utils)

    implementation(libs.bundles.compose.implementation)
    implementation(libs.androidx.activity.compose)
    implementation(libs.navigation.compose)
    testImplementation(libs.junit)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.compose.androidTest)
}
