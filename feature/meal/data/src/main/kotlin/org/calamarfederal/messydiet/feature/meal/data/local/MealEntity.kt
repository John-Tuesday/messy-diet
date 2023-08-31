package org.calamarfederal.messydiet.feature.meal.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.calamarfederal.messydiet.diet_data.model.FoodEnergy
import org.calamarfederal.messydiet.diet_data.model.NutritionInfo
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
    override val totalProtein: Weight,
    override val fiber: Weight?,
    override val sugar: Weight?,
    @ColumnInfo(name = "sugar_alcohol")
    override val sugarAlcohol: Weight?,
    override val starch: Weight?,
    @ColumnInfo(name = "total_carbohydrates")
    override val totalCarbohydrates: Weight,
    override val monounsaturatedFat: Weight?,
    override val polyunsaturatedFat: Weight?,
    override val omega3: Weight?,
    override val omega6: Weight?,
    override val saturatedFat: Weight?,
    override val transFat: Weight?,
    override val cholesterol: Weight?,
    @ColumnInfo(name = "total_fat")
    override val totalFat: Weight,
    override val calcium: Weight?,
    override val chloride: Weight?,
    override val iron: Weight?,
    override val magnesium: Weight?,
    override val phosphorous: Weight?,
    override val potassium: Weight?,
    override val sodium: Weight?,
    override val portion: Weight,
    @ColumnInfo(name = "food_energy")
    override val foodEnergy: FoodEnergy,
): NutritionInfo {
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

    @Query("DELETE FROM meal WHERE id IN (:ids)")
    suspend fun deleteMeals(ids: List<Long>)
}

private const val DB_VERSION = 2

@Database(
    entities = [MealEntity::class],
    version = DB_VERSION,
    exportSchema = false,
)
internal abstract class SavedMealLocalDb : RoomDatabase() {
    abstract fun getSavedMealDao(): SavedMealDao
}
