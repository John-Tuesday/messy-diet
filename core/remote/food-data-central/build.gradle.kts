
plugins {
    id("messydiet.kotlin.library")
    alias(libs.plugins.kotlin.serialization.gradle)
    alias(libs.plugins.kotlin.ksp)
}

kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(platform(project(":app-platform")))
                implementation(libs.measure)
                implementation(libs.nutrition)
                implementation(libs.kotlin.kodein.di)
                implementation(libs.kotlin.coroutine)
                implementation(libs.kotlin.serialization.json)
                implementation(libs.retrofit)
                implementation(libs.retrofit.moshi)
                implementation(libs.moshi)
            }
        }

        val testFixturesLocal by creating {
        }

        val test by getting {
            dependsOn(testFixturesLocal)
            dependencies {
                implementation(libs.nutrition.test)
                implementation(libs.kotlin.test)
                implementation(libs.kotlin.coroutine.test)
                implementation(libs.mockk)
                implementation(project(":core:remote:food-data-central-test"))
            }
        }
    }
}

dependencies {
    ksp(libs.moshi.codegen)
}
