package org.calamarfederal.messydiet.food.data.central

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.calamarfederal.messydiet.food.data.central.model.*

interface FoodDataCentralRepository {
    /**
     * Search for a food item by its UPC or GTIN
     *
     * implicitly searches Branded only
     */
    suspend fun searchFoodWithUpcGtin(upcGtin: String): ResultResponse<List<FDCFoodItem>, FoodDataCentralError>

    /**
     * General search
     */
    suspend fun searchFood(criteria: SearchCriteriaBuilder): ResultResponse<List<FDCFoodItem>, FoodDataCentralError>

    /**
     * uses [fdcId] to deduce data type and returns the full details
     */
    suspend fun getFoodDetails(fdcId: FDCId): ResultResponse<FDCFoodItem, FoodDataCentralError>
}

internal class FoodDataCentralRepositoryImplementation(
    private val dispatcher: CoroutineDispatcher,
    private val fdcSource: FoodDataCentralRemoteSource,
) : FoodDataCentralRepository {
    override suspend fun searchFoodWithUpcGtin(upcGtin: String): ResultResponse<List<FDCFoodItem>, FoodDataCentralError> =
        withContext(dispatcher) {
            val result = fdcSource.searchFood {
                query = searchQuery { withUpc(upcGtin) }
                dataType = listOf(FDCDataType.Branded)
            }

            result.fold(
                onSuccess = {
                    ResultResponse.Success(it.foodItems)
                },
                onFailure = {
                    ResultResponse.Failure(it)
                }
            )
        }

    override suspend fun searchFood(criteria: SearchCriteriaBuilder): ResultResponse<List<FDCFoodItem>, FoodDataCentralError> =
        withContext(dispatcher) {
            fdcSource.searchFoodNormal(criteria).fold(
                onSuccess = {
                    ResultResponse.Success(it.foodItems)
                },
                onFailure = {
                    ResultResponse.Failure(it)
                }
            )
        }

    override suspend fun getFoodDetails(fdcId: FDCId): ResultResponse<FDCFoodItem, FoodDataCentralError> =
        withContext(dispatcher) {
            val result = fdcSource.getFoodByFdcId(fdcId)

            if (result.getResponseOrNull() is FoodDataCentralError.NotFoundError)
                fdcSource.getFoodByFdcId(fdcId, abridged = true)
            else
                result
        }
}
