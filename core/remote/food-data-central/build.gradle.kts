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

        val test by getting {
            dependencies {
                implementation(project(":core:test:remote:food-data-central"))
                implementation(libs.nutrition.test)
                implementation(libs.kotlin.coroutine.test)
            }
        }
    }
}

tasks.test {
    filter {
        excludePatterns += "*"
    }
}

dependencies {
    ksp(libs.moshi.codegen)
}
