package org.calamarfederal.messydiet.feature.meal.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.calamarfederal.messydiet.feature.meal.data.local.MealEntity
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import javax.inject.Inject
import kotlin.random.Random

interface MealRepository {
    /**
     * Cold flow of [Meal] with [id] (emits when changed)
     */
    fun getMeal(id: Long): Flow<Meal?>

    /**
     * Cold flow of all [Meal] (emits when changed)
     */
    fun getAllMeals(): Flow<List<Meal>>

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
}

internal class MealRepositoryImplementation @Inject constructor(
    private val mealLocalSource: MealLocalSource,
) : MealRepository {
    override fun getMeal(id: Long): Flow<Meal?> = mealLocalSource.getMeal(id = id)

    override fun getAllMeals(): Flow<List<Meal>> = mealLocalSource.getAllMeals()

    override suspend fun insertMeal(meal: Meal, generateId: Boolean): Long =
        mealLocalSource.insertMeal(meal = meal, generateId = generateId)

    override suspend fun updateMeal(meal: Meal): Boolean = mealLocalSource.updateMeal(meal)

    override suspend fun insertOrUpdateMeal(meal: Meal) = mealLocalSource.insertOrUpdate(meal)

    override suspend fun deleteMeal(id: Long): Boolean = mealLocalSource.deleteMeal(id = id)
    override suspend fun deleteMeals(ids: List<Long>) = mealLocalSource.deleteMeals(ids = ids)

}
