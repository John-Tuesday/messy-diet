package org.calamarfederal.messydiet.feature.meal.data.local

import androidx.room.*
import io.github.john.tuesday.nutrition.NutrientType
import io.github.john.tuesday.nutrition.Portion
import kotlinx.coroutines.flow.Flow
import org.calamarfederal.physical.measurement.Energy
import org.calamarfederal.physical.measurement.Mass

@Entity(tableName = "meal")
@TypeConverters(
    MeasureConverters::class,
    NutritionConverters::class,
)
internal data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(index = true)
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

@Dao
internal interface MealNutrientDao {
    @Query("SELECT amount FROM meal_nutrient WHERE meal_id = :mealId AND nutrient_type = :nutrientType")
    @TypeConverters(MeasureConverters::class)
    fun getNutrient(mealId: Long, nutrientType: NutrientType): Flow<Mass?>

    @MapInfo(
        keyColumn = "nutrient_type",
        keyTable = "meal_nutrient",
        valueColumn = "amount",
        valueTable = "meal_nutrient"
    )
    @Query("SELECT * FROM meal_nutrient WHERE meal_id = :mealId")
    @TypeConverters(MeasureConverters::class)
    fun getAllNutrientsOf(mealId: Long): Flow<Map<NutrientType, Mass>>

    @Upsert
    suspend fun upsertNutrient(nutrient: MealNutrientEntity)

    @Insert
    suspend fun insertNutrients(nutrients: List<MealNutrientEntity>): List<Long>

    @Update
    suspend fun updateNutrients(nutrients: List<MealNutrientEntity>): Int

    @Upsert
    suspend fun upsertNutrients(nutrients: List<MealNutrientEntity>)

    @Transaction
    @Query("DELETE FROM meal_nutrient WHERE meal_id = :mealId AND nutrient_type in (:nutrientTypes)")
    suspend fun deleteNutrientsOf(mealId: Long, nutrientTypes: List<NutrientType>)

    @Transaction
    @Query("DELETE FROM meal_nutrient WHERE meal_id in (:mealIds)")
    suspend fun deleteAllNutrientsOf(mealIds: List<Long>)
}


@Dao
internal interface SavedMealDao : MealNutrientDao {
    @Query("SELECT id FROM meal")
    fun getAllMealIds(): Flow<List<Long>>

    @Query("SELECT * FROM meal")
    fun getAllMeals(): Flow<List<MealEntity>>

    @Query("SELECT * FROM meal WHERE id = :id")
    fun getMeal(id: Long): Flow<MealEntity?>

    @Insert
    suspend fun insertMeal(meal: MealEntity): Long

    @Update
    suspend fun updateMeal(meal: MealEntity): Int

    @Upsert
    suspend fun upsertMeal(meal: MealEntity)

    @Query("DELETE FROM meal WHERE id = :id")
    suspend fun deleteMeal(id: Long): Int

    @Query("DELETE FROM meal WHERE id IN (:ids)")
    suspend fun deleteMeals(ids: List<Long>)
}

private const val DB_VERSION = 5

@Database(
    entities = [MealEntity::class, MealNutrientEntity::class],
    version = DB_VERSION,
    exportSchema = false,
)
internal abstract class SavedMealLocalDb : RoomDatabase() {
    abstract fun getSavedMealDao(): SavedMealDao
}
