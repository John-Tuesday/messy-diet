plugins {
    id("org.calamarfederal.kotlin-library")
    alias(libs.plugins.kotlin.serialization.gradle)
    alias(libs.plugins.kotlin.ksp)
}

dependencies {
    implementation(platform("org.calamarfederal.platform:plugins-platform"))
    implementation(platform(project(":app-platform")))
    implementation(project(":core:diet-model"))
    implementation(project(":core:measure"))

    implementation(libs.kotlin.coroutine)
    testImplementation(libs.kotlin.coroutine.test)

    implementation(libs.kotlin.serialization.json)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
}
