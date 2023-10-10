package org.calamarfederal.messydiet.feature.meal.data.local

import androidx.room.*
import io.github.john.tuesday.nutrition.NutrientType
import kotlinx.coroutines.flow.Flow
import org.calamarfederal.physical.measurement.Mass

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
internal interface MealInfoDao {
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


@Dao
internal interface SavedMealDao : MealNutrientDao, MealInfoDao
