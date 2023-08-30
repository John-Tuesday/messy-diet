package org.calamarfederal.messydiet.food.data.central

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.diet_data.model.inKilocalories
import org.calamarfederal.messydiet.food.data.central.di.FoodDataCentral
import org.calamarfederal.messydiet.food.data.central.model.getResponseOrNull
import org.calamarfederal.messydiet.food.data.central.model.getValueOrNull
import org.calamarfederal.messydiet.food.data.central.model.isSuccess
import org.junit.Before
import org.junit.Test

internal class FoodDataCentralRepositoryUnitTest {
    private lateinit var repo: FoodDataCentralRepository

    @Before
    fun setUp() {
        repo = FoodDataCentral.repository(
            networkDispatcher = Dispatchers.Default,
            apiKey = TestApiKey,
        )
    }

    @Test
    fun `Sprite Upc test`() {
        val spriteUpc = "00049000052091"

        val result = runBlocking {
            repo.searchFoodWithUpcGtin(
                searchQuery {
                    withUpc(spriteUpc)
                }
            )
        }

        assert(result.isSuccess()) {
            println("${result.getResponseOrNull()?.message}")
        }

        val v = result.getValueOrNull()!!
        println("$v")
    }

    @Test
    fun `Sprite Food detail test`() {
        val result = runBlocking {
            repo.getFoodDetails(SpriteTest.spriteFdcId)
        }
        assert(result.isSuccess())

        val foodItem = result.getValueOrNull()!!

        assert(foodItem.fdcId == SpriteTest.spriteFdcId)
        assert(foodItem.nutritionalInfo != null)

        val nutritionInfo = foodItem.nutritionalInfo!!

        prettyPrint(nutritionInfo)
        assert(nutritionInfo.foodEnergy.inKilocalories() - SpriteTest.spriteNutrition.foodEnergy.inKilocalories() <= 1)
    }
}
