package org.calamarfederal.messydiet.test.measure

import org.calamarfederal.messydiet.measure.grams
import org.calamarfederal.messydiet.measure.plus
import kotlin.test.Test
import kotlin.test.assertFails

class AssertHelpersUnitTest {
    @Test
    fun `Assert NutritionInfo equal passes when exactly equal`() {
        val a = MeasureSamples.filledNutritionA
        val b = a.copy()

        assertEquals(
            expected = a,
            actual = b,
            tolerance = 0.00,
        )
    }

    @Test
    fun `Assert NutritionInfo equal passes when within tolerance`() {
        val a = MeasureSamples.filledNutritionA
        val b = a.copy(totalProtein = a.totalProtein + 0.1.grams)

        assertEquals(
            expected = a,
            actual = b,
            tolerance = 1.00,
        )
    }

    @Test
    fun `Assert NutritionInfo equal fails when outside tolerance`() {
        val a = MeasureSamples.filledNutritionA
        val b = a + a

        assertFails {
            assertEquals(
                expected = a,
                actual = b,
                tolerance = 0.1,
            )
        }
    }
}
