plugins {
    id("messydiet.kotlin.library")
}

group = "org.calamarfederal.messydiet.test"

dependencies {
    implementation(platform(project(":app-platform")))

    implementation(project(":core:test:common"))

    api(project(":core:diet-model"))
    api(project(":core:measure"))

    implementation(libs.kotlin.test.junit)
}
