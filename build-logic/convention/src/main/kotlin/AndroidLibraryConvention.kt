import com.android.build.gradle.LibraryExtension
import org.calamarfederal.messydiet.configureAndroidCompose
import org.calamarfederal.messydiet.configureGradleManagedDevices
import org.calamarfederal.messydiet.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                defaultConfig.targetSdk = 34
                configureGradleManagedDevices(this)
            }
        }
    }
}

class AndroidLibraryComposeConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureAndroidCompose(this)
            }
        }
    }
}

class AndroidFeatureConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("messydiet.android.library")
                apply("messydiet.android.library.compose")
            }
            extensions.configure<LibraryExtension> {
                configureGradleManagedDevices(this)
            }

            dependencies {
            }
        }
    }
}
