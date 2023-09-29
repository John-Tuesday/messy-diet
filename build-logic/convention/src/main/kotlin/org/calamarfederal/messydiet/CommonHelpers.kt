package org.calamarfederal.messydiet

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal val DependencyHandlerScope.ksp: String get() = "ksp"
internal val DependencyHandlerScope.kspAndroidTest: String get() = "kspAndroidTest"
internal val DependencyHandlerScope.implementation: String get() = "implementation"
internal val DependencyHandlerScope.testImplementation: String get() = "testImplementation"
internal val DependencyHandlerScope.androidTestImplementation: String get() = "androidTestImplementation"
