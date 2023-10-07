package org.calamarfederal.messydiet.food.data.central.model

import io.github.john.tuesday.nutrition.NutrientType
import io.github.john.tuesday.nutrition.Portion
import io.github.john.tuesday.nutrition.mutate
import org.calamarfederal.messydiet.food.data.central.remote.schema.*
import org.calamarfederal.physical.measurement.*

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
        val energyUnit = if (nutrientSchema.unitName != "kcal") return null else EnergyUnit.Kilocalorie
        val calories = nutrientSchema.amount?.let { Energy(it, energyUnit) } ?: return null
        return mutate(foodEnergy = calories)
    }

    val massUnit = nutrientSchema.unitName?.let { stringToMassUnitOrNull(it) } ?: return null
    val mass = nutrientSchema.amount?.let { Mass(it, massUnit) } ?: return null

    val type = when (nutrientSchema.number) {
        "203" -> NutrientType.Protein
        "205" -> NutrientType.TotalCarbohydrate
        "605" -> NutrientType.TransFat
        "204" -> NutrientType.TotalFat
        "298" -> NutrientType.TotalFat
        "645" -> NutrientType.MonounsaturatedFat
        else -> return null
    }
    return mutate(nutritionMap = nutrients.toMutableMap().apply { put(type, mass) })
}

internal fun parseNutrients(foodNutrients: List<AbridgedFoodNutrientSchema>): FDCNutritionInfo {
    var nutrition = FDCNutritionInfo(Portion(0.grams), 0.kilocalories, mapOf())
    for (n in foodNutrients) {
        nutrition = nutrition.parseNutrient(n) ?: nutrition
    }
    return nutrition
}
