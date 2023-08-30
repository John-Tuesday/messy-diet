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

    implementation(project(":feature:search:data"))
    implementation(project(":feature:measure"))
    implementation(project(":core:diet-model"))

    implementation(libs.lifecycle.compose.utils)
    implementation(libs.bundles.compose.implementation)
    implementation(libs.androidx.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.navigation.ui)
    testImplementation(libs.junit)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.compose.androidTest)
}
