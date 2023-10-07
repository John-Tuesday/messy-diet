package org.calamarfederal.messydiet.feature.search.presentation.detail

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.john.tuesday.nutrition.FoodNutrition
import io.github.john.tuesday.nutrition.Portion
import org.calamarfederal.messydiet.feature.search.data.model.FoodDetailsStatus
import org.calamarfederal.messydiet.feature.search.data.model.FoodItemDetails
import org.calamarfederal.messydiet.feature.search.data.model.SearchRemoteError
import org.calamarfederal.messydiet.feature.search.data.model.foodId
import org.calamarfederal.messydiet.feature.search.presentation.R
import org.calamarfederal.physical.measurement.grams
import org.calamarfederal.physical.measurement.kilocalories
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.BeforeTest
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class FdcFoodItemDetailsUiUnitTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private var detailStatusState by mutableStateOf<FoodDetailsStatus?>(null)

    @BeforeTest
    fun setUp() {
        detailStatusState = null

        composeRule.setContent {
            FoodItemDetailsLayoutFromStatus(
                state = detailStatusState
            )
        }
    }

    @Test
    fun `Failed status shows message`() {
        val failMessage = "fail message"

        composeRule
            .onNodeWithText(
                composeRule.activity.getString(R.string.fetch_food_detail_failed_title)
            )
            .assertDoesNotExist()

        detailStatusState = FoodDetailsStatus.Failure(SearchRemoteError.InternalApiError(failMessage))

        composeRule
            .onNodeWithText(
                composeRule.activity.getString(R.string.fetch_food_detail_failed_title)
            )
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `Success status shows details`() {
        val detailName = "test name"

        composeRule
            .onNodeWithText(detailName)
            .assertDoesNotExist()

        detailStatusState = FoodDetailsStatus.Success(
            FoodItemDetails(
                foodId = foodId(1, 2),
                name = detailName,
                nutritionInfo = FoodNutrition(portion = Portion(1.grams), foodEnergy = 0.kilocalories)
            )
        )

        composeRule
            .onNodeWithText(detailName)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `Loading status shows progress indicator`() {
        composeRule
            .onNodeWithText(
                composeRule.activity.getString(R.string.fetch_food_detail_loading)
            )
            .assertDoesNotExist()

        detailStatusState = FoodDetailsStatus.Loading

        composeRule
            .onNodeWithText(
                composeRule.activity.getString(R.string.fetch_food_detail_loading)
            )
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `Null status shows nothing`() {
        detailStatusState = null
    }
}
