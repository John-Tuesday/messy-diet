plugins {
    id("org.calamarfederal.kotlin-library")
}

dependencies {
    implementation(platform("org.calamarfederal.platform:plugins-platform"))
    implementation(platform(project(":app-platform")))
}
