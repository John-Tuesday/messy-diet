package org.calamarfederal.messydiet

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

/**
 * Android configurations common to all & unrelated to [configureKotlinAndroid]
 */
internal fun Project.configureAndroidCommon(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        buildToolsVersion = "34.0.0"

        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }

}
