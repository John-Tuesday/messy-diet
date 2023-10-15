package org.calamarfederal.messydiet.feature.search.data.repository

import io.github.john.tuesday.nutrition.FoodNutrition
import io.github.john.tuesday.nutrition.Portion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.calamarfederal.messydiet.feature.search.data.model.*
import org.calamarfederal.messydiet.feature.search.data.model.FoodDetailsStatus.*
import org.calamarfederal.messydiet.feature.search.data.model.SearchRemoteError.InvalidFoodIdError
import org.calamarfederal.messydiet.food.data.central.FoodDataCentralRepository
import org.calamarfederal.messydiet.food.data.central.model.FDCFoodItem
import org.calamarfederal.messydiet.food.data.central.model.fold
import org.calamarfederal.physical.measurement.grams
import org.calamarfederal.physical.measurement.kilocalories

interface FoodDetailsRepository {
    /**
     * Fetch food details using [foodId]
     */
    fun foodDetails(foodId: FoodId): Flow<FoodDetailsStatus>
}

internal fun FDCFoodItem.toFoodItemDetails(): FoodItemDetails = FoodItemDetails(
    name = description,
    foodId = FdcFoodId(fdcId),
    nutritionInfo = nutritionalInfo ?: FoodNutrition(portion = Portion(0.grams), foodEnergy = 0.kilocalories),
)

internal class FoodDetailsRepositoryImplementation(
    private val fdcRepo: FoodDataCentralRepository,
) : FoodDetailsRepository {
    override fun foodDetails(foodId: FoodId): Flow<FoodDetailsStatus> = flow {
        emit(Loading())
        when (foodId) {
            is FdcFoodId -> {
                val result = fdcRepo.getFoodDetails(foodId.fdcId)
                emit(
                    result.fold(
                        onSuccess = {
                            Success(it.toFoodItemDetails())
                        },
                        onFailure = {
                            Failure(it.toSearchRemoteError())
                        }
                    ))
            }

            is FoodIdDummy -> {
                emit(
                    Failure(
                        InvalidFoodIdError(
                            message = "Invalid food id given as input: id=${foodId.id}, type=${foodId.type}",
                            id = foodId.id,
                            type = foodId.type,
                        )
                    )
                )

            }
        }
    }
}
