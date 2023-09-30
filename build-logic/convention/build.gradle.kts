plugins {
    `kotlin-dsl`
}

group = "org.calamarfederal.messydiet.build-logic"

dependencies {
    compileOnly(libs.kotlin.android.gradlePlugin)
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kotlinLibrary") {
            id = "messydiet.kotlin.library"
            implementationClass = "KotlinLibraryConvention"
        }
        register("androidLibrary") {
            id = "messydiet.android.library"
            implementationClass = "AndroidLibraryConvention"
        }
        register("androidLibraryCompose") {
            id = "messydiet.android.library.compose"
            implementationClass = "AndroidLibraryComposeConvention"
        }
        register("androidTest") {
            id = "messydiet.android.test"
            implementationClass = "AndroidTestConvention"
        }
        register("androidTestCompose") {
            id = "messydiet.android.test.compose"
            implementationClass = "AndroidTestComposeConvention"
        }
        register("androidFeature") {
            id = "messydiet.android.feature"
            implementationClass = "AndroidFeatureConvention"
        }
        register("androidApplication") {
            id = "messydiet.android.application"
            implementationClass = "AndroidApplicationConvention"
        }
        register("androidApplicationCompose") {
            id = "messydiet.android.application.compose"
            implementationClass = "AndroidApplicationComposeConvention"
        }
        register("androidHilt") {
            id = "messydiet.android.hilt"
            implementationClass = "AndroidHiltConvention"
        }
        register("androidRoom") {
            id = "messydiet.android.room"
            implementationClass = "AndroidRoomConvention"
        }
    }
}
