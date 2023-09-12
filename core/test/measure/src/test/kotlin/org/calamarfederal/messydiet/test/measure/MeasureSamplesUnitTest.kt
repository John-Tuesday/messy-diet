package org.calamarfederal.messydiet.test.measure

import org.calamarfederal.messydiet.diet_data.model.Nutrients
import org.calamarfederal.messydiet.measure.grams
import kotlin.test.Test

class MeasureSamplesUnitTest {
    @Test
    fun `filledNutritionA has every field filled`() {
        val testSample = MeasureSamples.filledNutritionA
        val missingNutrients = mutableListOf<String>()
        for (n in Nutrients.entries) {
            if (testSample[n] == null)
                missingNutrients.add("Nutrient $n cannot be null")
        }

        assert(missingNutrients.isEmpty()) {
            missingNutrients.joinToString(separator = "\n") { it }
        }
    }

    @Test
    fun `filledNonZeroNutritionA has every field filled and non zero`() {
        val testSample = MeasureSamples.filledNonZeroNutritionA
        val missingNutrients = mutableListOf<String>()
        for (n in Nutrients.entries) {
            if (testSample[n] == null)
                missingNutrients.add("Nutrient $n cannot be null")
            else if (testSample[n] == 0.grams)
                missingNutrients.add("Nutrient $n cannot be 0")
        }

        assert(missingNutrients.isEmpty()) {
            missingNutrients.joinToString(separator = "\n") { it }
        }
    }
}
