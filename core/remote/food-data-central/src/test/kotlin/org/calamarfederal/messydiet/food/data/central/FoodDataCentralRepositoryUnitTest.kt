package org.calamarfederal.messydiet.food.data.central

import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.diet_data.model.NutritionInfo
import org.calamarfederal.messydiet.food.data.central.di.testDi
import org.calamarfederal.messydiet.food.data.central.model.getResponseOrNull
import org.calamarfederal.messydiet.food.data.central.model.getValueOrNull
import org.calamarfederal.messydiet.food.data.central.model.isSuccess
import org.calamarfederal.messydiet.test.food.data.central.FoodItemExpect
import org.calamarfederal.messydiet.test.food.data.central.FoodItemExpectCase
import org.calamarfederal.messydiet.test.measure.prettyPrint
import org.kodein.di.direct
import org.kodein.di.instance
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.calamarfederal.messydiet.test.measure.assertEquals as assertAboutEqual

internal class FoodDataCentralRepositoryUnitTest {
    private lateinit var repo: FoodDataCentralRepository

    @BeforeTest
    fun setUp() {
        repo = testDi.direct.instance()
    }

    fun testSearchByUpcGtin(expectCase: FoodItemExpectCase) {
        val result = runBlocking {
            repo.searchFoodWithUpcGtin(expectCase.gtinUpc)
        }
        assert(result.isSuccess()) {
            println("${result.getResponseOrNull()?.message}")
        }

        val searchResultFoods = result.getValueOrNull()!!
        assertEquals(1, searchResultFoods.size)
        val searchFood = searchResultFoods.single()

        assertEquals(expectCase.fdcId, searchFood.fdcId)
        assertEquals(expectCase.searchDescription, searchFood.description)
    }

    fun testGetDetails(
        expectCase: FoodItemExpectCase,
        expectNutrition: NutritionInfo = expectCase.nutritionPerServing,
    ) {
        val result = runBlocking {
            repo.getFoodDetails(expectCase.fdcId)
        }
        assert(result.isSuccess())

        val foodItem = result.getValueOrNull()!!

        assertEquals(expectCase.fdcId, foodItem.fdcId)
        assertNotNull(foodItem.nutritionalInfo)
        val nutritionInfo = foodItem.nutritionalInfo!!
        prettyPrint(nutritionInfo)
        assertAboutEqual(expectNutrition, nutritionInfo)
    }

    @Test
    fun `Search by UPC GTIN SpriteTest`() {
        testSearchByUpcGtin(FoodItemExpect.SpriteTest)
    }

    @Test
    fun `Get food details for SpriteTest`() {
        testGetDetails(FoodItemExpect.SpriteTest)
    }

    @Test
    fun `Get food details for CheeriosTestA`() {
        testGetDetails(
            FoodItemExpect.CheeriosTestA,
            FoodItemExpect.CheeriosTestA.nutritionPer100.scaleToPortion(FoodItemExpect.CheeriosTestA.nutritionPerServing.portion),
        )
    }
}
