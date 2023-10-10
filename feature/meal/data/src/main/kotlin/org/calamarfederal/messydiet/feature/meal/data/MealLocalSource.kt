package org.calamarfederal.messydiet.feature.meal.data

import androidx.room.withTransaction
import io.github.john.tuesday.nutrition.FoodNutrition
import io.github.john.tuesday.nutrition.NutritionMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.calamarfederal.messydiet.core.android.hilt.IODispatcher
import org.calamarfederal.messydiet.feature.meal.data.local.MealEntity
import org.calamarfederal.messydiet.feature.meal.data.local.MealNutrientEntity
import org.calamarfederal.messydiet.feature.meal.data.local.SavedMealDao
import org.calamarfederal.messydiet.feature.meal.data.local.SavedMealLocalDb
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import org.calamarfederal.messydiet.feature.meal.data.model.MealInfo
import javax.inject.Inject

sealed class MealDatabaseError : Throwable()
class TransactionError : MealDatabaseError()

internal interface MealLocalSource {
    /**
     * Cold [Flow] of all saved ids. emits on changes to the ids only. (applies [Flow.distinctUntilChanged])
     */
    fun getMealIds(): Flow<List<Long>>

    /**
     * Watch [Meal] (with [Flow.distinctUntilChanged] applied)
     */
    fun getMeal(id: Long): Flow<Meal?>

    suspend fun findMealInfoByName(name: String): MealInfo?

    fun getAllMealInfo(): Flow<List<MealInfo>>

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

internal fun Meal.toMealEntity(): MealEntity = MealEntity(
    id = id,
    name = name,
    portion = foodNutrition.portion,
    foodEnergy = foodNutrition.foodEnergy,
)

internal fun Meal.toNutrientEntity(): List<MealNutrientEntity> = foodNutrition.nutrients.map { (type, amount) ->
    MealNutrientEntity(
        mealId = id,
        nutrientType = type,
        amount = amount,
    )
}

internal fun MealEntity.toMealInfo(): MealInfo = Meal(
    id = id,
    name = name,
)

internal class MealLocalSourceImplementation @Inject constructor(
    private val db: SavedMealLocalDb,
    private val dao: SavedMealDao,
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
) : MealLocalSource {
    operator fun FoodNutrition.Companion.invoke(meal: MealEntity, nutrients: NutritionMap): FoodNutrition {
        return FoodNutrition(portion = meal.portion, foodEnergy = meal.foodEnergy, nutritionMap = nutrients)
    }

    override fun getMealIds(): Flow<List<Long>> = dao.getAllMealIds()
        .distinctUntilChanged()
        .flowOn(ioDispatcher)

    override fun getMeal(id: Long): Flow<Meal?> {
        return dao.getMeal(id = id)
            .distinctUntilChanged()
            .combine(dao.getAllNutrientsOf(mealId = id).distinctUntilChanged()) { meal, nutrients ->
                meal?.let {
                    Meal(
                        id = meal.id,
                        name = meal.name,
                        foodNutrition = FoodNutrition(meal = meal, nutrients = nutrients)
                    )
                }
            }.flowOn(ioDispatcher)
    }

    override suspend fun findMealInfoByName(name: String): MealInfo? = withContext(ioDispatcher) {
        dao.findMealByName(name)?.toMealInfo()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllMealInfo(): Flow<List<MealInfo>> {
        return dao
            .getAllMeals()
            .distinctUntilChanged()
            .mapLatest { it.map { it.toMealInfo() } }
            .flowOn(ioDispatcher)
    }

    override suspend fun updateMeal(meal: Meal): Boolean = withContext(ioDispatcher) {
        val result = runCatching {
            db.withTransaction {
                if (dao.updateMeal(meal.toMealEntity()) == 0) throw (TransactionError())
                val nutrients = meal.toNutrientEntity()
                if (dao.updateNutrients(nutrients) != nutrients.size) throw (TransactionError())
            }
        }

        return@withContext result.isSuccess
    }

    override suspend fun insertOrUpdate(meal: Meal) {
        withContext(ioDispatcher) {
            db.withTransaction {
                dao.upsertMeal(meal.toMealEntity())
                dao.upsertNutrients(meal.toNutrientEntity())
            }
        }
    }

    override suspend fun insertMeal(meal: Meal, generateId: Boolean): Long = withContext(ioDispatcher) {
        val result = runCatching {
            var id: Long = -1L
            db.withTransaction {
                val mealAlt = if (generateId) meal.copy(id = MealEntity.UNSET_ID) else meal
                id = dao.insertMeal(mealAlt.toMealEntity())
                if (id == -1L) throw (TransactionError())
                val nutrients = mealAlt.copy(id = id).toNutrientEntity()
                if (dao.insertNutrients(nutrients).size != nutrients.size) throw (TransactionError())

            }
            id
        }

        return@withContext result.fold(
            onSuccess = { it },
            onFailure = { if (it is TransactionError) -1L else throw (it) })
    }

    override suspend fun deleteMeal(id: Long): Boolean = withContext(ioDispatcher) {
        db.withTransaction {
            dao.deleteAllNutrientsOf(mealIds = listOf(id))
            dao.deleteMeal(id = id) > 0
        }
    }

    override suspend fun deleteMeals(ids: List<Long>) {
        withContext(ioDispatcher) {
            db.withTransaction {
                dao.deleteAllNutrientsOf(ids)
                dao.deleteMeals(ids)
            }
        }
    }
}
