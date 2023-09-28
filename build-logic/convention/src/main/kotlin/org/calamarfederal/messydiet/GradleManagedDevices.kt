package org.calamarfederal.messydiet

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import org.gradle.kotlin.dsl.maybeCreate

internal fun configureGradleManagedDevices(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.testOptions {
        managedDevices {
            devices.apply {
                maybeCreate<ManagedVirtualDevice>("pixel7api33").apply {
                    device = "Pixel 7"
                    apiLevel = 33
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }

}
