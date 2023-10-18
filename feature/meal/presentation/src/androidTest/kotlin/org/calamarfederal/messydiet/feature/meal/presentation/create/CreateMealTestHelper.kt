package org.calamarfederal.messydiet.feature.meal.presentation.create

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import io.github.john.tuesday.nutrition.FoodNutrition
import io.github.john.tuesday.nutrition.NutrientType
import org.calamarfederal.messydiet.feature.meal.presentation.R
import org.calamarfederal.messydiet.measure.getFullName
import org.calamarfederal.physical.measurement.inGrams
import org.calamarfederal.physical.measurement.inKilocalories

internal fun performDataEntry(
    composeRule: AndroidComposeTestRule<*, *>,
    mealName: String,
    mealNutrition: FoodNutrition,
) {
    composeRule
        .onNodeWithText(text = composeRule.activity.getString(R.string.meal_name_placeholder))
        .performTextReplacement(mealName)

    composeRule
        .onNodeWithText(text = composeRule.activity.getString(R.string.serving_size))
        .performTextReplacement(mealNutrition.portion.mass!!.inGrams().toString())

    composeRule
        .onNodeWithText(text = composeRule.activity.getString(R.string.food_energy_label))
        .performTextReplacement(mealNutrition.foodEnergy.inKilocalories().toString())

    composeRule
        .onNodeWithContentDescription(label = composeRule.activity.getString(R.string.expand_fat_group))
        .performScrollTo()
        .performClick()

    composeRule
        .onNodeWithContentDescription(label = composeRule.activity.getString(R.string.expand_carbohydrate_group))
        .performScrollTo()
        .performClick()

    composeRule
        .onNode(
            hasAnyChild(hasText(composeRule.activity.getString(R.string.show_more))) and hasClickAction(),
            useUnmergedTree = true,
        )
        .performScrollTo()
        .performClick()

    for ((nutrientType, mass) in mealNutrition.nutrients.entries) {
        val resources = composeRule.activity.resources
        val text = when (nutrientType) {
            NutrientType.Protein -> resources.getString(R.string.meal_protein_label)
            NutrientType.TotalFat -> resources.getString(R.string.meal_total_fats_label)
            NutrientType.TotalCarbohydrate -> resources.getString(R.string.meal_total_carbohydrates_label)
            else -> nutrientType.getFullName(resources)
        }
        composeRule
            .onNodeWithText(text = text)
            .assertExists(nutrientType.getFullName(resources))
            .performScrollTo()
            .performTextReplacement(text = mass.inGrams().toString())
    }
}
