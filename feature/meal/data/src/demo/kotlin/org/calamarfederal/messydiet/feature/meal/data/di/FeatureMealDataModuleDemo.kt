package org.calamarfederal.messydiet.feature.meal.data.di

import android.content.Context
import androidx.room.Room
import org.calamarfederal.messydiet.feature.meal.data.local.SavedMealLocalDb

fun FeatureMealDataModule.Companion.testImplementation(
    context: Context,
): FeatureMealDataModule = FeatureMealDataModuleTestImplementation(context)

internal class FeatureMealDataModuleTestImplementation(
    context: Context,
) : FeatureMealDataModuleImplementation(context = context) {

    override val mealLocalDb: SavedMealLocalDb by lazy {
        Room
            .inMemoryDatabaseBuilder(
                context = context,
                klass = SavedMealLocalDb::class.java,
            )
            .fallbackToDestructiveMigration()
            .build()

    }
}
