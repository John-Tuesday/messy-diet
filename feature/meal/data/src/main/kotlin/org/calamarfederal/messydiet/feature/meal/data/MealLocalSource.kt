package org.calamarfederal.messydiet.feature.meal.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import org.calamarfederal.messydiet.core.android.hilt.IODispatcher
import org.calamarfederal.messydiet.feature.meal.data.local.MealEntity
import org.calamarfederal.messydiet.feature.meal.data.local.SavedMealDao
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import javax.inject.Inject

internal interface MealLocalSource {
    /**
     * Cold [Flow] of all saved ids. emits on changes to the ids only. (applies [Flow.distinctUntilChanged])
     */
    fun getMealIds(): Flow<List<Long>>

    /**
     * Watch [Meal] (with [Flow.distinctUntilChanged] applied)
     */
    fun getMeal(id: Long): Flow<Meal?>

    fun getAllMeals(): Flow<List<Meal>>

    /**
     * find matching [Meal] by [Meal.id] and return if found and atomically updated.
     */
    suspend fun updateMeal(meal: Meal): Boolean

    /**
     * insert or replace meal with matching [Meal.id] with [meal]
     *
     * if [meal] has `0L` a new id will be generated
     */
    suspend fun insertOrUpdate(meal: Meal)

    /**
     * insert [meal]. Returns `true` iff [meal] is successfully added. No change on failure
     */
    suspend fun insertMeal(meal: Meal, generateId: Boolean = true): Long

    /**
     * find and delete [Meal] by [id] returns `true` iff [Meal] is found and removed
     */
    suspend fun deleteMeal(id: Long): Boolean
    suspend fun deleteMeals(ids: List<Long>)
}

internal fun MealEntity.toMeal(): Meal = Meal(
    id = id,
    name = name,
    totalProtein = totalProtein,
    fiber = fiber,
    sugar = sugar,
    sugarAlcohol = sugarAlcohol,
    starch = starch,
    totalCarbohydrates = totalCarbohydrates,
    monounsaturatedFat = monounsaturatedFat,
    polyunsaturatedFat = polyunsaturatedFat,
    omega3 = omega3,
    omega6 = omega6,
    saturatedFat = saturatedFat,
    transFat = transFat,
    cholesterol = cholesterol,
    totalFat = totalFat,
    calcium = calcium,
    chloride = chloride,
    iron = iron,
    magnesium = magnesium,
    phosphorous = phosphorous,
    potassium = potassium,
    sodium = sodium,
    portion = portion,
    foodEnergy = foodEnergy
)

internal fun Meal.toMealEntity(): MealEntity = MealEntity(
    id = id,
    name = name,
    totalProtein = totalProtein,
    fiber = fiber,
    sugar = sugar,
    sugarAlcohol = sugarAlcohol,
    starch = starch,
    totalCarbohydrates = totalCarbohydrates,
    monounsaturatedFat = monounsaturatedFat,
    polyunsaturatedFat = polyunsaturatedFat,
    omega3 = omega3,
    omega6 = omega6,
    saturatedFat = saturatedFat,
    transFat = transFat,
    cholesterol = cholesterol,
    totalFat = totalFat,
    calcium = calcium,
    chloride = chloride,
    iron = iron,
    magnesium = magnesium,
    phosphorous = phosphorous,
    potassium = potassium,
    sodium = sodium,
    portion = portion,
    foodEnergy = foodEnergy
)

internal class MealLocalSourceImplementation @Inject constructor(
    private val dao: SavedMealDao,
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
) : MealLocalSource {
    override fun getMealIds(): Flow<List<Long>> = dao.getAllMealIds()
        .distinctUntilChanged()
        .flowOn(ioDispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMeal(id: Long): Flow<Meal?> = dao
        .getMeal(id = id)
        .distinctUntilChanged()
        .mapLatest { it?.toMeal() }
        .flowOn(ioDispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllMeals(): Flow<List<Meal>> = dao
            .getAlMeals()
            .distinctUntilChanged()
            .mapLatest { it.map { it.toMeal() } }
            .flowOn(ioDispatcher)

    override suspend fun updateMeal(meal: Meal): Boolean = withContext(ioDispatcher) {
        dao.updateMeal(meal = meal.toMealEntity()) != 0
    }

    override suspend fun insertOrUpdate(meal: Meal) {
        dao.upsertMeal(meal = meal.toMealEntity())
    }

    override suspend fun insertMeal(meal: Meal, generateId: Boolean): Long = withContext(ioDispatcher) {
        val mealEntity = meal.toMealEntity().let { if (generateId) it.copy(id = MealEntity.UNSET_ID) else it }
        dao.insertMeal(meal = mealEntity)
    }

    override suspend fun deleteMeal(id: Long): Boolean = withContext(ioDispatcher) {
        dao.deleteMeal(id = id) > 0
    }

    override suspend fun deleteMeals(ids: List<Long>) {
        return dao.deleteMeals(ids)
    }
}
