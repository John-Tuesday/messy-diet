package org.calamarfederal.messydiet.feature.search.data.model

import androidx.compose.runtime.Stable
import io.github.john.tuesday.nutrition.FoodNutrition
import org.calamarfederal.messydiet.food.data.central.model.FDCDataType
import org.calamarfederal.messydiet.food.data.central.model.FDCId
import org.calamarfederal.messydiet.food.data.central.model.dataType
import org.calamarfederal.messydiet.food.data.central.model.foodDataCentralId

/**
 * Food ID for fetching remote food items
 *
 * save [id] and [type] to restore it later using [foodId]
 */
@Stable
sealed interface FoodId {
    val id: Int
    val type: Int
}

/**
 * Construct a new [FoodId] using the saved [id] and [type]
 */
fun foodId(id: Int, type: Int): FoodId {
    require(type in FDCDataType.entries.indices)

    return FdcFoodId(id = id, dataType = FDCDataType.entries[type])
}

/**
 * Implementation of [FoodId] strictly for compose preview
 */
data object FoodIdDummy : FoodId {
    override val id: Int = 0
    override val type: Int = -1
}

/**
 * Food IDs from Food Data Central
 */
internal class FdcFoodId(
    override val id: Int,
    val dataType: FDCDataType,
    val fdcId: FDCId = foodDataCentralId(id = id, dataType = dataType),
) : FoodId {
    constructor(fdcId: FDCId) : this(id = fdcId.fdcId, dataType = fdcId.dataType, fdcId = fdcId)

    override fun equals(other: Any?): Boolean {
        if (other is FoodId)
            return id == other.id && type == other.type

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + dataType.hashCode()
        result = 31 * result + fdcId.hashCode()
        result = 31 * result + type
        return result
    }

    override val type: Int = dataType.ordinal
}

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

/**
 * All known non-fatal errors for this module
 */
sealed interface SearchRemoteError {
    data class UnknownNetworkError(
        override val message: String?,
        val code: Int,
    ) : SearchRemoteError

    data class NetworkTimeoutError(
        override val message: String?,
    ) : SearchRemoteError

    data class NotFoundError(
        override val message: String?,
    ) : SearchRemoteError

    data class OverRateLimitError(
        override val message: String?,
    ) : SearchRemoteError

    data class InvalidFoodIdError(
        override val message: String?,
        val id: Int,
        val type: Int,
    ) : SearchRemoteError

    data class InternalApiError(
        override val message: String?,
        val cause: Throwable? = null,
    ) : SearchRemoteError

    val message: String?
}

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
