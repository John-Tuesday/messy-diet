package org.calamarfederal.messydiet.feature.search.data

import io.github.john.tuesday.nutrition.FoodNutrition
import io.github.john.tuesday.nutrition.Portion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.calamarfederal.messydiet.feature.search.data.model.*
import org.calamarfederal.messydiet.food.data.central.FoodDataCentralRepository
import org.calamarfederal.messydiet.food.data.central.model.FDCFoodItem
import org.calamarfederal.messydiet.food.data.central.model.FoodDataCentralError
import org.calamarfederal.messydiet.food.data.central.model.fold
import org.calamarfederal.physical.measurement.grams
import org.calamarfederal.physical.measurement.kilocalories

/**
 * Search (remote) food items
 */
interface FoodSearchRepository {
    /**
     * Search for food items using it's GTIN or UPC (barcode)
     */
    fun searchWithUpcGtin(upcGtin: String): Flow<SearchStatus>
}

/**
 * Fetch (remote) details on a known food item
 */
interface FoodDetailsRepository {
    /**
     * Fetch food details using [foodId]
     */
    fun foodDetails(foodId: FoodId): Flow<FoodDetailsStatus>
}

internal fun FoodDataCentralError.toSearchRemoteError(): SearchRemoteError = when (this) {
    is FoodDataCentralError.NotFoundError -> SearchRemoteError.NotFoundError(message)
    is FoodDataCentralError.OverRateLimitError -> SearchRemoteError.OverRateLimitError(message)
    is FoodDataCentralError.NetworkError -> SearchRemoteError.UnknownNetworkError(message, code)
    is FoodDataCentralError.ParseErrorType -> SearchRemoteError.InternalApiError(message = message, cause = this)
}

internal fun FDCFoodItem.toSearchResultFoodItem(): SearchResultFoodItem = SearchResultFoodItem(
    name = description,
    foodId = FdcFoodId(fdcId),
    nutritionInfo = nutritionalInfo ?: FoodNutrition(portion = Portion(0.grams), foodEnergy = 0.kilocalories),
)

internal class FoodSearchRepositoryImplementation(
    private val fdcRepo: FoodDataCentralRepository,
) : FoodSearchRepository {
    override fun searchWithUpcGtin(upcGtin: String): Flow<SearchStatus> = flow {
        emit(SearchStatus.Loading())
        val result = fdcRepo.searchFoodWithUpcGtin(upcGtin = upcGtin)
        emit(
            result.fold(
                onSuccess = {
                    SearchStatus.Success(it.map { it.toSearchResultFoodItem() })
                },
                onFailure = {
                    SearchStatus.Failure(it.toSearchRemoteError())
                }
            )
        )
    }
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
        emit(FoodDetailsStatus.Loading())
        when (foodId) {
            is FdcFoodId -> {
                val result = fdcRepo.getFoodDetails(foodId.fdcId)
                emit(
                    result.fold(
                        onSuccess = {
                            FoodDetailsStatus.Success(it.toFoodItemDetails())
                    },
                    onFailure = {
                        FoodDetailsStatus.Failure(it.toSearchRemoteError())
                    }
                ))
            }

            is FoodIdDummy -> {
                emit(
                    FoodDetailsStatus.Failure(
                        SearchRemoteError.InvalidFoodIdError(
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
