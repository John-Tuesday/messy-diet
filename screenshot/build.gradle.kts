plugins {
    id("messydiet.android.test")
    id("messydiet.android.test.compose")
}

android {
    namespace = "org.calamarfederal.messydiet.screenshot"
    targetProjectPath = ":app"
}

dependencies {
    implementation(libs.kotlin.test.junit)
    implementation(libs.bundles.compose.debug)
    implementation(libs.bundles.compose.androidTest)

    implementation(project(":core:test:measure"))
}
