package org.calamarfederal.messydiet.food.data.central

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.food.data.central.di.API_KEY_TAG
import org.calamarfederal.messydiet.food.data.central.di.NETWORK_DISPATCHER_TAG
import org.calamarfederal.messydiet.food.data.central.di.testDi
import org.calamarfederal.messydiet.food.data.central.model.getValueOrNull
import org.calamarfederal.messydiet.food.data.central.model.isSuccess
import org.calamarfederal.messydiet.food.data.central.remote.FoodDataCentralApi
import org.calamarfederal.messydiet.food.data.central.remote.schema.BrandedFoodItemSchema
import org.kodein.di.direct
import org.kodein.di.instance
import retrofit2.Response
import kotlin.test.*

internal class FoodDataCentralRemoteSourceUnitTest {
    private lateinit var fdcApiMock: FoodDataCentralApi
    private lateinit var remote: FoodDataCentralRemoteSource

    @BeforeTest
    fun setUp() {
        fdcApiMock = mockk<FoodDataCentralApi>()
        remote = FoodDataCentralRemoteSourceImplementation(
            networkDispatcher = testDi.direct.instance(tag = NETWORK_DISPATCHER_TAG),
            apiKey = testDi.direct.instance(tag = API_KEY_TAG),
            fdcApi = fdcApiMock,
        )
    }

    @AfterTest
    fun tearDown() {
        confirmVerified(fdcApiMock)
        checkUnnecessaryStub(fdcApiMock)
    }

    private fun getFoodBrandedTest(expectCase: FoodItemExpectCase, brandedFoodItemSchema: BrandedFoodItemSchema) {
        coEvery {
            fdcApiMock.getFoodWithFdcIdBranded(
                apiKey = any(),
                fdcId = expectCase.fdcIdString,
                format = null,
                nutrients = null,
            )
        } returns mockk { every { execute() } returns Response.success(brandedFoodItemSchema) }

        val result = runBlocking { remote.getFoodByFdcId(fdcId = expectCase.fdcId) }
        assertTrue { result.isSuccess() }
        val foodItem = result.getValueOrNull()!!
        assertNotNull(foodItem.nutritionalInfo)
        assertEquals(expectCase.fdcId, foodItem.fdcId)
        assertEquals(expectCase.searchDescription, foodItem.description)
        assertEquals(expectCase.foodNutrition, foodItem.nutritionalInfo)

        coVerify(exactly = 1) {
            fdcApiMock.getFoodWithFdcIdBranded(any(), expectCase.fdcIdString, null, null)
        }
    }

    @Test
    fun `Get Sprite test`() {
        getFoodBrandedTest(
            expectCase = FoodItemExpect.SpriteTest,
            brandedFoodItemSchema = FoodItemExpect.SpriteTest.getFoodResultSchema
        )
    }
}
