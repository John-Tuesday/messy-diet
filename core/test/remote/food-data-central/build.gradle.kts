plugins {
    id("messydiet.kotlin.library")
}

group = "org.calamarfederal.messydiet.test.remote"

dependencies {
    implementation(platform(project(":app-platform")))

    implementation(project(":core:remote:food-data-central"))

    implementation(project(":core:test:common"))

    implementation(libs.nutrition)
    implementation(libs.measure)
}
