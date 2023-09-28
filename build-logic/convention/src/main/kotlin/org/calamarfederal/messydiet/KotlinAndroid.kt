package org.calamarfederal.messydiet

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal val DependencyHandlerScope.ksp: String get() = "ksp"
internal val DependencyHandlerScope.kspAndroidTest: String get() = "kspAndroidTest"
internal val DependencyHandlerScope.implementation: String get() = "implementation"
internal val DependencyHandlerScope.testImplementation: String get() = "testImplementation"
internal val DependencyHandlerScope.androidTestImplementation: String get() = "androidTestImplementation"

/**
 * Configure Kotlin for Android
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        buildToolsVersion = "34.0.0"
        compileSdk = 34

        defaultConfig {
            minSdk = 33
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }

    dependencies {
        androidTestImplementation(kotlin("test"))
    }

    configureKotlin()
}

/**
 * Configure Kotlin for jvm (local/non-Android)
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    configureKotlin()
}

/**
 * Configure base kotlin
 */
private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    dependencies {
        implementation(libs.findLibrary("kotlin.stdlib").get())
        implementation(libs.findLibrary("kotlin.coroutine").get())
        testImplementation(kotlin("test"))
    }
}
