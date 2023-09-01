package org.calamarfederal.messydiet.food.data.central

import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.diet_data.model.inKilocalories
import org.calamarfederal.messydiet.food.data.central.di.testDi
import org.calamarfederal.messydiet.food.data.central.model.getResponseOrNull
import org.calamarfederal.messydiet.food.data.central.model.getValueOrNull
import org.calamarfederal.messydiet.food.data.central.model.isSuccess
import org.kodein.di.direct
import org.kodein.di.instance
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class FoodDataCentralRepositoryUnitTest {
    private lateinit var repo: FoodDataCentralRepository

    @BeforeTest
    fun setUp() {
        repo = testDi.direct.instance()
    }

    @Test
    fun `Sprite Upc test`() {
        val result = runBlocking {
            repo.searchFoodWithUpcGtin(
                searchQuery {
                    withUpc(SpriteTest.spriteUpc)
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
