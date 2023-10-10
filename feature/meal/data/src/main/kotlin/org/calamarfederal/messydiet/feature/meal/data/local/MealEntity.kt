package org.calamarfederal.messydiet.feature.meal.data.local

import androidx.room.*
import io.github.john.tuesday.nutrition.NutrientType
import io.github.john.tuesday.nutrition.Portion
import org.calamarfederal.physical.measurement.Energy
import org.calamarfederal.physical.measurement.Mass

@Entity(
    tableName = "meal",
    indices = [Index("name", unique = true)]
)
@TypeConverters(
    MeasureConverters::class,
    NutritionConverters::class,
)
internal data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    @ColumnInfo(name = "food_energy")
    val foodEnergy: Energy,
    val portion: Portion,
) {
    companion object {
        internal const val UNSET_ID: Long = 0L
    }
}

@Entity(
    tableName = "meal_nutrient",
    primaryKeys = ["meal_id", "nutrient_type"],
//    indices = [Index("meal_id", "nutrient_type", unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = MealEntity::class,
            childColumns = ["meal_id"],
            parentColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true,
        )
    ],
)
@TypeConverters(
    MeasureConverters::class,
    NutritionConverters::class,
)
data class MealNutrientEntity(
//    @PrimaryKey
//    val id: Long,
    @ColumnInfo(name = "meal_id")
    val mealId: Long,
    @ColumnInfo(name = "nutrient_type")
    val nutrientType: NutrientType,
    val amount: Mass,
)

private const val DB_VERSION = 11

@Database(
    entities = [MealEntity::class, MealNutrientEntity::class],
    version = DB_VERSION,
    exportSchema = false,
)
internal abstract class SavedMealLocalDb : RoomDatabase() {
    abstract fun getSavedMealDao(): SavedMealDao
}
