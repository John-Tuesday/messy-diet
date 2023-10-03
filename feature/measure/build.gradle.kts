plugins {
    id("messydiet.android.feature")
}

android {
    namespace = "org.calamarfederal.messydiet.feature.measure"
}

dependencies {
    implementation(platform(project(":app-platform")))
    implementation(project(":core:diet-model"))
    implementation(libs.measure)

    androidTestImplementation(project(":core:test:common"))
    androidTestImplementation(project(":core:test:measure"))

    implementation(libs.bundles.compose.implementation)
    testImplementation(libs.junit)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.compose.androidTest)
}
