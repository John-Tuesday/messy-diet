package org.calamarfederal.messydiet.feature.search.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import org.calamarfederal.messydiet.core.android.hilt.NetworkDispatcher
import org.calamarfederal.messydiet.feature.search.data.*
import org.calamarfederal.messydiet.feature.search.data.FoodDetailsRepositoryImplementation
import org.calamarfederal.messydiet.feature.search.data.FoodSearchRepositoryImplementation
import org.calamarfederal.messydiet.food.data.central.FoodDataCentralRepository
import org.calamarfederal.messydiet.food.data.central.di.FoodDataCentral
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class FoodDataCentralApiKey

@Module
@InstallIn(SingletonComponent::class)
internal object FoodSearchDiModule {
    @Provides
    @FoodDataCentralApiKey
    fun provideApiKey(): String = "DEMO_KEY"

    @Provides
    fun provideFoodDataCentralRepository(
        @FoodDataCentralApiKey
        apiKey: String,
        @NetworkDispatcher
        networkDispatcher: CoroutineDispatcher,
    ): FoodDataCentralRepository = FoodDataCentral.repository(
        networkDispatcher = networkDispatcher,
        apiKey = apiKey,
    )
}

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class FoodSearchDiBindModule {
    @Binds
    @ViewModelScoped
    abstract fun bindFoodSearchRepository(impl: FoodSearchRepositoryImplementation): FoodSearchRepository
}

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class FoodDetailsDiBindModule{
    @Binds
    @ViewModelScoped
    abstract fun bindFoodSearchRepository(impl: FoodDetailsRepositoryImplementation): FoodDetailsRepository
}

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class SaveFoodDetailsDiBindModule {
    @Binds
    @ViewModelScoped
    abstract fun bindSaveFoodDetailsRepository(impl: SaveFoodDetailsRepositoryImplementation): SaveFoodDetailsRepository
}
