package org.calamarfederal.messydiet.feature.search.data.model

import androidx.compose.runtime.Stable
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
