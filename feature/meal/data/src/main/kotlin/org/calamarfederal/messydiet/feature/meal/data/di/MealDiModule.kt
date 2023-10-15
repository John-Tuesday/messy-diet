package org.calamarfederal.messydiet.feature.meal.data.di

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.calamarfederal.messydiet.feature.meal.data.MealLocalSource
import org.calamarfederal.messydiet.feature.meal.data.MealLocalSourceImplementation
import org.calamarfederal.messydiet.feature.meal.data.MealRepository
import org.calamarfederal.messydiet.feature.meal.data.MealRepositoryImplementation
import org.calamarfederal.messydiet.feature.meal.data.local.SavedMealLocalDb

interface FeatureMealDataModule {
    fun provideMealRepository(): MealRepository

    companion object {
        fun implementation(context: Context): FeatureMealDataModule = FeatureMealDataModuleImplementation(
            context = context.applicationContext,
        )
    }
}


internal class FeatureMealDataModuleImplementation(
    private val context: Context,
) : FeatureMealDataModule {
    companion object {
        internal const val DatabaseName = "savedmeallocal.db"
    }

    private val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO
    private val mealLocalDb: SavedMealLocalDb by lazy {
        Room
            .databaseBuilder(
                context = context,
                klass = SavedMealLocalDb::class.java,
                name = DatabaseName,
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    internal fun provideMealLocalSource(): MealLocalSource = MealLocalSourceImplementation(
        db = mealLocalDb,
        dao = mealLocalDb.getSavedMealDao(),
        ioDispatcher = ioDispatcher,
    )

    override fun provideMealRepository(): MealRepository =
        MealRepositoryImplementation(mealLocalSource = provideMealLocalSource())
}
