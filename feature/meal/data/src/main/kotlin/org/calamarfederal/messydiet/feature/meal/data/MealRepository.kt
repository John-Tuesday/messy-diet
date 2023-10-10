package org.calamarfederal.messydiet.feature.meal.data

import kotlinx.coroutines.flow.Flow
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import org.calamarfederal.messydiet.feature.meal.data.model.MealInfo
import javax.inject.Inject

interface MealRepository {
    /**
     * Cold flow of [Meal] with [id] (emits when changed)
     */
    fun getMeal(id: Long): Flow<Meal?>

    suspend fun findMealInfoByName(name: String): MealInfo?

    fun getAllMealInfo(): Flow<List<MealInfo>>

    /**
     * insert [meal]. Returns `true` iff [meal] is successfully added. No change on failure
     *
     * if [generateId] is `true` [meal]'s id will be ignored and a new one will be generated
     * otherwise [meal] is added 'as-is'
     */
    suspend fun insertMeal(meal: Meal, generateId: Boolean = true): Long

    /**
     * insert or replace meal with matching [Meal.id] with [meal]
     *
     * if [meal] has `0L` a new id will be generated
     */
    suspend fun insertOrUpdateMeal(meal: Meal)

    /**
     * find matching [Meal] by [Meal.id] and return if found and atomically updated.
     */
    suspend fun updateMeal(meal: Meal): Boolean

    /**
     * Delete [Meal] identified by [id] and return `true` if [Meal] was removed
     */
    suspend fun deleteMeal(id: Long): Boolean

    /**
     * Delete every [Meal] whose [Meal.id] is contained in [ids]
     */
    suspend fun deleteMeals(ids: List<Long>)

    suspend fun isMealValidUpsert(meal: Meal): Boolean
}

internal class MealRepositoryImplementation @Inject constructor(
    private val mealLocalSource: MealLocalSource,
) : MealRepository {
    override fun getMeal(id: Long): Flow<Meal?> = mealLocalSource.getMeal(id = id)

    override suspend fun findMealInfoByName(name: String): MealInfo? = mealLocalSource.findMealInfoByName(name)

    override fun getAllMealInfo(): Flow<List<MealInfo>> = mealLocalSource.getAllMealInfo()

    override suspend fun insertMeal(meal: Meal, generateId: Boolean): Long =
        mealLocalSource.insertMeal(meal = meal, generateId = generateId)

    override suspend fun updateMeal(meal: Meal): Boolean = mealLocalSource.updateMeal(meal)

    override suspend fun insertOrUpdateMeal(meal: Meal) = mealLocalSource.insertOrUpdate(meal)

    override suspend fun deleteMeal(id: Long): Boolean = mealLocalSource.deleteMeal(id = id)
    override suspend fun deleteMeals(ids: List<Long>) = mealLocalSource.deleteMeals(ids = ids)

    override suspend fun isMealValidUpsert(meal: Meal): Boolean {
        val result = findMealInfoByName(meal.name)
        return result == null || result.id == meal.id
    }
}
