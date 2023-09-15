package org.calamarfederal.messydiet.food.data.central.model

import org.calamarfederal.messydiet.food.data.central.remote.schema.FoundationFoodItemSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.SearchResultFoodSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.parseResponseDate

data class FDCFoundationFoodItem internal constructor(
    override val fdcId: FoundationFdcId,
    override val description: String,
    override val nutritionalInfo: FDCNutritionInfo?,
    val ndbNumber: Int? = null,
    val publicationDate: FDCDate? = null,
) : FDCFoodItem

data class FoundationFdcId internal constructor(
    override val fdcId: Int,
) : FDCId

internal fun FoundationFoodItemSchema.toModel(): FDCFoundationFoodItem = FDCFoundationFoodItem(
    fdcId = FoundationFdcId(fdcId),
    description = description,
    nutritionalInfo = foodNutrients?.toNutritionInfo()?.chooseBest(),
    ndbNumber = ndbNumber,
    publicationDate = publicationDate?.let { parseResponseDate(it) },
)

internal fun SearchResultFoodSchema.toFDCFoundationFoodOrNull(): FDCFoundationFoodItem? {
    if (dataType?.let { FDCDataType.fromString(it) } != FDCDataType.Foundation)
        return null

    return FDCFoundationFoodItem(
        fdcId = FoundationFdcId(fdcId),
        description = description,
        nutritionalInfo = foodNutrients?.let { parseNutrients(it) },
        ndbNumber = ndbNumber,
        publicationDate = publicationDate?.let { parseResponseDate(it) },
    )
}
