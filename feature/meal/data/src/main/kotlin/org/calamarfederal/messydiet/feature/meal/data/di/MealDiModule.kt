package org.calamarfederal.messydiet.feature.meal.data.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.calamarfederal.messydiet.feature.meal.data.MealLocalSource
import org.calamarfederal.messydiet.feature.meal.data.MealLocalSourceImplementation
import org.calamarfederal.messydiet.feature.meal.data.MealRepository
import org.calamarfederal.messydiet.feature.meal.data.MealRepositoryImplementation
import org.calamarfederal.messydiet.feature.meal.data.local.SavedMealDao
import org.calamarfederal.messydiet.feature.meal.data.local.SavedMealLocalDb
import javax.inject.Singleton

object MealManualDi {
    const val MEAL_DB_NAME = "savedmeallocal.db"
}

@Module
@InstallIn(SingletonComponent::class)
internal object MealDiModule {
    @Provides
    @Singleton
    fun provideMealDb(@ApplicationContext context: Context): SavedMealLocalDb = Room
        .databaseBuilder(
            context = context,
            klass = SavedMealLocalDb::class.java,
            name = MealManualDi.MEAL_DB_NAME,
        ).fallbackToDestructiveMigration()
//        .setAutoCloseTimeout(15, TimeUnit.MINUTES)
        .build()

    @Provides
    fun provideSavedMealDao(db: SavedMealLocalDb): SavedMealDao = db.getSavedMealDao()
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MealBindDiModule {
    @Binds
    internal abstract fun bindMealLocalSource(impl: MealLocalSourceImplementation): MealLocalSource

    @Binds
    internal abstract fun bindMealRepository(impl: MealRepositoryImplementation): MealRepository
}
