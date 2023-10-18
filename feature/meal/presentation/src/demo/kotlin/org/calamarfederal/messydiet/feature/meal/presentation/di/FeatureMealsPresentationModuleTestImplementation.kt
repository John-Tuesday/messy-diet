package org.calamarfederal.messydiet.feature.meal.presentation.di

import android.content.Context
import org.calamarfederal.messydiet.feature.meal.data.di.FeatureMealDataModule
import org.calamarfederal.messydiet.feature.meal.data.di.testImplementation

internal fun FeatureMealsPresentationModule.Companion.testImplementation(context: Context): FeatureMealsPresentationModule =
    FeatureMealsPresentationModuleImplementation(dataModule = FeatureMealDataModule.testImplementation(context))
