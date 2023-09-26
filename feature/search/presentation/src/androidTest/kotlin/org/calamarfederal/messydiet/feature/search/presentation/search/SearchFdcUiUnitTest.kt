package org.calamarfederal.messydiet.feature.search.presentation.search

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.feature.search.data.model.*
import org.calamarfederal.messydiet.feature.search.presentation.R
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class SearchFdcUiUnitTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private var queryInputState by mutableStateOf("")
    private var foodDetailStatusState by mutableStateOf<FoodDetailsStatus?>(null)
    private var searchStatusState by mutableStateOf<SearchStatus?>(null)

    @BeforeTest
    fun setUp() {
        queryInputState = ""
        foodDetailStatusState = null
        searchStatusState = null

        composeRule.setContent {
            SearchFdcScreen(
                queryInput = queryInputState,
                onQueryChange = {},
                onSubmitQuery = {},
                getFoodItemDetails = {},
                saveFoodItemDetails = {},
                useBarcodeScanner = {},
                foodItemDetailStatus = foodDetailStatusState,
                searchStatus = searchStatusState,
                onNavigateUp = {},
            )
        }

    }

    private val searchBarNode
        get() = composeRule
            .onNodeWithText(
                queryInputState.ifEmpty { composeRule.activity.getString(R.string.search_searchbar_hint) }
            )

    @Test
    fun `Query text is shown in the search bar`() {
        val testQueryText = "ajajalksdjf asldjf alsdjkf "

        composeRule
            .onNodeWithText(testQueryText)
            .assertDoesNotExist()

        queryInputState = testQueryText

        composeRule
            .onNodeWithText(testQueryText)
            .assertExists()
            .assertIsDisplayed()

        searchBarNode
            .assertTextContains(testQueryText)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `Search bar has placeholder hint`() {
        val hintText = composeRule.activity.getString(R.string.search_searchbar_hint)

        composeRule
            .onNodeWithText(hintText)
            .assertExists()
            .assertIsDisplayed()

        searchBarNode
            .assertTextContains(hintText)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun `Search bar content`() {
        searchBarNode
            .performClick()

        searchStatusState = null

        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.search_nonexistent))
            .assertExists()
            .assertIsDisplayed()

        searchStatusState = SearchStatus.Loading

        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.search_loading))
            .assertExists()
            .assertIsDisplayed()

        val failMessage = "Fail message"
        searchStatusState = SearchStatus.Failure(SearchRemoteError.InternalApiError(failMessage))

        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.search_failed))
            .assertExists()
            .assertIsDisplayed()

        val resultNames = (0..15).map {
            val charset = (('a'..'z') + ('A'..'Z') + ('0'..'9'))
            val length = Random.nextInt(4, 25)
            buildString {
                append("$it")
                for (i in 0..<length) {
                    append(charset.random())
                }
            }
        }
        searchStatusState = SearchStatus.Success(
            resultNames.mapIndexed { index, name ->
                SearchResultFoodItem(foodId = foodId(id = index, type = 2), name = name, nutritionInfo = Nutrition())
            }
        )

        for ((index, name) in resultNames.withIndex()) {
            composeRule
                .onNodeWithText(name)
                .assertExists()
                .onAncestors()
                .filterToOne(hasScrollToKeyAction())
                .performScrollToKey(index)
                .onChildren()
                .filterToOne(hasText(name))
        }
    }
}
