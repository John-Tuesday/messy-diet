package org.calamarfederal.messydiet.food.data.central.model

import org.calamarfederal.messydiet.diet_data.model.*
import org.calamarfederal.messydiet.food.data.central.remote.schema.DataTypeSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodNutrientSchema
import org.calamarfederal.messydiet.measure.WeightUnit
import org.calamarfederal.messydiet.measure.weightOf

internal fun stringToWeightUnit(
    text: String,
): WeightUnit? = when (val str = text.lowercase().trim()) {
    "g" -> WeightUnit.Gram
    "mg" -> WeightUnit.Milligram
    "kg" -> WeightUnit.Kilogram
    "oz" -> WeightUnit.Ounce
    "lbs" -> WeightUnit.Pound
    else -> null
}

internal fun stringToFoodEnergyUnit(
    text: String,
): FoodEnergyUnit? = when (val str = text.uppercase().trim()) {
    "KCAL" -> FoodEnergyUnit.Kilocalories
    else -> null
}

internal fun stringToDataType(text: String): FDCDataType? {
    return when (text.lowercase().trim()) {
        "foundation" -> FDCDataType.Foundation
        "branded" -> FDCDataType.Branded
        "survey (fndds)" -> FDCDataType.Survey
        "sr legacy" -> FDCDataType.Legacy
//        "experimental" -> FDCDataType.Experimental
        else -> null
    }
}

data class FDCDate(
    val year: Int,
    val month: Int,
    val day: Int,
)

enum class FDCDataType {
    Foundation,

    //    Experimental,
    Survey,
    Branded,
    Legacy,
}

internal val FDCDataType.schema: DataTypeSchema
    get() = when (this) {
        FDCDataType.Foundation -> DataTypeSchema.Foundation
        FDCDataType.Survey -> DataTypeSchema.SurveyFNDDS
        FDCDataType.Branded -> DataTypeSchema.Branded
        FDCDataType.Legacy -> DataTypeSchema.SRLegacy
    }


sealed interface FDCId {
    val fdcId: Int
}

val FDCId.dataType: FDCDataType get() = when(this) {
    is BrandedFDCId -> FDCDataType.Branded
    is SurveyFdcId -> FDCDataType.Survey
    is LegacyFdcId -> FDCDataType.Legacy
    is FoundationFdcId -> FDCDataType.Foundation
}

fun foodDataCentralId(
    id: Int,
    dataType: FDCDataType,
): FDCId = when(dataType) {
    FDCDataType.Foundation -> FoundationFdcId(id)
    FDCDataType.Survey -> SurveyFdcId(id)
    FDCDataType.Branded -> BrandedFDCId(id)
    FDCDataType.Legacy -> LegacyFdcId(id)
}

typealias FDCNutritionInfo = Nutrition
//data class FDCNutritionInfo internal constructor(
//    override val portion: Weight = 0.grams,
//    override val totalProtein: Weight = 0.grams,
//    override val fiber: Weight? = null,
//    override val sugar: Weight? = null,
//    override val sugarAlcohol: Weight? = null,
//    override val starch: Weight? = null,
//    override val totalCarbohydrates: Weight = 0.grams,
//    override val monounsaturatedFat: Weight? = null,
//    override val polyunsaturatedFat: Weight? = null,
//    override val omega3: Weight? = null,
//    override val omega6: Weight? = null,
//    override val saturatedFat: Weight? = null,
//    override val transFat: Weight? = null,
//    override val cholesterol: Weight? = null,
//    override val totalFat: Weight = 0.grams,
//    override val foodEnergy: FoodEnergy = 0.kcal,
//    override val calcium: Weight? = null,
//    override val chloride: Weight? = null,
//    override val iron: Weight? = null,
//    override val magnesium: Weight? = null,
//    override val phosphorous: Weight? = null,
//    override val potassium: Weight? = null,
//    override val sodium: Weight? = null,
//    override val vitaminC: Weight? = null,
//) : NutritionInfo

sealed interface FDCFoodItem {
    val fdcId: FDCId
    val description: String
    val nutritionalInfo: FDCNutritionInfo?
}

internal fun parseNutrientNumber(amount: Number, unitName: String, number: String): FDCNutritionInfo? {
    val weightUnit = stringToWeightUnit(unitName)
    val energyUnit = stringToFoodEnergyUnit(unitName)
    return when (number) {
        "203" -> FDCNutritionInfo(totalProtein = weightUnit!!.weightOf(amount))
        "204" -> FDCNutritionInfo(totalFat = weightUnit!!.weightOf(amount))
        "205" -> FDCNutritionInfo(totalCarbohydrates = weightUnit!!.weightOf(amount))
        "208" -> FDCNutritionInfo(foodEnergy = energyUnit!!.energyOf(amount))
        "262" -> FDCNutritionInfo(/*caffeine*/)
        "269" -> FDCNutritionInfo(sugar = weightUnit!!.weightOf(amount))
        "291" -> FDCNutritionInfo(fiber = weightUnit!!.weightOf(amount))
        "301" -> FDCNutritionInfo(calcium = weightUnit!!.weightOf(amount))
        "303" -> FDCNutritionInfo(iron = weightUnit!!.weightOf(amount))
        "307" -> FDCNutritionInfo(sodium = weightUnit!!.weightOf(amount))
        "401" -> FDCNutritionInfo(vitaminC = weightUnit!!.weightOf(amount))
        "539" -> FDCNutritionInfo(sugar = weightUnit!!.weightOf(amount)) // sugar added
        "601" -> FDCNutritionInfo(cholesterol = weightUnit!!.weightOf(amount))
        "605" -> FDCNutritionInfo(transFat = weightUnit!!.weightOf(amount))
        "606" -> FDCNutritionInfo(saturatedFat = weightUnit!!.weightOf(amount))
        else -> null
    }
}

internal fun FoodNutrientSchema.toNutritionInfo(): FDCNutritionInfo? {
    return parseNutrientNumber(
        amount = amount ?: return null,
        unitName = nutrient?.unitName ?: return null,
        number = nutrient.number ?: return null,
    )
}

internal fun List<FoodNutrientSchema>.toNutritionInfo(): FDCNutritionInfo =
    mapNotNull { it.toNutritionInfo() }
        .fold(FDCNutritionInfo()) { acc, nutrition -> acc + nutrition }
