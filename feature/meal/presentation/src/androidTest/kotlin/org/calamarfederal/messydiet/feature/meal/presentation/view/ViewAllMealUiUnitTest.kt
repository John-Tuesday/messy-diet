package org.calamarfederal.messydiet.feature.meal.presentation.view

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performSemanticsAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.calamarfederal.messydiet.feature.meal.presentation.R

@RunWith(AndroidJUnit4::class)
class ViewAllMealUiUnitTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var testMeals: List<Meal>

    @Before
    fun setUp() {
        testMeals = listOf(
            Meal(id = 1, name = "alpha"),
            Meal(id = 2, name = "BETA"),
            Meal(id = 3, name = "chArlie"),
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
