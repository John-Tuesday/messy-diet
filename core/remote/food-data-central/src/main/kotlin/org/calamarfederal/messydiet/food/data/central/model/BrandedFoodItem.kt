package org.calamarfederal.messydiet.food.data.central.model

import org.calamarfederal.messydiet.diet_data.model.kcal
import org.calamarfederal.messydiet.food.data.central.remote.schema.BrandedFoodItemSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.BrandedFoodItemLabelNutrientsSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.SearchResultFoodSchema
import org.calamarfederal.messydiet.measure.Weight
import org.calamarfederal.messydiet.measure.grams
import org.calamarfederal.messydiet.measure.weightIn


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

internal fun BrandedFoodItemSchema.toModel(): BrandedFDCFoodItem = BrandedFDCFoodItem(
    fdcId = BrandedFDCId(fdcId),
    description = description,
    nutritionalInfo = foodNutrients?.toNutritionInfo(),
    ingredients = ingredients,
    brandOwner = brandOwner,
    upcGTIN = gtinUpc,
)

private fun convertAPINutrients(
    servingSize: Weight,
    response: BrandedFoodItemLabelNutrientsSchema,
): FDCNutritionInfo {

    return FDCNutritionInfo(
        totalProtein = response.protein?.number?.grams ?: 0.grams,
        fiber = response.fiber?.number?.grams,
        sugar = response.sugars?.number?.grams,
        sugarAlcohol = null,
        starch = null,
        totalCarbohydrates = response.carbohydrates?.number?.grams ?: 0.grams,
        monounsaturatedFat = null,
        polyunsaturatedFat = null,
        omega3 = null,
        omega6 = null,
        saturatedFat = response.saturatedFat?.number?.grams,
        transFat = response.transFat?.number?.grams,
        cholesterol = response.cholesterol?.number?.grams,
        totalFat = response.fat?.number?.grams ?: 0.grams,
        foodEnergy = response.calories?.number?.kcal ?: 0.kcal,
        portion = servingSize,
    )
}

private fun responseToFoodItem(
    response: BrandedFoodItemSchema,
): BrandedFDCFoodItem {
    return BrandedFDCFoodItem(
        fdcId = BrandedFDCId(response.fdcId),
        nutritionalInfo = response.labelNutrients?.let {
            convertAPINutrients(
                response.servingSize!!.weightIn(stringToWeightUnit(response.servingSizeUnit!!)!!),
                it
            )
        },
        description = response.description,
        ingredients = response.ingredients,
        brandOwner = response.brandOwner,
        upcGTIN = response.gtinUpc,
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
