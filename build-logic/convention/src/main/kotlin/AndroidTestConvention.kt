import com.android.build.gradle.TestExtension
import org.calamarfederal.messydiet.configureAndroidCommon
import org.calamarfederal.messydiet.configureAndroidCompose
import org.calamarfederal.messydiet.configureGradleManagedDevices
import org.calamarfederal.messydiet.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidTestConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.test")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
                configureAndroidCommon(this)

                defaultConfig.targetSdk = 34
                configureGradleManagedDevices(this)
            }
        }
    }
}

class AndroidTestComposeConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.test")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<TestExtension> {
                configureAndroidCompose(this)
            }
        }
    }
}
