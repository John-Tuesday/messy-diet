plugins {
    id("messydiet.android.library")
    id("messydiet.android.hilt")
    id("messydiet.android.room")
}

android {
    namespace = "org.calamarfederal.messydiet.feature.meal.data"
}

dependencies {
    implementation(platform(project(":app-platform")))

    implementation(project(":feature:measure"))
    implementation(libs.measure)
    implementation(libs.nutrition)
    implementation(project(":core:android:hilt"))

    // Lifecycle
    implementation(libs.lifecycle.compose.utils)

    implementation(libs.bundles.compose.implementation)
    implementation(libs.androidx.activity.compose)
    implementation(libs.navigation.compose)
    testImplementation(libs.junit)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.compose.androidTest)
}
