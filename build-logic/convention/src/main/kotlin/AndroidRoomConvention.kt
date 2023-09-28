import com.google.devtools.ksp.gradle.KspExtension
import org.calamarfederal.messydiet.implementation
import org.calamarfederal.messydiet.ksp
import org.calamarfederal.messydiet.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConvention : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            extensions.configure<KspExtension> {
                // arg processor
            }

            dependencies {
                implementation(libs.findLibrary("room.runtime").get())
                implementation(libs.findLibrary("room.ktx").get())
                ksp(libs.findLibrary("room.compiler").get())
            }
        }

    }
}
