package org.calamarfederal.messydiet.feature.meal.presentation.view

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.john.tuesday.nutrition.FoodNutrition
import io.github.john.tuesday.nutrition.Portion
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import org.calamarfederal.messydiet.feature.meal.presentation.R
import org.calamarfederal.physical.measurement.grams
import org.calamarfederal.physical.measurement.kilocalories
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewAllMealUiUnitTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var testMeals: List<Meal>

    @Before
    fun setUp() {
        testMeals = listOf(
            Meal(
                id = 1, name = "alpha", foodNutrition = FoodNutrition(
                    portion = Portion(1.grams),
                    foodEnergy = 1.kilocalories,
                    nutritionMap = mapOf(),
                )
            ),
            Meal(
                id = 2, name = "BETA", foodNutrition = FoodNutrition(
                    portion = Portion(1.grams),
                    foodEnergy = 1.kilocalories,
                    nutritionMap = mapOf(),
                )
            ),
            Meal(
                id = 3, name = "chArlie", foodNutrition = FoodNutrition(
                    portion = Portion(1.grams),
                    foodEnergy = 1.kilocalories,
                    nutritionMap = mapOf(),
                )
            ),
        )

        composeRule.setContent {
            ViewAllMealsLayout(
                meals = testMeals,
            )
        }
    }

    @Test
    fun `Each meal is shown with its name`() {
        for (meal in testMeals) {
            composeRule
                .onNodeWithText(meal.name)
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .assertHasClickAction()
        }
    }

    @Test
    fun `Long press on a Meal to show options`() {
        composeRule
            .onNodeWithText(testMeals.first().name)
            .performScrollTo()
            .performSemanticsAction(SemanticsActions.OnLongClick)

        val deleteText = composeRule.activity.getString(R.string.delete)
        val editText = composeRule.activity.getString(R.string.edit)

        composeRule
            .onNodeWithText(deleteText)
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()

        composeRule
            .onNodeWithText(editText)
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }
}
