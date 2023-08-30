package org.calamarfederal.messydiet.feature.meal.presentation.view

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class ViewMealUiUnitTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private var testMeal by mutableStateOf(Meal())

    @Before
    fun setUp() {
        testMeal = Meal(id = 1, name = "TestName")

        composeRule.setContent {
            ViewMealScreenLayout(
                meal = testMeal,
                onNavigateUp = {},
            )
        }
    }

    @Test
    fun `Meal name is shown`() {
        composeRule
            .onNodeWithText(testMeal.name)
            .assertExists()
            .assertIsDisplayed()
    }
}