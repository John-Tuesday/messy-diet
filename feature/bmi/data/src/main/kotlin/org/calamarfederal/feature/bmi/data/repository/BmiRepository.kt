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

package org.calamarfederal.feature.bmi.data.repository

import org.calamarfederal.feature.bmi.data.model.Bmi
import org.calamarfederal.physical.measurement.Length
import org.calamarfederal.physical.measurement.Mass
import org.calamarfederal.physical.measurement.inKilograms
import org.calamarfederal.physical.measurement.inMeters
import kotlin.math.pow

interface BmiRepository {
    /**
     * Correctly build a [Bmi] given [height] and [mass]
     */
    fun bmiOf(height: Length, mass: Mass): Bmi
}

class BmiRepositoryImplementation : BmiRepository {
    override fun bmiOf(height: Length, mass: Mass): Bmi {
        val heightMeters = height.inMeters()
        require(heightMeters != 0.00) {
            "Height is a divisor and so cannot be zero"
        }
        val bmiIndex = mass.inKilograms() / heightMeters.pow(2)
        return Bmi.fromBmiIndex(bmiIndex)
    }
}
