package org.calamarfederal.messydiet

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension

sealed interface Flavor {
    val applicationIdSuffix: String?
    val dimension: FlavorDimension
    val flavorName: String
}

sealed interface FlavorDimension {
    val dimensionName: String
}

enum class ContentFlavor(override val applicationIdSuffix: String? = null) : Flavor {
    Production,
    Demo(".demo"),
    ;

    override val flavorName: String
        get() = "${name.first().lowercase()}${name.drop(1)}"

    override val dimension: FlavorDimension get() = ContentFlavor.DimensionType

    companion object DimensionType : FlavorDimension {
        override val dimensionName: String = "contentType"
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        flavorDimensions += ContentFlavor.dimensionName
        productFlavors {
            for (flavor in ContentFlavor.entries) {
                create(flavor.flavorName) {
                    dimension = flavor.dimension.dimensionName
                    if (commonExtension is ApplicationExtension && this is ApplicationProductFlavor && flavor.applicationIdSuffix != null)
                        applicationIdSuffix = flavor.applicationIdSuffix
                }
            }
        }
    }
}
