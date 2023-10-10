plugins {
    id("messydiet.kotlin.library")
    alias(libs.plugins.kotlin.serialization.gradle)
    alias(libs.plugins.kotlin.ksp)
    `java-test-fixtures`
}

configurations.testFixturesCompileOnlyApi.configure {
    extendsFrom(configurations.mainSourceElements.get())
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

        val testFixtures by getting {
            dependsOn(main)
        }

        val test by getting {
            dependsOn(testFixtures)
            dependencies {
                implementation(libs.nutrition.test)
                implementation(libs.kotlin.test)
                implementation(libs.kotlin.coroutine.test)
                implementation(libs.mockk)
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
