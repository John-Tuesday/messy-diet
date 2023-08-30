package org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats

import org.junit.Before
import org.junit.Test
import kotlin.math.absoluteValue

class HeightAndWeightInputStateImplUnitTests {
    private lateinit var inputState: HeightAndWeightInputStateImpl

    @Before
    fun setUp() {
        inputState = HeightAndWeightInputStateImpl()
    }

    @Test
    fun `Switching to and from Units converts properly`() {
        val tolerance = 0.01

        inputState.heightInputType = HeightInputType.FeetAndInches
        inputState.heightInput = "5"
        inputState.heightInputOptional = "9"
        inputState.heightInputType = HeightInputType.OnlyMeters

        assert(
            (inputState.heightInput.toDouble() - 1.75).absoluteValue <= tolerance
        )
        assert(
            inputState.heightInputOptional.isEmpty()
        )

        inputState.heightInput = "1.6"
        inputState.heightInputType = HeightInputType.FeetAndInches
        assert(
            inputState.heightInput == "5"
        )
        assert(
            (inputState.heightInputOptional.toDouble() - 2.992).absoluteValue <= tolerance
        ) {
            "height optional: ${inputState.heightInputOptional}"
        }
        inputState.heightInputType = HeightInputType.OnlyFeet
        assert(
            (inputState.heightInput.toDouble() - 5.25).absoluteValue <= tolerance
        )
        inputState.heightInputType = HeightInputType.OnlyInches
        assert(
            (inputState.heightInput.toDouble() - 63).absoluteValue <= tolerance
        )
    }
}
