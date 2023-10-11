package org.calamarfederal.messydiet.feature.search.data

import io.github.john.tuesday.nutrition.Portion
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.feature.search.data.model.SearchStatus
import org.calamarfederal.messydiet.food.data.central.FoodDataCentralRepository
import org.calamarfederal.messydiet.food.data.central.model.FDCNutritionInfo
import org.calamarfederal.messydiet.food.data.central.model.ResultResponse
import org.calamarfederal.messydiet.remote.food.data.central.test.FoodItemExpect
import org.calamarfederal.messydiet.remote.food.data.central.test.FoodItemExpectCase
import org.calamarfederal.physical.measurement.grams
import org.calamarfederal.physical.measurement.kilocalories
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import kotlin.test.*

@RunWith(Parameterized::class)
class FoodSearchRepositoryUnitTest(
    private val expectCase: FoodItemExpectCase,
) {
    companion object {
        @JvmStatic
        @Parameters(name = "{index}: FoodItemExpectCase({1})")
        fun foodItemExpectData(): Iterable<FoodItemExpectCase> = sequenceOf(
            FoodItemExpect.SpriteTest,
            FoodItemExpect.CheeriosTestA,
        ).asIterable()
    }

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var searchRepository: FoodSearchRepository

    @MockK
    private lateinit var remoteRepositoryMock: FoodDataCentralRepository

    @BeforeTest
    fun setUp() {
        searchRepository = FoodSearchRepositoryImplementation(fdcRepo = remoteRepositoryMock)
    }

    @AfterTest
    fun tearDown() {
        confirmVerified(remoteRepositoryMock)
        checkUnnecessaryStub(remoteRepositoryMock)
    }

    @Test
    fun `Search with valid GTIN`() {
        coEvery { remoteRepositoryMock.searchFoodWithUpcGtin(expectCase.gtinUpc) } returns ResultResponse.Success(
            listOf(
                mockk {
                    every { fdcId } returns expectCase.fdcId
                    every { description } returns expectCase.searchDescription
                    every { nutritionalInfo } returns FDCNutritionInfo(
                        portion = Portion(0.grams),
                        foodEnergy = 0.kilocalories
                    )
                }
            )
        )

        val searchFlow = searchRepository.searchWithUpcGtin(expectCase.gtinUpc)

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
                            assertEquals(expectCase.name, foodItem.name)
                            assertEquals(expectCase.fdcIdInt, foodItem.id)
                            assertEquals(
                                FDCNutritionInfo(portion = Portion(0.grams), foodEnergy = 0.kilocalories),
                                foodItem.nutritionInfo
                            )
                        }

                        else -> fail("There should only be 2 emissions")
                    }
                }

            assertEquals(2, count)
        }

        coVerify(exactly = 1) { remoteRepositoryMock.searchFoodWithUpcGtin(expectCase.gtinUpc) }
    }
}
