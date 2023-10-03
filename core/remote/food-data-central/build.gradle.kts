plugins {
    id("messydiet.kotlin.library")
    alias(libs.plugins.kotlin.serialization.gradle)
    alias(libs.plugins.kotlin.ksp)
}

dependencies {
    implementation(platform(project(":app-platform")))
    implementation(project(":core:diet-model"))
    implementation(libs.measure)

    testImplementation(project(":core:test:measure"))
    testImplementation(project(":core:test:remote:food-data-central"))

    implementation(libs.kotlin.kodein.di)

    implementation(libs.kotlin.coroutine)
    testImplementation(libs.kotlin.coroutine.test)

    implementation(libs.kotlin.serialization.json)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
}
