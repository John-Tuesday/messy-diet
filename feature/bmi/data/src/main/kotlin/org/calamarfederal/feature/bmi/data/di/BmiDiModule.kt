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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.calamarfederal.feature.bmi.data.repository.BmiRepository
import org.calamarfederal.feature.bmi.data.repository.BmiRepositoryImplementation
import org.calamarfederal.feature.bmi.data.repository.UserHeightMassRepository
import org.calamarfederal.feature.bmi.data.repository.UserHeightRepositoryImplementation
import org.calamarfederal.feature.bmi.data.source.local.UserHeightMassLocalSource
import org.calamarfederal.feature.bmi.data.source.local.UserHeightMassLocalSourceImplementation
import org.calamarfederal.feature.bmi.data.source.local.datastore.HeightMass
import org.calamarfederal.feature.bmi.data.source.local.datastore.heightMassStore

interface FeatureBmiDataModule {
    fun provideBmiRepository(): BmiRepository
    fun provideHeightMassLocalRepository(): UserHeightMassRepository

    companion object {
        fun implementation(context: Context, ioDispatcher: CoroutineDispatcher = Dispatchers.IO): FeatureBmiDataModule =
            FeatureBmiDataModuleImplementation(context = context, ioDispatcher = ioDispatcher)
    }
}

internal class FeatureBmiDataModuleImplementation(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) : FeatureBmiDataModule {
    private val heightWeightDataSource: DataStore<HeightMass> by lazy {
        context.heightMassStore
    }

    override fun provideBmiRepository(): BmiRepository = BmiRepositoryImplementation()
    override fun provideHeightMassLocalRepository(): UserHeightMassRepository = UserHeightRepositoryImplementation(
        heightMassLocalSource = provideHeightMassLocalSource()
    )

    internal fun provideHeightMassLocalSource(): UserHeightMassLocalSource =
        UserHeightMassLocalSourceImplementation(
            heightMassStore = heightWeightDataSource,
            ioDispatcher = ioDispatcher,
        )
}
