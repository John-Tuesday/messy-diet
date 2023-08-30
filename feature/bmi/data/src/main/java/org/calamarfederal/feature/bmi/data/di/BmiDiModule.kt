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
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.calamarfederal.feature.bmi.data.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BmiDiModule {

    @Binds
    abstract fun bindBmiRepository(impl: BmiRepositoryImplementation): BmiRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class HeightWeightBinder {

    @Binds
    abstract fun bindHeightWeightLocalSource(impl: UserHeightLocalSourceImplementation): UserHeightLocalSource

    @Binds
    abstract fun bindHeightWeightRepository(impl: UserHeightRepositoryImplementation): UserHeightWeightRepository
}

@Module
@InstallIn(SingletonComponent::class)
object HeightWeightProvider {

    @Provides
    @Singleton
    fun provideHeightWeightDataSource(@ApplicationContext context: Context): DataStore<HeightWeight> =
        context.heightWeightStore
//        DataStoreFactory.create(
//            serializer = HeightWeightStoreSerializer,
//            produceFile = { File(HeightWeightStoreFileName) }
//        )
}
