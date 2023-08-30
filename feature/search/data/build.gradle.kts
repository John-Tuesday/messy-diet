plugins {
    id("org.calamarfederal.android-common")
    id("org.calamarfederal.hilt")
}

group = "org.calamarfederal.messydiet.feature.search"

android {
    namespace = "${project.group}.${project.name}"
}

dependencies {
    implementation(platform("org.calamarfederal.platform:plugins-platform"))
    implementation(platform(project(":app-platform")))

    implementation(project(":core:remote:food-data-central"))
    implementation(project(":core:android:hilt"))
    implementation(project(":core:diet-model"))

    implementation(project(":feature:meal:data"))
}
