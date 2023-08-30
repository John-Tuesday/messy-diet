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
    id("java-platform")
}

group = "org.calamarfederal.platform"

dependencies {
    constraints {
//        api("com.android.tools.build:gradle:8.1.0")
        api(libs.android.tools)
//        api("com.android.build.gradle:com.android.application:8.1.0")
        api(libs.android.build)
//        api("org.jetbrains.kotlin.android:org.jetbrains.kotlin.android.gradle.plugin:1.9.0")
        api(libs.kotlin.android)
//        api("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.0")
        api(libs.kotlin.jvm)
//        api("org.jetbrains.kotlin.kapt:org.jetbrains.kotlin.kapt.gradle.plugin:1.9.0")
        api(libs.kotlin.kapt)
//        api("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
        api(libs.kotlin.stdlib)
//        api("com.google.dagger:hilt-android-gradle-plugin:2.47")
        api(libs.hilt.plugin)
//        api("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.0-1.0.13")
        api(libs.kotlin.ksp)
        api(libs.kotlin.serialization.plugin)
    }
}
