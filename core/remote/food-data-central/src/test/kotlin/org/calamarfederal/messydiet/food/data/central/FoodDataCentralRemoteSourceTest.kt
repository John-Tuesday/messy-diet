package org.calamarfederal.messydiet.food.data.central

import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.food.data.central.di.testDi
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

internal class FoodDataCentralRemoteSourceTest {
    private lateinit var remote: FoodDataCentralRemoteSource

    @BeforeTest
    fun setUp() {
        remote = testDi.direct.instance()
    }

    @Test
    fun `Find SpriteTest by upc gtin`() {
        val result = runBlocking {
            remote.searchFood {
                query = searchQuery {
                    withUpc(FoodItemExpect.SpriteTest.spriteUpc)
                }
            }
        }

        val searchResult = result.getValueOrNull()!!

        assertEquals(1, searchResult.totalHits)
        assertEquals(1, searchResult.currentPage)
        assertEquals(1, searchResult.totalPages)
        assertEquals(1, searchResult.foodItems.size)

        val foodItem = searchResult.foodItems.single()

        assertEquals(FoodItemExpect.SpriteTest.spriteFdcId, foodItem.fdcId)
        assertEquals(FoodItemExpect.SpriteTest.spriteSearchDescription, foodItem.description)
    }

    @Test
    fun `Sprite Food test`() {
        val result = runBlocking {
            remote.getFoodByFdcId(
                fdcId = FoodItemExpect.SpriteTest.spriteFdcId,
            )
        }
        assert(result.isSuccess())

        val foodItem = result.getValueOrNull()!!
        assertNotNull(foodItem.nutritionalInfo)
        prettyPrint(foodItem.nutritionalInfo!!)
        assertEquals(FoodItemExpect.SpriteTest.spriteFdcId, foodItem.fdcId)
        assertEquals(FoodItemExpect.SpriteTest.spriteSearchDescription, foodItem.description)
        assertAboutEqual(FoodItemExpect.SpriteTest.spriteNutritionPerServing, foodItem.nutritionalInfo!!)
    }

    @Test
    fun `Cheerios Food test`() {
        val result = runBlocking {
            remote.getFoodByFdcId(
                fdcId = FoodItemExpect.CheeriosTestA.cheeriosFdcId,
            )
        }
        assert(result.isSuccess())

        val foodItem = result.getValueOrNull()!!
        val nutrition = foodItem.nutritionalInfo!!
        prettyPrint(nutrition)
        assertAboutEqual(
            FoodItemExpect.CheeriosTestA.cheeriosNutritionPer100.scaleToPortion(FoodItemExpect.CheeriosTestA.cheeriosNutritionPerServing.portion),
            nutrition
        )
    }
}
