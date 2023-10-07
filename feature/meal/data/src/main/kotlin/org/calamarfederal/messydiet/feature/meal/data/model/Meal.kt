package org.calamarfederal.messydiet.feature.meal.data.model

import io.github.john.tuesday.nutrition.FoodNutrition
import io.github.john.tuesday.nutrition.Portion
import org.calamarfederal.messydiet.feature.meal.data.local.MealEntity
import org.calamarfederal.physical.measurement.grams
import org.calamarfederal.physical.measurement.kilocalories

data class Meal(
    val id: Long = MealEntity.UNSET_ID,
    val name: String = "",
    val foodNutrition: FoodNutrition = FoodNutrition(
        portion = Portion(0.grams),
        foodEnergy = 0.kilocalories,
        nutritionMap = mapOf()
    ),
)
