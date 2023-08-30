package org.calamarfederal.messydiet.feature.search.data

import org.calamarfederal.messydiet.diet_data.model.NutritionInfo
import org.calamarfederal.messydiet.feature.meal.data.MealRepository
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import javax.inject.Inject

interface SaveFoodDetailsRepository {
    suspend fun saveFoodDetails(name: String, nutritionInfo: NutritionInfo): Long
}

internal class SaveFoodDetailsRepositoryImplementation @Inject constructor(
    private val mealRepository: MealRepository,
) : SaveFoodDetailsRepository {
    override suspend fun saveFoodDetails(name: String, nutritionInfo: NutritionInfo): Long {
        return mealRepository.insertMeal(Meal(name = name, nutritionInfo = nutritionInfo))
    }

}
