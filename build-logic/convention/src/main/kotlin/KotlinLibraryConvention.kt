import org.calamarfederal.messydiet.configureKotlinJvm
import org.calamarfederal.messydiet.configureRemoteApiTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class KotlinLibraryConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }

            configureKotlinJvm()
        }
    }
}

class KotlinLibraryRemoteApiTestConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }

            extensions.configure<KotlinJvmProjectExtension> {
                target {
                    configureRemoteApiTest(provider { tasks })
                }
            }
        }
    }
}
