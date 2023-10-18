package org.calamarfederal.messydiet.feature.meal.presentation

import android.content.Context
import org.calamarfederal.messydiet.feature.meal.presentation.di.FeatureMealsPresentationModule
import org.calamarfederal.messydiet.feature.meal.presentation.di.testImplementation

internal fun MealsGraphModule.Companion.testImplementation(context: Context): MealsGraphModule =
    MealsGraphModuleTestImplementation(context)

internal class MealsGraphModuleTestImplementation(
    private val context: Context,
) : MealsGraphModule() {
    override val featureMealsPresentationModule: FeatureMealsPresentationModule by lazy {
        FeatureMealsPresentationModule.testImplementation(context)
    }
}
