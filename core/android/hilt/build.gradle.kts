plugins {
    id("messydiet.android.library")
    id("messydiet.android.hilt")
}

android {
    namespace = "org.calamarfederal.messydiet.core.android.hilt"
}

dependencies {
    implementation(platform("org.calamarfederal.platform:plugins-platform"))
    implementation(platform(project(":app-platform")))

    implementation(libs.kotlin.coroutine)
    testImplementation(libs.kotlin.coroutine.test)
}
