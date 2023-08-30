plugins {
    id("org.calamarfederal.hilt")
}

dependencies {
    implementation(platform("org.calamarfederal.platform:plugins-platform"))
    implementation(platform(project(":app-platform")))

    implementation(libs.kotlin.coroutine)
    testImplementation(libs.kotlin.coroutine.test)
}
