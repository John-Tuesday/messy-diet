/******************************************************************************
 * Copyright (c) 2023 John Tuesday Picot                                      *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  *
 * copies of the Software, and to permit persons to whom the Software is      *
 * furnished to do so, subject to the following conditions:                   *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.                            *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,   *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE*
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER     *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.                                                                  *
 ******************************************************************************/

package org.calamarfederal.feature.bmi.data

import org.calamarfederal.feature.bmi.data.BmiCategory.*
import org.calamarfederal.physical.measurement.*
import javax.inject.Inject
import kotlin.math.pow

enum class BmiCategory(start: Double, endExclusive: Double) {
    VerySeverelyUnderweight(Double.NEGATIVE_INFINITY, 15.00),
    SeverelyUnderweight(15.00, 16.00),
    Underweight(16.00, 18.50),
    Normal(18.50, 25.00),
    Overweight(25.00, 30.00),
    ModeratelyObese(30.00, 35.00),
    SeverelyObese(35.00, 40.00),
    VerySeverelyObese(40.00, Double.POSITIVE_INFINITY),
    ;
    internal val openEndRange = start..<endExclusive
}

private fun bmiCategoryOf(index: Double): BmiCategory = when (index) {
    in VerySeverelyUnderweight.openEndRange -> VerySeverelyUnderweight
    in SeverelyUnderweight.openEndRange -> SeverelyUnderweight
    in Underweight.openEndRange -> Underweight
    in Normal.openEndRange -> Normal
    in Overweight.openEndRange -> Overweight
    in ModeratelyObese.openEndRange -> ModeratelyObese
    in SeverelyObese.openEndRange -> SeverelyObese
    in VerySeverelyObese.openEndRange -> VerySeverelyObese
    else -> throw (IllegalArgumentException())
}

data class Bmi(
    val value: Double = 0.00,
    val category: BmiCategory = bmiCategoryOf(value),
)

interface BmiRepository {
    fun bmiOf(height: Length, mass: Mass): Bmi
}

class BmiRepositoryImplementation @Inject constructor() : BmiRepository {
    override fun bmiOf(height: Length, mass: Mass): Bmi {
        require(height != 0.00.meters) {
            "Height is a divisor and so cannot be zero"
        }
        val bmiIndex = mass.inKilograms() / height.inMeters().pow(2)
        return Bmi(bmiIndex, bmiCategoryOf(bmiIndex))
    }
}
