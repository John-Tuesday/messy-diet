package org.calamarfederal.messydiet.diet_data.model

import org.calamarfederal.messydiet.diet_data.model.assertEquals
import org.calamarfederal.messydiet.measure.*
import kotlin.math.absoluteValue
import kotlin.test.*

internal class NutrientsUnitTest {
    private val sample = Nutrition(
        portion = Portion(100.grams),
        foodEnergy = 359.kcal,
        totalProtein = 12.8.grams,
        totalFat = 6.41.grams,
        totalCarbohydrates = 74.4.grams,
        fiber = 10.3.grams,
        sugar = 5.13.grams, // 5.1 added
        calcium = 333.milligrams,
        iron = 32.3.milligrams,
        magnesium = 154.milligrams, // 700 from DV
//        phosphorous = 123.milligrams, // 256 from DV ?? 750
        potassium = 641.milligrams,
        sodium = 487.milligrams,
//        zinc
        vitaminC = 90.milligrams, // ?? 15.4 3.7
        saturatedFat = 1.28.grams,
        monounsaturatedFat = 2.56.grams,
        polyunsaturatedFat = 2.56.grams,
        transFat = 0.grams,
        cholesterol = 0.milligrams,
    )

    @Test
    fun `Nutrient compare with when equal`() {
        val first = sample.copy()
        val second = first.copy()

        assertTrue {
            first.compareWith(
                second,
                compareWeight = { a, b -> a == b },
                comparePortion = { a, b -> a == b },
                compareEnergy = { a, b -> a == b },
            )
        }

        assertAboutEqual(first, second)
    }

    @Test
    fun `Nutrients within tolerance`() {
        val first = sample.copy()
        val second = sample.copy(fiber = sample.fiber!! - 1.milligrams)

        assertFalse {
            first.compareWith(
                second,
                compareWeight = { a, b -> a == b },
                comparePortion = { a, b -> a == b },
                compareEnergy = { a, b -> a == b },
            )
        }

        assertAboutEqual(first, second)
    }

    @Test
    fun `Nutrients outside of tolerance`() {
        val first = sample.copy()
        val second = sample + sample

        assertFalse {
            first.compareWith(
                second,
                compareWeight = { a, b -> a == b },
                comparePortion = { a, b -> a == b },
                compareEnergy = { a, b -> a == b },
            )
        }

        assertFails {
            assertAboutEqual(first, second)
        }
    }
}

fun assertEquals(expected: Weight, actual: Weight, absoluteTolerance: Weight) {
    val result = expected - actual
    assert(result <= absoluteTolerance.absoluteValue)
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
        assertContains(-absoluteAccuracy.absoluteValue..absoluteAccuracy.absoluteValue, (expected - actual) / expected)
}

fun assertAboutEqual(expected: NutritionInfo, actual: NutritionInfo, tolerance: Double = 0.1) {
    assert(
        expected.compareWith(
            actual,
            compareWeight = { a, b ->
                if (a != null && b != null) true.also { assertEquals(a, b) }
                else true.also { assertEquals(a, b) }
            },
            compareEnergy = { a, b -> true.also { assertEquals(a, b, tolerance) } },
            comparePortion = { a, b ->
                when {
                    a.volume != null && b.volume != null -> (a.volume!! - b.volume!!).inMilliliters().absoluteValue <= tolerance.absoluteValue
                    a.weight != null && b.weight != null -> true.also { assertEquals(a.weight!!, b.weight!!) }
                    else -> true.also { assertEquals(a, b) }
                }
            }
        )
    )
}
