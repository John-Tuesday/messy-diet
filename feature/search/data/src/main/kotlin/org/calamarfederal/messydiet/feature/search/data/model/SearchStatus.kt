package org.calamarfederal.messydiet.feature.search.data.model

import androidx.compose.runtime.Stable
import org.calamarfederal.messydiet.diet_data.model.NutritionInfo
import org.calamarfederal.messydiet.food.data.central.model.FDCDataType
import org.calamarfederal.messydiet.food.data.central.model.FDCId
import org.calamarfederal.messydiet.food.data.central.model.dataType
import org.calamarfederal.messydiet.food.data.central.model.foodDataCentralId

@Stable
sealed interface FoodId {
    val id: Int
    val type: Int
}

fun foodId(id: Int, type: Int): FoodId {
    require(type in FDCDataType.entries.indices)

    return FdcFoodId(id = id, dataType = FDCDataType.entries[type])
}

data object FoodIdDummy : FoodId {
    override val id: Int = 0
    override val type: Int = -1
}

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

data class SearchResultFoodItem(
    val foodId: FoodId,
    val name: String,
    val nutritionInfo: NutritionInfo,
) {
    val id: Int = foodId.id
}

data class FoodItemDetails(
    val foodId: FoodId,
    val name: String,
    val nutritionInfo: NutritionInfo,
)

sealed class SearchStatus {
    data class Failure(val message: String) : SearchStatus()
    data object Loading : SearchStatus() {
        operator fun invoke() = this
    }

    data class Success(val results: List<SearchResultFoodItem>) : SearchStatus()
}

sealed class FoodDetailsStatus {
    data class Failure(val message: String) : FoodDetailsStatus()
    data object Loading : FoodDetailsStatus() {
        operator fun invoke() = this
    }

    data class Success(val results: FoodItemDetails) : FoodDetailsStatus()
}
