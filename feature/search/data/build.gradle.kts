plugins {
    id("messydiet.android.library")
    id("messydiet.android.hilt")
}

android {
    namespace = "org.calamarfederal.messydiet.feature.search.data"
}

dependencies {
    implementation(platform(project(":app-platform")))

    implementation(project(":core:remote:food-data-central"))
    testImplementation(project(":core:test:remote:food-data-central"))
    implementation(project(":core:android:hilt"))
    implementation(libs.measure)
    implementation(libs.nutrition)

    implementation(project(":feature:meal:data"))

    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.compose.androidTest)
}
