package org.calamarfederal.messydiet.feature.search.data.model

import io.github.john.tuesday.nutrition.FoodNutrition

/**
 * Result from a successful remote search
 */
data class SearchResultFoodItem(
    val foodId: FoodId,
    val name: String,
    val nutritionInfo: FoodNutrition,
) {
    /**
     * Convenience for `foodId.id`
     */
    val id: Int = foodId.id
}

/**
 * Result from a successful remote getDetails
 */
data class FoodItemDetails(
    val foodId: FoodId,
    val name: String,
    val nutritionInfo: FoodNutrition,
)

sealed class SearchStatus {
    data class Failure(val remoteError: SearchRemoteError) : SearchStatus()
    data object Loading : SearchStatus() {
        operator fun invoke() = this
    }

    data class Success(val results: List<SearchResultFoodItem>) : SearchStatus()
}

sealed class FoodDetailsStatus {
    data class Failure(val remoteError: SearchRemoteError) : FoodDetailsStatus()
    data object Loading : FoodDetailsStatus() {
        operator fun invoke() = this
    }

    data class Success(val results: FoodItemDetails) : FoodDetailsStatus()
}
