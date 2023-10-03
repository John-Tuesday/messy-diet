package org.calamarfederal.messydiet.food.data.central.model

import org.calamarfederal.messydiet.diet_data.model.FoodEnergyUnit
import org.calamarfederal.messydiet.diet_data.model.energyIn
import org.calamarfederal.messydiet.food.data.central.remote.schema.*
import org.calamarfederal.physical.measurement.Mass
import org.calamarfederal.physical.measurement.invoke

data class LegacyFdcId internal constructor(override val fdcId: Int) : FDCId
data class SurveyFdcId internal constructor(override val fdcId: Int) : FDCId

internal fun SearchResultFoodSchema.toFdcAbridgedFoodItemOrNull(): FDCAbridgedFoodItem? {
    return FDCAbridgedFoodItem(
        fdcId = when (dataType?.let { FDCDataType.fromString(it) }) {
            FDCDataType.Branded -> BrandedFDCId(fdcId)
            FDCDataType.Foundation -> FoundationFdcId(fdcId)
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

internal fun AbridgedFoodItemSchema.toModel(): FDCAbridgedFoodItem = FDCAbridgedFoodItem(
    fdcId = foodDataCentralId(id = fdcId, dataType = FDCDataType.fromString(dataType)!!),
    description = description,
    nutritionalInfo = foodNutrients?.toNutritionInfo()?.chooseBest(),
    publishedDate = parseDate(),
    brandOwner = brandOwner,
    gtinUPC = gtinUpc,
    ndbNumber = ndbNumber,
    foodCode = foodCode,
)

internal fun SRLegacyFoodItemSchema.toModel(): FDCAbridgedFoodItem {
    return FDCAbridgedFoodItem(
        fdcId = LegacyFdcId(fdcId),
        description = description,
        nutritionalInfo = foodNutrients?.toNutritionInfo()?.chooseBest(),
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

    val massUnit = nutrientSchema.unitName?.let { stringToMassUnitOrNull(it) } ?: return null
    val mass = nutrientSchema.amount?.let { Mass(it, massUnit) } ?: return null

    return when (nutrientSchema.number) {
        "203" -> copy(totalProtein = mass)
        "205" -> copy(totalCarbohydrates = mass)
        "605" -> copy(transFat = mass)
        "204" -> copy(totalFat = mass)
        "298" -> copy(totalFat = mass)
        "645" -> copy(monounsaturatedFat = mass)
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
