package org.calamarfederal.feature.bmi.data.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotEquals

class BmiModelUnitTest {
    @Test
    fun `Bmi fromBmiIndex get correct category with valid input`() {
        for (bmiCategory in BmiCategory.entries) {
            assertEquals(bmiCategory, BmiCategory.fromBmiIndex(bmiCategory.openEndRange.start))
            if (bmiCategory.ordinal != BmiCategory.entries.lastIndex)
                assertNotEquals(bmiCategory, BmiCategory.fromBmiIndex(bmiCategory.openEndRange.endExclusive))
            else
                assertFails {
                    assertNotEquals(
                        bmiCategory,
                        BmiCategory.fromBmiIndex(bmiCategory.openEndRange.endExclusive)
                    )
                }
        }
    }
}
