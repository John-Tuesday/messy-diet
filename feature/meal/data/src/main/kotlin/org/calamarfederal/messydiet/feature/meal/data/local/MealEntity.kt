package org.calamarfederal.messydiet.feature.meal.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.calamarfederal.messydiet.diet_data.model.FoodEnergy
import org.calamarfederal.messydiet.diet_data.model.inKilocalories
import org.calamarfederal.messydiet.diet_data.model.kcal
import org.calamarfederal.messydiet.measure.Weight
import org.calamarfederal.messydiet.measure.grams

internal class MealConverters {
    @TypeConverter
    fun fromWeightToDoubleOrNull(weight: Weight?): Double? = weight?.inGrams()

    @TypeConverter
    fun fromDoubleToWeightOrNull(grams: Double?): Weight? = grams?.grams

    @TypeConverter
    fun fromFoodEnergyToDoubleOrNull(energy: FoodEnergy?): Double? = energy?.inKilocalories()

    @TypeConverter
    fun fromDoubleToFoodEnergyOrNull(kcals: Double?): FoodEnergy? = kcals?.kcal

    @TypeConverter
    fun fromFoodEnergyToDouble(energy: FoodEnergy): Double = energy.inKilocalories()

    @TypeConverter
    fun fromDoubleToFoodEnergy(kcals: Double): FoodEnergy = kcals.kcal

    @TypeConverter
    fun fromWeightToLong(weight: Weight): Double = weight.inGrams()

    @TypeConverter
    fun fromDoubleToWeight(grams: Double): Weight = grams.grams
}

@Entity(tableName = "meal")
@TypeConverters(MealConverters::class)
internal data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(index = true)
    val name: String,
    @ColumnInfo(name = "total_protein")
    val totalProtein: Weight,
    val fiber: Weight?,
    val sugar: Weight?,
    @ColumnInfo(name = "sugar_alcohol")
    val sugarAlcohol: Weight?,
    val starch: Weight?,
    @ColumnInfo(name = "total_carbohydrates")
    val totalCarbohydrates: Weight,
    val monounsaturated: Weight?,
    val polyunsaturated: Weight?,
    val omega3: Weight?,
    val omega6: Weight?,
    val saturated: Weight?,
    val trans: Weight?,
    val cholesterol: Weight?,
    @ColumnInfo(name = "total_fat")
    val totalFat: Weight,
    val calcium: Weight?,
    val chloride: Weight?,
    val iron: Weight?,
    val magnesium: Weight?,
    val phosphorous: Weight?,
    val potassium: Weight?,
    val sodium: Weight?,
    val portion: Weight,
    @ColumnInfo(name = "food_energy")
    val foodEnergy: FoodEnergy,
) {
    companion object {
        internal const val UNSET_ID: Long = 0L
    }
}


@Dao
internal interface SavedMealDao {
    @Query("SELECT id FROM meal")
    fun getAllMealIds(): Flow<List<Long>>

    @Query("SELECT * FROM meal")
    fun getAlMeals(): Flow<List<MealEntity>>

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
}

private const val DB_VERSION = 1

@Database(
    entities = [MealEntity::class],
    version = DB_VERSION,
    exportSchema = false,
)
internal abstract class SavedMealLocalDb : RoomDatabase() {
    abstract fun getSavedMealDao(): SavedMealDao
}
