package org.calamarfederal.messydiet.feature.search.data.repository

import io.github.john.tuesday.nutrition.FoodNutrition
import org.calamarfederal.messydiet.feature.meal.data.MealRepository
import org.calamarfederal.messydiet.feature.meal.data.model.Meal

interface SaveFoodDetailsRepository {
    suspend fun saveFoodDetails(name: String, nutritionInfo: FoodNutrition): Long
}

internal class SaveFoodDetailsRepositoryImplementation(
    private val mealRepository: MealRepository,
) : SaveFoodDetailsRepository {
    override suspend fun saveFoodDetails(name: String, nutritionInfo: FoodNutrition): Long {
        return mealRepository.insertMeal(Meal(name = name, foodNutrition = nutritionInfo))
    }

}
