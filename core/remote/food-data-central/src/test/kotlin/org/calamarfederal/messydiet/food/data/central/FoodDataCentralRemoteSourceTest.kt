package org.calamarfederal.messydiet.food.data.central

import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.food.data.central.di.testDi
import org.calamarfederal.messydiet.food.data.central.model.getValueOrNull
import org.calamarfederal.messydiet.food.data.central.model.isSuccess
import org.kodein.di.direct
import org.kodein.di.instance
import kotlin.test.BeforeTest
import kotlin.test.Test

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
                    withUpc(SpriteTest.spriteUpc)
                }
            }
        }

        val searchResult = result.getValueOrNull()!!

        assert(searchResult.totalHits == 1)
    }

    @Test
    fun `Sprite Food test`() {
        val result = runBlocking {
            remote.getFoodByFdcId(SpriteTest.spriteFdcId)
        }

        assert(result.isSuccess())

        val foodItem = result.getValueOrNull()!!
        prettyPrint(foodItem.nutritionalInfo as Nutrition)

        val nutrition = foodItem.nutritionalInfo!!
        assert(nutrition.totalCarbohydrates.inGrams() - 10.83 <= 0.01)
    }

    @Test
    fun `Cheerios Food test`() {
        val result = runBlocking {
            remote.getFoodByFdcId(CheeriosTestA.cheeriosFdcId)
        }

        assert(result.isSuccess())

        val foodItem = result.getValueOrNull()!!
        prettyPrint(foodItem.nutritionalInfo as Nutrition)

        val nutrition = foodItem.nutritionalInfo!!
        prettyPrint(nutrition)
    }
}
