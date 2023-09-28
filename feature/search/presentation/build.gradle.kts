plugins {
    id("messydiet.android.feature")
    id("messydiet.android.hilt")
}


android {
    namespace = "org.calamarfederal.messydiet.feature.search.presentation"
}

dependencies {
    implementation(platform("org.calamarfederal.platform:plugins-platform"))
    implementation(platform(project(":app-platform")))

    implementation(project(":feature:search:data"))
    implementation(project(":feature:measure"))
    implementation(project(":core:diet-model"))

    androidTestImplementation(project(":core:test:common"))

    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera2)
    implementation(libs.androidx.camerax.lifecycle)
    implementation(libs.androidx.camerax.view)
    implementation(libs.androidx.camerax.extenstions)
    implementation(libs.mlkit.barcode)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity)

    implementation(libs.lifecycle.compose.utils)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.bundles.compose.implementation)
    implementation(libs.androidx.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.navigation.ui)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test.junit)
    androidTestImplementation(libs.kotlin.test.junit)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation(libs.bundles.compose.androidTest)
}
