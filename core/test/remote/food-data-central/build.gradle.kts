plugins {
    id("messydiet.kotlin.library")
}

group = "org.calamarfederal.messydiet.test.remote"

dependencies {
    implementation(platform("org.calamarfederal.platform:plugins-platform"))
    implementation(platform(project(":app-platform")))

    implementation(project(":core:remote:food-data-central"))

    implementation(project(":core:test:common"))

    implementation(project(":core:diet-model"))
    implementation(project(":core:measure"))
}
