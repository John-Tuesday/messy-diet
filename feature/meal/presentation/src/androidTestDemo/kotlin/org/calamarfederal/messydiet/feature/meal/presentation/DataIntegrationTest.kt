package org.calamarfederal.messydiet.feature.meal.presentation

import android.icu.number.LocalizedNumberFormatter
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.john.tuesday.nutrition.*
import org.calamarfederal.messydiet.feature.meal.presentation.create.performDataEntry
import org.calamarfederal.messydiet.measure.*
import org.calamarfederal.physical.measurement.inKilocalories
import org.calamarfederal.physical.measurement.inUnitsOf
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.calamarfederal.messydiet.measure.R as M


internal fun assertCorrectNutrition(
    composeRule: AndroidComposeTestRule<*, *>,
    mealName: String,
    mealNutrition: FoodNutrition,
    formatter: LocalizedNumberFormatter,
) {
    fun AndroidComposeTestRule<*, *>.onScrollContainer(): SemanticsNodeInteraction = onNode(hasScrollToKeyAction())
    val resources = composeRule.activity.resources

    composeRule
        .onNodeWithText(mealName)
        .assertExists()
        .assertIsDisplayed()

    composeRule
        .onNode(
            hasTextExactly(
                resources.getString(M.string.serving_size),
                formatter.format(mealNutrition.portion.inDefaultUnits()).toString(),
                mealNutrition.portion.defaultUnitLabel(resources),
            )
        )
        .assertExists()

    composeRule
        .onScrollContainer()
        .performScrollToKey(M.string.calories)
        .onChildren()
        .filterToOne(
            hasTextExactly(
                resources.getString(M.string.calories),
                formatter.format(mealNutrition.foodEnergy.inKilocalories()).toString(),
                "",
            )
        )

    for ((nutrientType, mass) in mealNutrition.nutrients) {
        val massUnit = nutrientType.defaultUnit()
        composeRule
            .onScrollContainer()
            .performScrollToKey(nutrientType.stringResId)
        composeRule
            .onNode(
                hasTextExactly(
                    nutrientType.getFullName(resources),
                    formatter.format(mass.inUnitsOf(massUnit)).toString(),
                    weightUnitShortString(massUnit, resources),
                ),
            )
            .assertExists()
    }
}

@RunWith(AndroidJUnit4::class)
class DataIntegrationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var mealsGraphModule: MealsGraphModule
    private lateinit var navController: NavController
    private lateinit var formatter: LocalizedNumberFormatter

    @BeforeTest
    fun setUp() {
        mealsGraphModule = MealsGraphModule.testImplementation(composeRule.activity)

        composeRule.setContent {
            val navHostController = rememberNavController()
            navController = navHostController

            formatter = simpleFormatter
            NavHost(
                navController = navHostController,
                startDestination = MealsGraph.route,
            ) {
                with(mealsGraphModule) {
                    mealsGraph(
                        navController = navController,
                        navigateToSearchRemoteMeal = {},
                    )
                }
            }
        }
    }

    @Test
    fun `Create a custom save and view it`() {
        val mealName = "ajsddff"
        val mealNutrition: FoodNutrition = NutritionSamples.filledNutritionA
        composeRule
            .onNodeWithText(text = mealName)
            .assertDoesNotExist()

        composeRule.runOnIdle {
            navController.toCreateMealScreen()
        }

        performDataEntry(composeRule, mealName = mealName, mealNutrition = mealNutrition)

        // save and return to ViewAllMeals Screen
        composeRule
            .onNodeWithText(text = composeRule.activity.getString(R.string.save))
            .assertIsEnabled()
            .performClick()

        // navigate to ViewMeal to view details
        composeRule
            .onNodeWithText(text = mealName)
            .assertExists()
            .assertHasClickAction()
            .performClick()

        composeRule
            .onNodeWithText(text = mealName)
            .assertExists()
            .assertIsDisplayed()
            .assertHasNoClickAction()

        assertCorrectNutrition(
            composeRule = composeRule,
            mealName = mealName,
            mealNutrition = mealNutrition,
            formatter = formatter
        )
    }
}
