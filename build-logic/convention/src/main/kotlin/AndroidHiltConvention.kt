import org.calamarfederal.messydiet.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.android")
                apply("com.google.devtools.ksp")
                apply("dagger.hilt.android.plugin")
            }

            dependencies {
                implementation(libs.findLibrary("hilt.android").get())
                implementation(libs.findLibrary("hilt.navigation.compose").get())
                ksp(libs.findLibrary("hilt.compiler").get())
                androidTestImplementation(libs.findLibrary("hilt.android.testing").get())
                kspAndroidTest(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}
