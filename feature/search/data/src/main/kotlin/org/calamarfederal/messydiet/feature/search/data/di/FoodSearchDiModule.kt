package org.calamarfederal.messydiet.feature.search.data.di

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.calamarfederal.messydiet.feature.meal.data.di.FeatureMealDataModule
import org.calamarfederal.messydiet.feature.search.data.*
import org.calamarfederal.messydiet.food.data.central.di.FoodDataCentral

interface FeatureSearchDataModule {
    fun provideFoodSearchRepository(): FoodSearchRepository
    fun provideFoodDetailsRepository(): FoodDetailsRepository

    fun provideSaveFoodDetailsRepository(): SaveFoodDetailsRepository

    companion object {
        fun implementation(context: Context): FeatureSearchDataModule = FeatureSearchDataModuleImplementation(
            mealDataModule = FeatureMealDataModule.implementation(context)
        )
    }
}

internal class FeatureSearchDataModuleImplementation(
    private val mealDataModule: FeatureMealDataModule,
) : FeatureSearchDataModule {
    internal val networkDispatcher: CoroutineDispatcher get() = Dispatchers.IO
    internal val foodDataCentralApiKey: String get() = FoodDataCentral.DemoKey
    internal val foodDataCentralRepository
        get() = FoodDataCentral.repository(
            networkDispatcher = networkDispatcher,
            apiKey = foodDataCentralApiKey,
        )

    override fun provideFoodSearchRepository(): FoodSearchRepository = FoodSearchRepositoryImplementation(
        fdcRepo = foodDataCentralRepository,
    )

    override fun provideFoodDetailsRepository(): FoodDetailsRepository = FoodDetailsRepositoryImplementation(
        fdcRepo = foodDataCentralRepository,
    )

    override fun provideSaveFoodDetailsRepository(): SaveFoodDetailsRepository =
        SaveFoodDetailsRepositoryImplementation(
            mealRepository = mealDataModule.provideMealRepository()
        )

}
