plugins {
    id("messydiet.android.feature")
}

android {
    namespace = "org.calamarfederal.messydiet.feature.measure"
}

dependencies {
    implementation(platform("org.calamarfederal.platform:plugins-platform"))
    implementation(platform(project(":app-platform")))
    implementation(project(":core:diet-model"))
    implementation(project(":core:measure"))

    androidTestImplementation(project(":core:test:common"))
    androidTestImplementation(project(":core:test:measure"))

    implementation(libs.bundles.compose.implementation)
    testImplementation(libs.junit)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.compose.androidTest)
}
