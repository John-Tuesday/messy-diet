package org.calamarfederal.messydiet.feature.search.data

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.feature.search.data.model.FoodDetailsStatus
import org.calamarfederal.messydiet.feature.search.data.model.foodId
import org.calamarfederal.messydiet.food.data.central.di.FoodDataCentral
import org.calamarfederal.messydiet.test.food.data.central.FoodItemExpect
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.*
import org.calamarfederal.messydiet.test.measure.assertEquals as assertMeasureEquals

@RunWith(AndroidJUnit4::class)
class FoodDetailsRepositoryUnitTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var detailsRepository: FoodDetailsRepository

    @BeforeTest
    fun setUp() {
        val remoteRepository = FoodDataCentral.repository(
            networkDispatcher = Dispatchers.Default,
            apiKey = "DEMO_KEY",
        )
        detailsRepository = FoodDetailsRepositoryImplementation(fdcRepo = remoteRepository)
    }

    @Test
    fun `Valid sprite test`() {
        val resultFlow = detailsRepository.foodDetails(
            foodId(
                id = FoodItemExpect.SpriteTest.spriteFdcIdInt,
                type = 2,
            )
        )

        runBlocking {
            var count = 0

            resultFlow.collectIndexed { index, value ->
                count += 1

                when (index) {
                    0 -> assertIs<FoodDetailsStatus.Loading>(value)
                    1 -> {
                        assertIs<FoodDetailsStatus.Success>(value)

                        assertEquals(
                            FoodItemExpect.SpriteTest.spriteName,
                            value.results.name,
                        )
                        assertEquals(
                            FoodItemExpect.SpriteTest.spriteFdcIdInt,
                            value.results.foodId.id,
                        )
                        assertMeasureEquals(
                            FoodItemExpect.SpriteTest.spriteNutritionPerServing,
                            value.results.nutritionInfo,
                        )
                    }

                    else -> fail("Only expected 2 emissions")
                }
            }

            assertEquals(2, count)
        }
    }

    @Test
    fun `Invalid id causes failure`() {
        val resultFlow = detailsRepository.foodDetails(
            foodId(
                id = 1,
                type = 2,
            )
        )

        runBlocking {
            var count = 0

            resultFlow.collectIndexed { index, value ->
                count += 1

                when (index) {
                    0 -> assertIs<FoodDetailsStatus.Loading>(value)
                    1 -> {
                        assertIs<FoodDetailsStatus.Failure>(value)

                        println(value.message)
                    }

                    else -> fail("Only expected 2 emissions")
                }
            }

            assertEquals(2, count)
        }
    }
}
