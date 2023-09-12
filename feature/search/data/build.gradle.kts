plugins {
    id("org.calamarfederal.android-common")
    id("org.calamarfederal.hilt")
}

group = "org.calamarfederal.messydiet.feature.search"

android {
    namespace = "${project.group}.${project.name}"
}

dependencies {
    implementation(platform("org.calamarfederal.platform:plugins-platform"))
    implementation(platform(project(":app-platform")))

    implementation(project(":core:remote:food-data-central"))
    androidTestImplementation(project(":core:test:remote:food-data-central"))
    implementation(project(":core:android:hilt"))
    implementation(project(":core:diet-model"))
    androidTestImplementation(project(":core:test:measure"))

    implementation(project(":feature:meal:data"))

    testImplementation(libs.junit)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.compose.androidTest)
}
