plugins {
    id("org.calamarfederal.kotlin-library")
}

group = "org.calamarfederal.messydiet.test"

dependencies {
    implementation(platform("org.calamarfederal.platform:plugins-platform"))
    implementation(platform(project(":app-platform")))

    implementation(project(":core:test:common"))

    api(project(":core:diet-model"))
    api(project(":core:measure"))

    implementation(libs.kotlin.test.junit)
}
