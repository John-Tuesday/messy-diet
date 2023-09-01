package org.calamarfederal.messydiet.food.data.central

import kotlinx.coroutines.runBlocking
import org.calamarfederal.messydiet.diet_data.model.*
import org.calamarfederal.messydiet.food.data.central.di.testDi
import org.calamarfederal.messydiet.food.data.central.model.getResponseOrNull
import org.calamarfederal.messydiet.food.data.central.model.getValueOrNull
import org.calamarfederal.messydiet.food.data.central.model.isSuccess
import org.calamarfederal.messydiet.measure.*
import org.kodein.di.direct
import org.kodein.di.instance
import kotlin.math.absoluteValue
import kotlin.test.*

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

        assertAboutEqual(SpriteTest.spriteNutrition, nutritionInfo)
    }

    @Test
    fun `Cheerios Food detail test`() {
        val result = runBlocking {
            repo.getFoodDetails(CheeriosTestA.cheeriosFdcId)
        }
        assert(result.isSuccess())

        val foodItem = result.getValueOrNull()!!

        assertEquals(foodItem.fdcId, CheeriosTestA.cheeriosFdcId)
        assertNotNull(foodItem.nutritionalInfo)

        val nutritionInfo = foodItem.nutritionalInfo!!
        prettyPrint(nutritionInfo)

        val expectedNutrition = CheeriosTestA.cheeriosNutritionPer100.scaleToPortion(CheeriosTestA.cheeriosNutritionPerServing.portion)
        prettyPrint(expectedNutrition)
        assertAboutEqual(expectedNutrition, nutritionInfo, 10.0)
    }
}

fun assertEquals(expected: Weight, actual: Weight, absoluteTolerance: Weight) {
    val result = expected - actual
    assert(result <= absoluteTolerance.absoluteValue) {
        "expected: $expected\nactual: $actual\ntolerance: $absoluteTolerance"
    }
}

fun assertEquals(
    expected: Weight,
    actual: Weight,
    absoluteAccuracy: Double = .05,
    ifZeroTolerance: Weight = absoluteAccuracy.grams,
) {
    if (expected == weightOf())
        assertEquals(expected, actual, ifZeroTolerance)
    else
        assertContains(
            -absoluteAccuracy.absoluteValue..absoluteAccuracy.absoluteValue, (expected - actual) / expected,
            "expected: $expected\nactual: $actual\ntolerance: $absoluteAccuracy"
        )

}

fun assertEquals(
    expected: FoodEnergy,
    actual: FoodEnergy,
    absoluteAccuracy: Double = .05,
    ifZeroTolerance: FoodEnergy = absoluteAccuracy.kcal,
) {
    if (expected == 0.kcal)
        assertEquals(expected.inKilocalories(), actual.inKilocalories(), ifZeroTolerance.inKilocalories())
    else
        assertContains(
            -absoluteAccuracy.absoluteValue..absoluteAccuracy.absoluteValue,
            (expected - actual) / expected,
            "expected: $expected\nactual: $actual\ntolerance: $absoluteAccuracy",
        )
}

fun assertAboutEqual(expected: NutritionInfo, actual: NutritionInfo, tolerance: Double = 0.1) {
    assert(
        expected.compareWith(
            actual,
            compareWeight = { a, b ->
                if (a != null && b != null) true.also { assertEquals(a, b) }
                else true.also { assertEquals(a, b, "expected both to be null\nfound \n$a\n$b") }
            },
            compareEnergy = { a, b -> true.also { assertEquals(a, b, tolerance) } },
            comparePortion = { a, b ->
                when {
                    a.volume != null && b.volume != null -> (a.volume!! - b.volume!!).inMilliliters().absoluteValue <= tolerance.absoluteValue
                    a.weight != null && b.weight != null -> true.also { assertEquals(a.weight!!, b.weight!!) }
                    else -> true.also { assertEquals(a, b, "expected both to be null\nfound \n$a\n$b") }
                }
            }
        )
    )
}
