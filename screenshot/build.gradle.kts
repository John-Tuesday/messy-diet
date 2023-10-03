plugins {
    id("messydiet.android.library")
    id("messydiet.android.library.compose")
}

android {
    namespace = "org.calamarfederal.messydiet.screenshot"
}

dependencies {
    implementation(libs.kotlin.test.junit)
    implementation(libs.bundles.compose.debug)
    implementation(libs.bundles.compose.androidTest)

    implementation(project(":core:test:measure"))
}
