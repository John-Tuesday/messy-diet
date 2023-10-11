package org.calamarfederal.messydiet

import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinWithJavaTarget

fun KotlinWithJavaTarget<*, *>.configureRemoteApiTest(
    taskProvider: Provider<TaskContainer>,
) {
    val main by compilations.getting

    val remoteApiTest by compilations.creating {
        associateWith(main)
        defaultSourceSet {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }

        taskProvider.get().register<Test>("remoteApiTest") {
            useJUnit()
            classpath = compileDependencyFiles + runtimeDependencyFiles + output.allOutputs
            testClassesDirs = output.classesDirs
            group = "verification"
            description = "Runs the remote api test suite"
        }
    }
}
