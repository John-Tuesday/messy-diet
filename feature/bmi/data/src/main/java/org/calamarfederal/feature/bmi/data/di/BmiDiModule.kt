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

package org.calamarfederal.feature.bmi.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import org.calamarfederal.feature.bmi.data.*

interface FeatureBmiDataModule {
    fun provideBmiRepository(): BmiRepository
    fun provideHeightMassLocalRepository(): UserHeightMassRepository

    companion object {
        fun implementation(context: Context): FeatureBmiDataModule =
            FeatureBmiDataModuleImplementation(context = context)
    }
}

internal class FeatureBmiDataModuleImplementation(
    private val context: Context,
) : FeatureBmiDataModule {
    private val heightWeightDataSource: DataStore<HeightWeight> by lazy {
        context.heightWeightStore
    }

    override fun provideBmiRepository(): BmiRepository = BmiRepositoryImplementation()
    override fun provideHeightMassLocalRepository(): UserHeightMassRepository = UserHeightRepositoryImplementation(
        heightMassLocalSource = provideHeightMassLocalSource()
    )

    internal fun provideHeightMassLocalSource(): UserHeightLocalSource =
        UserHeightLocalSourceImplementation(heightWeightStore = heightWeightDataSource)
}
