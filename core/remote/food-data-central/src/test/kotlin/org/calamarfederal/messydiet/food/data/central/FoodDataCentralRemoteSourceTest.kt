package org.calamarfederal.messydiet.food.data.central

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.food.data.central.di.FoodDataCentral
import org.calamarfederal.messydiet.food.data.central.model.getValueOrNull
import org.calamarfederal.messydiet.food.data.central.model.isSuccess
import org.junit.Before
import org.junit.Test

internal class FoodDataCentralRemoteSourceTest {
    private lateinit var remote: FoodDataCentralRemoteSource

    @Before
    fun setUp() {
        remote = FoodDataCentral.foodDataCentralRemoteSource(
            apiKey = TestApiKey,
            dispatcher = Dispatchers.Default,
        )
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
}
