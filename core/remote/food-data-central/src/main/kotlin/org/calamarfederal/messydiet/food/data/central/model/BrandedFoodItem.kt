package org.calamarfederal.messydiet.food.data.central.model

import org.calamarfederal.messydiet.diet_data.model.Portion
import org.calamarfederal.messydiet.food.data.central.remote.schema.BrandedFoodItemSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.SearchResultFoodSchema
import org.calamarfederal.messydiet.measure.grams
import org.calamarfederal.messydiet.measure.milliliters


data class BrandedFDCId internal constructor(
    override val fdcId: Int,
) : FDCId

data class BrandedFDCFoodItem internal constructor(
    override val fdcId: BrandedFDCId,
    override val nutritionalInfo: FDCNutritionInfo? = null,
    override val description: String,
    val ingredients: String? = null,
    val brandOwner: String? = null,
    val upcGTIN: String? = null,
) : FDCFoodItem {
    val upc: String? get() = upcGTIN
    val gtin: String? get() = upcGTIN
}

internal fun BrandedFoodItemSchema.toModel(): BrandedFDCFoodItem {
    val servingSize = when (servingSizeUnit?.lowercase()) {
        "mlt" -> Portion(servingSize!!.milliliters)
        "grm" -> Portion(servingSize!!.grams)
        null -> null
        else -> throw (Throwable("Unsupported serving size schema", IllegalStateException()))
    }
    val nutrition = foodNutrients?.toNutritionInfo(servingSize ?: Portion(100.grams))?.chooseBest()
    return BrandedFDCFoodItem(
        fdcId = BrandedFDCId(fdcId),
        description = description,
        nutritionalInfo = if (nutrition != null && servingSize != null) nutrition.scaleToPortion(servingSize) else nutrition,
        ingredients = ingredients,
        brandOwner = brandOwner,
        upcGTIN = gtinUpc,
    )
}

internal fun SearchResultFoodSchema.toFDCBrandedOrNull(): BrandedFDCFoodItem? {
    if (dataType?.let { stringToDataType(it) } != FDCDataType.Branded)
        return null

    return BrandedFDCFoodItem(
        fdcId = BrandedFDCId(fdcId),
        nutritionalInfo = foodNutrients?.let { parseNutrients(it) },
        description = description,
        ingredients = ingredients,
        brandOwner = brandOwner,
        upcGTIN = gtinUpc,
    )
}
