package org.calamarfederal.messydiet.feature.search.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.feature.search.data.model.*
import org.calamarfederal.messydiet.food.data.central.FoodDataCentralRepository
import org.calamarfederal.messydiet.food.data.central.model.FDCFoodItem
import org.calamarfederal.messydiet.food.data.central.model.fold
import javax.inject.Inject

interface FoodSearchRepository {
    fun searchWithUpcGtin(upcGtin: String): Flow<SearchStatus>
}

interface FoodDetailsRepository {
    fun foodDetails(foodId: FoodId): Flow<FoodDetailsStatus>
}

internal fun FDCFoodItem.toSearchResultFoodItem(): SearchResultFoodItem = SearchResultFoodItem(
    name = description,
    foodId = FdcFoodId(fdcId),
    nutritionInfo = nutritionalInfo ?: Nutrition(),
)

internal class FoodSearchRepositoryImplementation @Inject constructor(
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
                    SearchStatus.Failure(it.message ?: "Unknown external error")
                }
            )
        )
    }
}

internal fun FDCFoodItem.toFoodItemDetails(): FoodItemDetails = FoodItemDetails(
    name = description,
    foodId = FdcFoodId(fdcId),
    nutritionInfo = nutritionalInfo ?: Nutrition(),
)

internal class FoodDetailsRepositoryImplementation @Inject constructor(
    private val fdcRepo: FoodDataCentralRepository,
) : FoodDetailsRepository {
    override fun foodDetails(foodId: FoodId): Flow<FoodDetailsStatus> = flow {
        emit(FoodDetailsStatus.Loading())
        if (foodId is FdcFoodId) {
            val result = fdcRepo.getFoodDetails(foodId.fdcId)
            emit(result.fold(
                onSuccess = {
                    FoodDetailsStatus.Success(it.toFoodItemDetails())
                },
                onFailure = {
                    FoodDetailsStatus.Failure(it.message ?: "Unknown external error")
                }
            ))
        } else {
            emit(FoodDetailsStatus.Failure("Food Id not recognized"))
        }
    }
}
