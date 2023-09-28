plugins {
    id("messydiet.android.library")
    id("messydiet.android.hilt")
//    id("org.calamarfederal.android-common")
//    id("org.calamarfederal.hilt")
}

//group = "org.calamarfederal.messydiet.feature.search"

android {
    namespace = "org.calamarfederal.messydiet.feature.search.data"
}

dependencies {
    implementation(platform(project(":app-platform")))

    implementation(project(":core:remote:food-data-central"))
    testImplementation(project(":core:test:remote:food-data-central"))
    implementation(project(":core:android:hilt"))
    implementation(project(":core:diet-model"))
    testImplementation(project(":core:test:measure"))

    implementation(project(":feature:meal:data"))

    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.compose.androidTest)
}
