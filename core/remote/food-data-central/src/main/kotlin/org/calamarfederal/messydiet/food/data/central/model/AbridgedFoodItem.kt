package org.calamarfederal.messydiet.food.data.central.model

import org.calamarfederal.messydiet.diet_data.model.*
import org.calamarfederal.messydiet.food.data.central.remote.schema.*
import org.calamarfederal.messydiet.food.data.central.remote.schema.AbridgedFoodItemSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.AbridgedFoodNutrientSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.SearchResultFoodSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.parseResponseDate
import org.calamarfederal.messydiet.measure.weightIn

data class LegacyFdcId internal constructor(override val fdcId: Int): FDCId
data class SurveyFdcId internal constructor(override val fdcId: Int) : FDCId

internal fun SearchResultFoodSchema.toFdcAbridgedFoodItemOrNull(): FDCAbridgedFoodItem? {
    return FDCAbridgedFoodItem(
        fdcId = when(dataType?.let { stringToDataType(it) }) {
            FDCDataType.Branded -> BrandedFDCId(fdcId)
            FDCDataType.Foundation-> FoundationFdcId(fdcId)
            FDCDataType.Legacy -> LegacyFdcId(fdcId)
            FDCDataType.Survey -> SurveyFdcId(fdcId)
            null -> return null
        },
        description = description,
        nutritionalInfo = foodNutrients?.let { parseNutrients(it) },
        publishedDate = publicationDate?.let { parseResponseDate(it) },
        brandOwner = brandOwner,
        gtinUPC = gtinUpc,
        ndbNumber = ndbNumber,
        foodCode = foodCode,
    )
}

data class FDCAbridgedFoodItem internal constructor(
    override val fdcId: FDCId,
    override val description: String,
    override val nutritionalInfo: FDCNutritionInfo? = null,
    val publishedDate: FDCDate? = null,
    val brandOwner: String? = null,
    val gtinUPC: String? = null,
    val ndbNumber: Int? = null,
    val foodCode: String? = null,
) : FDCFoodItem

internal fun brandedFoodItemFrom(abridgedFoodItem: AbridgedFoodItemSchema): BrandedFDCFoodItem {
    return BrandedFDCFoodItem(
        fdcId = BrandedFDCId(abridgedFoodItem.fdcId),
        description = abridgedFoodItem.description,
        brandOwner = abridgedFoodItem.brandOwner,
        upcGTIN = abridgedFoodItem.gtinUpc,
    )
}

internal fun SRLegacyFoodItemSchema.toModel(): FDCAbridgedFoodItem{
    return FDCAbridgedFoodItem(
        fdcId = LegacyFdcId(fdcId),
        description = description,
        nutritionalInfo = foodNutrients?.toNutritionInfo(),
        publishedDate = publicationDate?.let { parseResponseDate(it) },
        brandOwner = null,
        gtinUPC = null,
        ndbNumber = ndbNumber,
        foodCode = null,
    )
}
internal fun SurveyFoodItemSchema.toModel(): FDCAbridgedFoodItem {
    return FDCAbridgedFoodItem(
        fdcId = LegacyFdcId(fdcId),
        description = description,
        nutritionalInfo = null,
        publishedDate = publicationDate?.let { parseResponseDate(it) },
        brandOwner = null,
        gtinUPC = null,
        ndbNumber = null,
        foodCode = foodCode,
    )
}

internal fun FDCNutritionInfo.parseNutrient(nutrientSchema: AbridgedFoodNutrientSchema): FDCNutritionInfo? {
    if (nutrientSchema.number == "208") {
        val energyUnit = if (nutrientSchema.unitName != "kcal") return null else FoodEnergyUnit.Kilocalories
        val calories = nutrientSchema.amount?.energyIn(energyUnit) ?: return null
        return copy(foodEnergy = calories)
    }

    val weightUnit = nutrientSchema.unitName?.let { stringToWeightUnit(it) } ?: return null
    val weight = nutrientSchema.amount?.weightIn(weightUnit) ?: return null

    return when(nutrientSchema.number) {
        "203" -> copy(totalProtein = weight)
        "205" -> copy(totalCarbohydrates = weight)
        "605" -> copy(transFat = weight)
        "204" -> copy(totalFat = weight)
        "298" -> copy(totalFat = weight)
        "645" -> copy(monounsaturatedFat = weight)
        else -> null
    }
}

internal fun parseNutrients(foodNutrients: List<AbridgedFoodNutrientSchema>): FDCNutritionInfo {
    var nutrition = FDCNutritionInfo()
    for (n in foodNutrients) {
        nutrition = nutrition.parseNutrient(n) ?: nutrition
    }
    return nutrition
}
