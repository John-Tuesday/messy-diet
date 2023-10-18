package org.calamarfederal.messydiet.feature.meal.presentation.di

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.calamarfederal.messydiet.feature.meal.data.di.FeatureMealDataModule

internal interface FeatureMealsPresentationModule : FeatureMealDataModule {
    companion object
}

internal class FeatureMealsPresentationModuleImplementation(
    private val dataModule: FeatureMealDataModule,
) : FeatureMealsPresentationModule, FeatureMealDataModule by dataModule

internal fun FeatureMealsPresentationModule.Companion.implementation(
    context: Context,
): FeatureMealsPresentationModule {
    return FeatureMealsPresentationModuleImplementation(
        dataModule = FeatureMealDataModule.implementation(context)
    )
}

@Composable
internal fun rememberFeatureMealsPresentationModule(
    context: Context = LocalContext.current,
): FeatureMealsPresentationModule = remember {
    FeatureMealsPresentationModule.implementation(context = context)
}
