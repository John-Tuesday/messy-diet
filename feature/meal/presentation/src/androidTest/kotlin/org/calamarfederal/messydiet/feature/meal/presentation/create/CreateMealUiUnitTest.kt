package org.calamarfederal.messydiet.feature.meal.presentation.create

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
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
        val fatGroupContentDescription = composeRule.stringResource(R.string.expand_fat_group)
        val optionalFatChild = composeRule.stringResource(M.string.omega3_fat)

        composeRule
            .onNodeWithText(optionalFatChild)
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription(fatGroupContentDescription)
            .assertExists()
            .assertHasClickAction()
            .performClick()

        composeRule
            .onNodeWithText(optionalFatChild)
            .assertExists()

        composeRule
            .onNodeWithContentDescription(fatGroupContentDescription)
            .performScrollTo()
            .performClick()

        composeRule
            .onNodeWithText(optionalFatChild)
            .assertIsNotDisplayed()
    }
}
