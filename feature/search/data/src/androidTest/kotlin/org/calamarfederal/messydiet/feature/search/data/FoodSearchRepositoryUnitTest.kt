package org.calamarfederal.messydiet.feature.search.data

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.feature.search.data.model.SearchStatus
import org.calamarfederal.messydiet.food.data.central.di.FoodDataCentral
import org.calamarfederal.messydiet.test.food.data.central.FoodItemExpect
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.*

@RunWith(AndroidJUnit4::class)
class FoodSearchRepositoryUnitTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var searchRepository: FoodSearchRepository


    @BeforeTest
    fun setUp() {
        val remoteRepository = FoodDataCentral.repository(
            networkDispatcher = Dispatchers.Default,
            apiKey = "DEMO_KEY",
        )
        searchRepository = FoodSearchRepositoryImplementation(fdcRepo = remoteRepository)
    }

    @Test
    fun `Search with valid Sprite GTIN`() {
        val gtinString = FoodItemExpect.SpriteTest.spriteUpc
        val searchFlow = searchRepository.searchWithUpcGtin(gtinString)


        runBlocking {
            var count = 0

            searchFlow
                .collectIndexed { index, value ->
                    count += 1
                    when (index) {
                        0 -> assertEquals(SearchStatus.Loading(), value)
                        1 -> {
                            assertIs<SearchStatus.Success>(value)

                            assert(value.results.size == 1) {
                                "Size is supposed to be 1\nFound: ${value.results.size}"
                                for (foodItem in value.results) {
                                    println(foodItem)
                                }
                            }

                            val foodItem = value.results.single()
                            assertEquals(FoodItemExpect.SpriteTest.spriteName, foodItem.name)
                        }

                        else -> fail("There should only be 2 emissions")
                    }
                }

            assertEquals(2, count)
        }
    }
}
