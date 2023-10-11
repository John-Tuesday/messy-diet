import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl`
}

group = "org.calamarfederal.messydiet.build-logic"

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_1_9
        apiVersion = KotlinVersion.KOTLIN_1_9
        progressiveMode = true
    }
}

dependencies {
    compileOnly(libs.kotlin.android.gradlePlugin)
    compileOnly(libs.kotlin.multiplatform.gradlePlugin)
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
        register("kotlinLibraryRemoteApiTest") {
            id = "messydiet.kotlin.library.remote-api-test"
            implementationClass = "KotlinLibraryRemoteApiTestConvention"
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
        register("androidApplicationFlavors") {
            id = "messydiet.android.application.flavors"
            implementationClass = "AndroidApplicationFlavorsConvention"
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
