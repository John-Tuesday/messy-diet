package org.calamarfederal.messydiet.food.data.central

import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.food.data.central.di.testDi
import org.calamarfederal.messydiet.food.data.central.model.getResponseOrNull
import org.calamarfederal.messydiet.food.data.central.model.getValueOrNull
import org.calamarfederal.messydiet.food.data.central.model.isSuccess
import org.calamarfederal.messydiet.test.food.data.central.FoodItemExpect
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

    @Test
    fun `Sprite Upc test`() {
        val result = runBlocking {
            repo.searchFoodWithUpcGtin(FoodItemExpect.SpriteTest.spriteUpc)
        }
        assert(result.isSuccess()) {
            println("${result.getResponseOrNull()?.message}")
        }

        val searchResultFoods = result.getValueOrNull()!!
        assertEquals(1, searchResultFoods.size)
        val searchFood = searchResultFoods.single()

        assertEquals(FoodItemExpect.SpriteTest.spriteFdcId, searchFood.fdcId)
        assertEquals(FoodItemExpect.SpriteTest.spriteSearchDescription, searchFood.description)
    }

    @Test
    fun `Sprite Food detail test`() {
        val result = runBlocking {
            repo.getFoodDetails(FoodItemExpect.SpriteTest.spriteFdcId)
        }
        assert(result.isSuccess())

        val foodItem = result.getValueOrNull()!!

        assertEquals(FoodItemExpect.SpriteTest.spriteFdcId, foodItem.fdcId)
        assertNotNull(foodItem.nutritionalInfo)
        val nutritionInfo = foodItem.nutritionalInfo!!
        prettyPrint(nutritionInfo)
        assertAboutEqual(FoodItemExpect.SpriteTest.spriteNutritionPerServing, nutritionInfo)
    }

    @Test
    fun `Sprite Food search then detail test`() {
        val searchResult = runBlocking {
            repo.searchFoodWithUpcGtin(FoodItemExpect.SpriteTest.spriteUpc)
        }

        assert(searchResult.isSuccess()) {
            println("${searchResult.getResponseOrNull()?.message}")
        }

        val searchItems = searchResult.getValueOrNull()!!
        assertEquals(1, searchItems.size)
        val searchFoodItem = searchItems.single()

        val detailResult = runBlocking {
            repo.getFoodDetails(
                fdcId = searchFoodItem.fdcId
            )
        }
        assert(detailResult.isSuccess())

        val foodItem = detailResult.getValueOrNull()!!

        assertEquals(FoodItemExpect.SpriteTest.spriteFdcId, foodItem.fdcId)
        assertNotNull(foodItem.nutritionalInfo)

        val nutritionInfo = foodItem.nutritionalInfo!!

        prettyPrint(nutritionInfo)
        assertAboutEqual(FoodItemExpect.SpriteTest.spriteNutritionPerServing, nutritionInfo)
    }

    @Test
    fun `Cheerios Food detail test`() {
        val result = runBlocking {
            repo.getFoodDetails(FoodItemExpect.CheeriosTestA.cheeriosFdcId)
        }
        assert(result.isSuccess())

        val foodItem = result.getValueOrNull()!!

        assertEquals(foodItem.fdcId, FoodItemExpect.CheeriosTestA.cheeriosFdcId)
        assertNotNull(foodItem.nutritionalInfo)

        val nutritionInfo = foodItem.nutritionalInfo!!
        prettyPrint(nutritionInfo)

        val expectedNutrition =
            FoodItemExpect.CheeriosTestA.cheeriosNutritionPer100.scaleToPortion(FoodItemExpect.CheeriosTestA.cheeriosNutritionPerServing.portion)
        prettyPrint(expectedNutrition)
        assertAboutEqual(expectedNutrition, nutritionInfo, 10.0)
    }
}
