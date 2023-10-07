package org.calamarfederal.messydiet.food.data.central

import io.github.john.tuesday.nutrition.FoodNutrition
import io.github.john.tuesday.nutrition.prettyPrint
import io.github.john.tuesday.nutrition.scaleToPortion
import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.food.data.central.di.testDi
import org.calamarfederal.messydiet.food.data.central.model.*
import org.calamarfederal.messydiet.test.food.data.central.FoodItemExpect
import org.calamarfederal.messydiet.test.food.data.central.FoodItemExpectCase
import org.kodein.di.direct
import org.kodein.di.instance
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class FoodDataCentralRemoteSourceTest {
    private lateinit var remote: FoodDataCentralRemoteSource

    @BeforeTest
    fun setUp() {
        remote = testDi.direct.instance()
    }

    fun testSearch(expectCase: FoodItemExpectCase) {
        val result = runBlocking {
            remote.searchFood {
                query = searchQuery {
                    withUpc(expectCase.gtinUpc)
                }
            }
        }

        val searchResult = result.getValueOrNull()!!

        assertEquals(1, searchResult.totalHits)
        assertEquals(1, searchResult.currentPage)
        assertEquals(1, searchResult.totalPages)
        assertEquals(1, searchResult.foodItems.size)

        val foodItem = searchResult.foodItems.single()

        assertEquals(expectCase.fdcId, foodItem.fdcId)
        assertEquals(expectCase.searchDescription, foodItem.description)
    }

    fun testGetFood(expectCase: FoodItemExpectCase, expectNutrition: FoodNutrition = expectCase.nutritionPerServing) {
        val result = runBlocking {
            remote.getFoodByFdcId(
                fdcId = expectCase.fdcId,
            )
        }
        assert(result.isSuccess())

        val foodItem = result.getValueOrNull()!!
        assertNotNull(foodItem.nutritionalInfo)
        prettyPrint(foodItem.nutritionalInfo!!)
        assertEquals(expectCase.fdcId, foodItem.fdcId)
        assertEquals(expectCase.searchDescription, foodItem.description)
//        assertAboutEqual(expectNutrition, foodItem.nutritionalInfo!!)
        assertEquals(expectNutrition, foodItem.nutritionalInfo!!)
    }

    @Test
    fun `Search GTIN UPC for SpriteTest`() {
        testSearch(FoodItemExpect.SpriteTest)
    }

    @Test
    fun `Search GTIN UPC for CheeriosTestA`() {
        testSearch(FoodItemExpect.CheeriosTestA)
    }

    @Test
    fun `Search GTIN UPC for TridentGumTest`() {
        testSearch(FoodItemExpect.TridentGumTest)
    }

    @Test
    fun `Get food details test for SpriteTest`() {
        testGetFood(FoodItemExpect.SpriteTest)
    }

    @Test
    fun `Get food details test for CheeriosTestA`() {
        testGetFood(
            FoodItemExpect.CheeriosTestA,
            FoodItemExpect.CheeriosTestA.nutritionPer100.scaleToPortion(FoodItemExpect.CheeriosTestA.nutritionPerServing.portion),
        )
    }

    @Test
    fun `Get food details test for TridentGumTest`() {
        testGetFood(FoodItemExpect.TridentGumTest, FoodItemExpect.TridentGumTest.nutritionPer100)
    }

    fun testSerialization(fdcId: FDCId): FDCFoodItem {
        val result = runBlocking {
            remote.getFoodByFdcId(
                fdcId = fdcId,
            )
        }
        assert(result.isSuccess())

        val foodItem = result.getValueOrNull()!!
        assertNotNull(foodItem.nutritionalInfo)
        prettyPrint(foodItem.nutritionalInfo!!)
        assertEquals(fdcId, foodItem.fdcId)

        return foodItem
    }

    @Test
    fun `Test serialization of Butterspray`() {
        val butterSprayUpc = "078742229423"
        val butterSprayFdcId = BrandedFDCId(1926907)

        testSerialization(butterSprayFdcId)
    }

    @Test
    fun `Test serialization of `() {
        // I forgot
        val fdcId = BrandedFDCId(2577315)

        testSerialization(fdcId)
    }

    @Test
    fun `Test serialization of Corn Dog Box`() {
        val upcGtin = "071068160241"
        val fdcId = BrandedFDCId(2502945)

        testSerialization(fdcId)
    }
}
