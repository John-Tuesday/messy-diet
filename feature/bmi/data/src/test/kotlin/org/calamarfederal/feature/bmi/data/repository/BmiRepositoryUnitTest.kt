package org.calamarfederal.feature.bmi.data.repository

import org.calamarfederal.feature.bmi.data.model.BmiCategory
import org.calamarfederal.physical.measurement.Length
import org.calamarfederal.physical.measurement.Mass
import org.calamarfederal.physical.measurement.centimeters
import org.calamarfederal.physical.measurement.kilograms
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BmiRepositoryUnitTest {
    private lateinit var bmiRepository: BmiRepository

    @BeforeTest
    fun setUp() {
        bmiRepository = BmiRepositoryImplementation()
    }

    @Test
    fun `bmi from height and mass`() {
        fun check(height: Length, mass: Mass, bmiIndex: Double, bmiCategory: BmiCategory, tolerance: Double = 0.05) {
            val actual = bmiRepository.bmiOf(height = height, mass = mass)
            assertEquals(bmiIndex, actual.value, absoluteTolerance = tolerance)
            assertEquals(bmiCategory, actual.category)
        }

        check(
            height = 200.centimeters,
            mass = 54.kilograms,
            bmiIndex = 13.5,
            bmiCategory = BmiCategory.VerySeverelyUnderweight,
        )
        check(
            height = 190.centimeters,
            mass = 55.kilograms,
            bmiIndex = 15.2,
            bmiCategory = BmiCategory.SeverelyUnderweight,
        )
        check(
            height = 180.centimeters,
            mass = 59.kilograms,
            bmiIndex = 18.2,
            bmiCategory = BmiCategory.Underweight,
        )
        check(
            height = 170.centimeters,
            mass = 60.kilograms,
            bmiIndex = 20.8,
            bmiCategory = BmiCategory.Normal,
        )
        check(
            height = 170.centimeters,
            mass = 80.kilograms,
            bmiIndex = 27.7,
            bmiCategory = BmiCategory.Overweight,
        )
        check(
            height = 134.centimeters,
            mass = 60.kilograms,
            bmiIndex = 33.4,
            bmiCategory = BmiCategory.ModeratelyObese,
        )
        check(
            height = 110.centimeters,
            mass = 45.kilograms,
            bmiIndex = 37.2,
            bmiCategory = BmiCategory.SeverelyObese,
        )
        check(
            height = 100.centimeters,
            mass = 60.kilograms,
            bmiIndex = 60.0,
            bmiCategory = BmiCategory.VerySeverelyObese,
        )
    }
}
