package org.calamarfederal.messydiet.feature.meal.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.calamarfederal.messydiet.diet_data.model.*
import org.calamarfederal.physical.measurement.*
import kotlin.math.absoluteValue

internal class MealConverters {
    @TypeConverter
    fun fromWeightToDoubleOrNull(weight: Mass?): Double? = weight?.inGrams()

    @TypeConverter
    fun fromDoubleToWeightOrNull(grams: Double?): Mass? = grams?.grams

    @TypeConverter
    fun fromFoodEnergyToDoubleOrNull(energy: FoodEnergy?): Double? = energy?.inKilocalories()

    @TypeConverter
    fun fromDoubleToFoodEnergyOrNull(kcals: Double?): FoodEnergy? = kcals?.kcal

    @TypeConverter
    fun fromFoodEnergyToDouble(energy: FoodEnergy): Double = energy.inKilocalories()

    @TypeConverter
    fun fromDoubleToFoodEnergy(kcals: Double): FoodEnergy = kcals.kcal

    @TypeConverter
    fun fromWeightToLong(weight: Mass): Double = weight.inGrams()

    @TypeConverter
    fun fromDoubleToWeight(grams: Double): Mass = grams.grams

    @TypeConverter
    fun fromPortionToPair(portion: Portion): Double {
        portion.mass?.let { return it.inGrams() }
        portion.volume?.let { return -it.inLiters() }
        return 0.00
    }

    @TypeConverter
    fun fromPairToPortion(amount: Double): Portion = when {
        amount > 0 -> Portion(amount.absoluteValue.grams)
        amount < 0 -> Portion(amount.absoluteValue.liters)
        else -> Portion()
    }
}
@Entity(tableName = "meal")
@TypeConverters(MealConverters::class)
internal data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(index = true)
    val name: String,
    @ColumnInfo(name = "total_protein")
    override val totalProtein: Mass,
    override val fiber: Mass?,
    override val sugar: Mass?,
    @ColumnInfo(name = "sugar_alcohol")
    override val sugarAlcohol: Mass?,
    override val starch: Mass?,
    @ColumnInfo(name = "total_carbohydrates")
    override val totalCarbohydrates: Mass,
    override val monounsaturatedFat: Mass?,
    override val polyunsaturatedFat: Mass?,
    override val omega3: Mass?,
    override val omega6: Mass?,
    override val saturatedFat: Mass?,
    override val transFat: Mass?,
    override val cholesterol: Mass?,
    @ColumnInfo(name = "total_fat")
    override val totalFat: Mass,
    override val calcium: Mass?,
    override val chloride: Mass?,
    override val iron: Mass?,
    override val magnesium: Mass?,
    override val phosphorous: Mass?,
    override val potassium: Mass?,
    override val sodium: Mass?,
    override val portion: Portion,
    @ColumnInfo(name = "food_energy")
    override val foodEnergy: FoodEnergy,
) : NutritionInfo {
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

private const val DB_VERSION = 4

@Database(
    entities = [MealEntity::class],
    version = DB_VERSION,
    exportSchema = false,
)
internal abstract class SavedMealLocalDb : RoomDatabase() {
    abstract fun getSavedMealDao(): SavedMealDao
}
