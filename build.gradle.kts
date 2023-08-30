// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.calamarfederal.android-application") apply false
    id("org.calamarfederal.android-common") apply false
    id("org.calamarfederal.hilt") apply false
    id("org.calamarfederal.room") apply false
    id("org.calamarfederal.kotlin-library") apply false
//    alias(libs.plugins.kotlin.serialization.gradle) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.kotlin.kapt) apply false
//    alias(libs.plugins.kotlin.hilt) apply false
}
