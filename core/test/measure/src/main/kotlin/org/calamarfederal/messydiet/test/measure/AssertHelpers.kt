package org.calamarfederal.messydiet.test.measure

import org.calamarfederal.messydiet.diet_data.model.*
import org.calamarfederal.messydiet.measure.*
import kotlin.math.absoluteValue
import kotlin.test.assertContains
import kotlin.test.assertEquals

fun assertEquals(expected: Weight, actual: Weight, absoluteTolerance: Weight, message: String? = null) {
    val result = expected - actual
    assert(result <= absoluteTolerance.absoluteValue) {
        "$message\nexpected: $expected\nactual: $actual\ntolerance: $absoluteTolerance"
    }
}

fun assertEquals(expected: Volume, actual: Volume, absoluteTolerance: Volume, message: String? = null) {
    val result = expected - actual
    assert(result <= absoluteTolerance.absoluteValue) {
        "$message\nexpected: $expected\nactual: $actual\ntolerance: $absoluteTolerance"
    }
}

fun assertEquals(expected: Length, actual: Length, absoluteTolerance: Length, message: String? = null) {
    val result = expected - actual
    assert(result <= absoluteTolerance.absoluteValue) {
        "$message\nexpected: $expected\nactual: $actual\ntolerance: $absoluteTolerance"
    }
}

fun assertEquals(
    expected: Weight,
    actual: Weight,
    absoluteAccuracy: Double = .05,
    ifZeroTolerance: Weight = absoluteAccuracy.grams,
    message: String? = null,
) {
    if (expected == weightOf())
        assertEquals(expected, actual, ifZeroTolerance, message = message)
    else
        assertContains(
            -absoluteAccuracy.absoluteValue..absoluteAccuracy.absoluteValue, (expected - actual) / expected,
            "$message\nexpected: $expected\nactual: $actual\ntolerance: $absoluteAccuracy"
        )

}

fun assertEquals(
    expected: FoodEnergy,
    actual: FoodEnergy,
    absoluteAccuracy: Double = .05,
    ifZeroTolerance: FoodEnergy = absoluteAccuracy.kcal,
    message: String? = null,
) {
    if (expected == 0.kcal)
        assertEquals(
            expected.inKilocalories(),
            actual.inKilocalories(),
            ifZeroTolerance.inKilocalories(),
            message = message
        )
    else
        assertContains(
            -absoluteAccuracy.absoluteValue..absoluteAccuracy.absoluteValue,
            (expected - actual) / expected,
            "$message\nexpected: $expected\nactual: $actual\ntolerance: $absoluteAccuracy",
        )
}

fun assertEquals(
    expected: NutritionInfo,
    actual: NutritionInfo,
    tolerance: Double = 0.1,
    nullEqualsZero: Boolean = false,
    message: String? = null,
) {
    assert(
        expected.compareWith(
            actual,
            compareWeight = { nutrient, a, b ->
                if (a != null && b != null)
                    true.also {
                        assertEquals(
                            expected = a,
                            actual = b,
                            absoluteAccuracy = tolerance,
                            message = "Nutrient not equal: $nutrient"
                        )
                    }
                else if (nullEqualsZero) {
                    assert((a ?: 0.grams) == (b ?: 0.grams)) {
                        "Nutrient not equal: $nutrient\nexpected = $a\nactual = $b"
                    }
                    assertEquals(
                        expected = a ?: 0.grams,
                        actual = b ?: 0.grams,
                        absoluteTolerance = 0.grams,
                        message = "Nutrient not equal: $nutrient\nexpected = $a\nactual = $b"
                    )
                    true
                } else
                    true.also {
                        assertEquals(a, b, "Nutrient not equal: $nutrient\nExpected both to be null\nfound \n$a\n$b")
                    }
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
    ) {
        message ?: ""
    }
}
