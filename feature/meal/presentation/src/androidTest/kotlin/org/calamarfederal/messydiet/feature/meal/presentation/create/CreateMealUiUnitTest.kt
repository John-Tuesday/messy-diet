package org.calamarfederal.messydiet.feature.meal.presentation.create

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.calamarfederal.messydiet.feature.meal.presentation.R

private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes id: Int): String {
    return activity.getString(id)
}

class CreateMealUiUnitTest {
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
    }

    @Test
    fun `Fat group expands and hides on arrow button click`() {
        composeRule
            .onNodeWithContentDescription(
                composeRule.stringResource(R.string.expand_fat_group),
//                useUnmergedTree = true,
            ).assertExists()
    }
}
