package org.calamarfederal.messydiet.feature.meal.presentation.create

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.calamarfederal.messydiet.feature.meal.presentation.R
import org.calamarfederal.messydiet.measure.R as M
import org.junit.runner.RunWith

private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes id: Int): String {
    return activity.getString(id)
}

@RunWith(AndroidJUnit4::class)
internal class CreateMealUiUnitTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var uiState: CreateMealUiState

    @Before
    fun setUp() {
        uiState = CreateMealUiState()

        composeRule.setContent {
            CreateMealLayout(
                state = uiState,
                enableSave = false,
            )
        }
    }

    private val fatExpandButton
        get() = composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.expand_fat_group))
    private val carbohydrateExpandButton
        get() = composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.expand_carbohydrate_group))

    private val totalCarbohydratesTextField
        get() = composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.meal_total_carbohydrates_label))

    private val totalFatTextField
        get() = composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.meal_total_fats_label))

    private val proteinTextField
        get() = composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.meal_protein_label))

    private val cholesterolTextField
        get() = composeRule
            .onNodeWithText(composeRule.activity.getString(M.string.cholesterol))

    @Test
    fun `Name Input correctly reflects input`() {
        val testName = "new name test"

        composeRule.onNodeWithText(testName).assertDoesNotExist()
        uiState.nameInput = testName
        composeRule.onNodeWithText(testName).assertExists()

        val testNameAlt = "abcd abcd 12"
        composeRule.onNodeWithText(testNameAlt).assertDoesNotExist()
        composeRule.onNodeWithText(testName).performTextReplacement(testNameAlt)
        composeRule.onNodeWithText(testNameAlt).assertExists()
    }

    @Test
    fun `Fat group expands and hides on arrow button click`() {
        val optionalFatChild = composeRule.stringResource(M.string.omega3_fat)

        composeRule
            .onNodeWithText(optionalFatChild)
            .assertDoesNotExist()

        fatExpandButton
            .performClick()

        composeRule
            .onNodeWithText(optionalFatChild)
            .assertExists()

        fatExpandButton
            .performScrollTo()
            .performClick()

        composeRule
            .onNodeWithText(optionalFatChild)
            .assertDoesNotExist()
    }

    @Test
    fun `Fat group always shows non empty optional fats`() {
        val optionalFatChild = composeRule.stringResource(M.string.omega3_fat)

        uiState.omega3Input.input = "5"

        composeRule
            .onNodeWithText(optionalFatChild)
            .assertExists()
            .performScrollTo()
            .assertIsDisplayed()

        fatExpandButton
            .assertExists()
            .assertHasClickAction()
            .performClick()

        composeRule
            .onNodeWithText(optionalFatChild)
            .assertExists()
            .performScrollTo()
            .assertIsDisplayed()

        fatExpandButton
            .performScrollTo()
            .performClick()

        composeRule
            .onNodeWithText(optionalFatChild)
            .assertExists()
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun `Carbohydrate group expands and hides on arrow button click`() {
        val optionalCarbohydrateChild = composeRule.stringResource(M.string.sugar)

        composeRule
            .onNodeWithText(optionalCarbohydrateChild)
            .assertDoesNotExist()

        carbohydrateExpandButton
            .assertExists()
            .assertHasClickAction()
            .performClick()

        composeRule
            .onNodeWithText(optionalCarbohydrateChild)
            .assertExists()

        carbohydrateExpandButton
            .performScrollTo()
            .performClick()

        composeRule
            .onNodeWithText(optionalCarbohydrateChild)
            .assertDoesNotExist()
    }

    @Test
    fun `Carbohydrate group always shows non empty optional child`() {
        val optionalCarbohydrateChild = composeRule.stringResource(M.string.sugar)

        uiState.sugarInput.input = "5"

        composeRule
            .onNodeWithText(optionalCarbohydrateChild)
            .assertExists()
            .performScrollTo()
            .assertIsDisplayed()

        carbohydrateExpandButton
            .assertExists()
            .assertHasClickAction()
            .performClick()

        composeRule
            .onNodeWithText(optionalCarbohydrateChild)
            .assertExists()
            .performScrollTo()
            .assertIsDisplayed()

        carbohydrateExpandButton
            .performScrollTo()
            .performClick()

        composeRule
            .onNodeWithText(optionalCarbohydrateChild)
            .assertExists()
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun `When focused text field is out of view the app does not crash`() {
        totalCarbohydratesTextField
            .performScrollTo()
            .performTextReplacement("1")

        fatExpandButton
            .performClick()

        carbohydrateExpandButton
            .performScrollTo()
            .performClick()

        cholesterolTextField
            .performScrollTo()
            .performSemanticsAction(SemanticsActions.RequestFocus)
            .assertIsFocused()

        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.meal_name_placeholder))
            .performScrollTo()

        cholesterolTextField
            .assertIsFocused()
            .assertIsNotDisplayed()
    }

    @Test
    fun `Ime action goes to the next field`() {
        val nameInput = "new name"

        fatExpandButton
            .performClick()
        carbohydrateExpandButton
            .performScrollTo()
            .performClick()

        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.meal_name_placeholder))
            .performScrollTo()
            .performTextReplacement(nameInput)

        composeRule
            .onNodeWithText(nameInput)
            .performImeAction()

        for (index in 1..16) {
            val inputString = "1"
            composeRule
                .onNode(isFocused())
                .assertIsDisplayed()
                .performTextReplacement(inputString)
            composeRule
                .onNode(isFocused())
                .assertTextContains(inputString)
                .performImeAction()
        }
    }

    @Test
    fun `Enter data in every field`() {

        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.meal_name_placeholder))
            .performScrollTo()
            .performTextReplacement("TestTest TestTest")

        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.serving_size))
            .performScrollTo()
            .performTextReplacement("1")

        proteinTextField
            .performScrollTo()
            .performTextReplacement("1")

        totalFatTextField
            .performScrollTo()
            .performTextReplacement("1")

        totalCarbohydratesTextField
            .performScrollTo()
            .performTextReplacement("1")

        fatExpandButton
            .performScrollTo()
            .performClick()

        composeRule
            .onNodeWithText(composeRule.activity.getString(M.string.omega3_fat))
            .performScrollTo()
            .performTextReplacement("1")

        composeRule
            .onNodeWithText(composeRule.activity.getString(M.string.omega6_fat))
            .performScrollTo()
            .performTextReplacement("1")

        composeRule
            .onNodeWithContentDescription(composeRule.activity.getString(R.string.expand_fat_group))
            .performScrollTo()
            .performClick()

        composeRule
            .onNodeWithContentDescription(composeRule.activity.getString(R.string.expand_fat_group))
            .performScrollTo()
            .performClick()

        composeRule
            .onNodeWithText(composeRule.activity.getString(M.string.trans_fat))
            .performScrollTo()
            .performTextReplacement("1")

        composeRule
            .onNodeWithContentDescription(composeRule.activity.getString(R.string.expand_carbohydrate_group))
            .performScrollTo()
            .performClick()
    }
}
