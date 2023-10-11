package org.calamarfederal.messydiet.food.data.central

import io.github.john.tuesday.nutrition.FoodNutrition
import io.github.john.tuesday.nutrition.prettyPrint
import io.github.john.tuesday.nutrition.scaleToPortion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.food.data.central.di.FoodDataCentral
import org.calamarfederal.messydiet.food.data.central.model.getResponseOrNull
import org.calamarfederal.messydiet.food.data.central.model.getValueOrNull
import org.calamarfederal.messydiet.food.data.central.model.isSuccess
import org.calamarfederal.messydiet.remote.food.data.central.test.FoodItemExpect
import org.calamarfederal.messydiet.remote.food.data.central.test.FoodItemExpectCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class FoodDataCentralRepositoryRemoteApiTest {
    private lateinit var repo: FoodDataCentralRepository

    @BeforeTest
    fun setUp() {
        repo = FoodDataCentral.repository(
            networkDispatcher = Dispatchers.Main,
            apiKey = FoodDataCentral.DemoKey,
        )
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
        expectNutrition: FoodNutrition = expectCase.nutritionPerServing,
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
        assertEquals(expectNutrition, nutritionInfo)
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
