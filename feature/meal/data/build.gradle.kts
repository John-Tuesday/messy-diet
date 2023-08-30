plugins {
    id("org.calamarfederal.android-common")
    id("org.calamarfederal.hilt")
    id("org.calamarfederal.room")
}

group = "$group.feature.meal"

dependencies {
    implementation(platform("org.calamarfederal.platform:plugins-platform"))
    implementation(platform(project(":app-platform")))

    implementation(project(":feature:measure"))
    implementation(project(":core:diet-model"))
    implementation(project(":core:measure"))
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
