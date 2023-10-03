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

package org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.calamarfederal.feature.bmi.data.Bmi
import org.calamarfederal.feature.bmi.data.BmiRepository
import org.calamarfederal.feature.bmi.data.UserHeightMassRepository
import org.calamarfederal.physical.measurement.meters
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class EnterStatsViewModel @Inject constructor(
    private val bmiRepository: BmiRepository,
    private val userHeightWeightRepository: UserHeightMassRepository,
) : ViewModel() {
    private fun <T> Flow<T>.stateInViewModel(initial: T) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds),
        initialValue = initial,
    )

    private val _heightAndWeightInputState = MutableStateFlow(HeightAndWeightInputStateImpl().apply {
        runBlocking {
            setHeight(userHeightWeightRepository.heightFlow.first())
            setWeight(userHeightWeightRepository.massFlow.first())
        }
    })
    val heightAndWeightInputState: StateFlow<HeightAndWeightInputState> = _heightAndWeightInputState.asStateFlow()


    val bmiState = _heightAndWeightInputState.mapLatest {
        combine(snapshotFlow { it.weightState.value }, snapshotFlow { it.heightState.value }) { mass, height ->
            if (height != 0.meters)
                bmiRepository.bmiOf(
                    height = height,
                    mass = mass,
                ).also {
                    userHeightWeightRepository.setHeight(height)
                    userHeightWeightRepository.setMass(mass)
                }
            else
                Bmi()
        }
    }
        .flattenConcat()
        .stateInViewModel(Bmi())
}
