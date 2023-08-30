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
    fun getAllMeals(): Flow<List<Meal>>
    suspend fun insertMeal(meal: Meal, generateId: Boolean = true): Long
    suspend fun insertOrUpdateMeal(meal: Meal)
    suspend fun updateMeal(meal: Meal, abortIfNotFound: Boolean = true): Boolean

    /**
     * Delete [Meal] identified by [id] and return `true` if [Meal] was removed
     */
    suspend fun deleteMeal(id: Long): Boolean
}

internal class MealRepositoryImplementation @Inject constructor(
    private val mealLocalSource: MealLocalSource,
) : MealRepository {
    override fun getMeal(id: Long): Flow<Meal?> = mealLocalSource.getMeal(id = id)

    override fun getAllMeals(): Flow<List<Meal>> = mealLocalSource.getAllMeals()

    override suspend fun insertMeal(meal: Meal, generateId: Boolean): Long =
        mealLocalSource.insertMeal(meal = meal, generateId = generateId)

    override suspend fun updateMeal(meal: Meal, abortIfNotFound: Boolean): Boolean = mealLocalSource.updateMeal(meal)

    override suspend fun insertOrUpdateMeal(meal: Meal) = mealLocalSource.insertOrUpdate(meal)

    override suspend fun deleteMeal(id: Long): Boolean = mealLocalSource.deleteMeal(id = id)

}
