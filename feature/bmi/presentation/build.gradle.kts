/******************************************************************************
 * Copyright (c) 2023 John Tuesday Picot                                      *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  *
 * copies of the Software, and to permit persons to whom the Software is      *
 * furnished to do so, subject to the following conditions:                   *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.                            *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,   *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE*
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER     *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.                                                                  *
 ******************************************************************************/
plugins {
    id("messydiet.android.feature")
    id("messydiet.android.hilt")
    id("messydiet.android.room")
}

android {
    namespace = "org.calamarfederal.messydiet.feature.bmi.presentation"
}

kotlin {
    sourceSets {
        val main by getting {
            dependencies {
                implementation(platform(project(":app-platform")))
                implementation(project(":feature:bmi:data"))
                implementation(project(":feature:measure"))
                implementation(libs.measure)
                implementation(libs.lifecycle.compose.utils)
                implementation(libs.bundles.compose.implementation)
                implementation(libs.androidx.activity.compose)
                implementation(libs.navigation.compose)
            }
        }

        val test by getting {
            dependencies {
                implementation(libs.junit)
            }
        }

        val androidTest by getting {
            dependencies {
                implementation(libs.bundles.compose.androidTest)
            }
        }
    }
}

dependencies {
    debugImplementation(libs.bundles.compose.debug)
}
