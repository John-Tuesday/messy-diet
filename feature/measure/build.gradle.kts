plugins {
    id("messydiet.android.feature")
}

android {
    namespace = "org.calamarfederal.messydiet.feature.measure"
}

dependencies {
    implementation(platform(project(":app-platform")))
    implementation(libs.nutrition)
    androidTestImplementation(libs.nutrition.test)
    implementation(libs.measure)

    androidTestImplementation(project(":core:test:common"))

    implementation(libs.bundles.compose.implementation)
    testImplementation(libs.junit)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.compose.androidTest)
}
