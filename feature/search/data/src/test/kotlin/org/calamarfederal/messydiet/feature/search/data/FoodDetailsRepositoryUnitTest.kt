package org.calamarfederal.messydiet.feature.search.data

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.feature.search.data.model.FoodDetailsStatus
import org.calamarfederal.messydiet.feature.search.data.model.FoodIdDummy
import org.calamarfederal.messydiet.feature.search.data.model.SearchRemoteError
import org.calamarfederal.messydiet.feature.search.data.model.foodId
import org.calamarfederal.messydiet.feature.search.data.repository.FoodDetailsRepository
import org.calamarfederal.messydiet.feature.search.data.repository.FoodDetailsRepositoryImplementation
import org.calamarfederal.messydiet.food.data.central.FoodDataCentralRepository
import org.calamarfederal.messydiet.food.data.central.model.FoodDataCentralError
import org.calamarfederal.messydiet.food.data.central.model.ResultResponse
import org.calamarfederal.messydiet.remote.food.data.central.test.FoodItemExpect
import org.calamarfederal.messydiet.remote.food.data.central.test.FoodItemExpectCase
import org.junit.Rule
import org.junit.experimental.theories.DataPoints
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith
import kotlin.test.*

@RunWith(Theories::class)
class FoodDetailsRepositoryUnitTest {
    companion object {
        @JvmStatic
        @DataPoints
        fun foodItemExpectData(): Iterable<FoodItemExpectCase> = listOf(
            FoodItemExpect.SpriteTest,
            FoodItemExpect.CheeriosTestA,
        )

        @JvmStatic
        @DataPoints
        fun errorData(): Iterable<Pair<FoodDataCentralError, SearchRemoteError>> {
            val message = "abcdEFGHJ"
            val gibberish = "alsdkfja;"
            return sequenceOf(
                FoodDataCentralError.NotFoundError(message = message) to SearchRemoteError.NotFoundError(message),
                FoodDataCentralError.OverRateLimitError(message = message) to SearchRemoteError.OverRateLimitError(
                    message = message,
                ),
                FoodDataCentralError.NetworkError(message = message, code = 0) to SearchRemoteError.UnknownNetworkError(
                    message = message,
                    code = 0
                ),
                FoodDataCentralError.UnrecognizedWeightUnitFormat(message = message, input = gibberish).let {
                    it to SearchRemoteError.InternalApiError(message = message, cause = it)
                },
                FoodDataCentralError.UnrecognizedNutrientNumber(message = message, number = gibberish).let {
                    it to SearchRemoteError.InternalApiError(message = message, cause = it)
                },
            ).asIterable()
        }
    }

    @get:Rule
    val mockkRule = MockKRule(this)

    private lateinit var detailsRepository: FoodDetailsRepository

    @MockK
    private lateinit var remoteRepositoryMock: FoodDataCentralRepository

    @BeforeTest
    fun setUp() {
        detailsRepository = FoodDetailsRepositoryImplementation(fdcRepo = remoteRepositoryMock)
    }

    @AfterTest
    fun tearDown() {
        confirmVerified(remoteRepositoryMock)
        checkUnnecessaryStub(remoteRepositoryMock)
    }

    @Theory
    fun `Food details emits the correct Statuses and food item`(expectCase: FoodItemExpectCase) {
        coEvery { remoteRepositoryMock.getFoodDetails(expectCase.fdcId) } returns ResultResponse.Success(
            mockk {
                every { fdcId } returns expectCase.fdcId
                every { description } returns expectCase.searchDescription
                every { nutritionalInfo } returns expectCase.nutritionPerServing
            }
        )

        val resultFlow = detailsRepository.foodDetails(
            foodId(
                id = expectCase.fdcIdInt,
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
                            expectCase.name,
                            value.results.name,
                        )
                        assertEquals(
                            expectCase.fdcIdInt,
                            value.results.foodId.id,
                        )
                        assertEquals(
                            expectCase.nutritionPerServing,
                            value.results.nutritionInfo,
                        )
                    }

                    else -> fail("Only expected 2 emissions")
                }
            }

            assertEquals(2, count)
        }

        coVerify(exactly = 1) { remoteRepositoryMock.getFoodDetails(eq(expectCase.fdcId)) }
    }

    @Theory
    fun `Food details emits correct statuses and error`(expectErrors: Pair<FoodDataCentralError, SearchRemoteError>) {
        coEvery { remoteRepositoryMock.getFoodDetails(any()) } returns ResultResponse.Failure(expectErrors.first)

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
                        assertEquals(expectErrors.second, value.remoteError)
                    }

                    else -> fail("Only expected 2 emissions")
                }
            }

            assertEquals(2, count)
        }

        coVerify(exactly = 1) { remoteRepositoryMock.getFoodDetails(any()) }
    }

    @Test
    fun `Dummy id will cause an error but not call remote`() {
        val resultFlow = detailsRepository.foodDetails(FoodIdDummy)

        runBlocking {
            var count = 0

            resultFlow.collectIndexed { index, value ->
                count += 1

                when (index) {
                    0 -> assertIs<FoodDetailsStatus.Loading>(value)
                    1 -> {
                        assertIs<FoodDetailsStatus.Failure>(value)
                        val actualError = value.remoteError
                        assertIs<SearchRemoteError.InvalidFoodIdError>(actualError)
                        assertEquals(FoodIdDummy.id, actualError.id)
                        assertEquals(FoodIdDummy.type, actualError.type)
                    }

                    else -> fail("Only expected 2 emissions")
                }
            }

            assertEquals(2, count)
        }

        coVerify(exactly = 0) { remoteRepositoryMock wasNot Called }
    }
}
