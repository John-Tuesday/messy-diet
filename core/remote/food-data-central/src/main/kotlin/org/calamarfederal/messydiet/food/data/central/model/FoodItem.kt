package org.calamarfederal.messydiet.food.data.central.model

import org.calamarfederal.messydiet.diet_data.model.*
import org.calamarfederal.messydiet.food.data.central.remote.schema.DataTypeSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodNutrientSchema
import org.calamarfederal.messydiet.measure.WeightUnit
import org.calamarfederal.messydiet.measure.grams
import org.calamarfederal.messydiet.measure.milliliters
import org.calamarfederal.messydiet.measure.weightOf

class UnrecognizedWeightUnitFormat(override val cause: Throwable? = null, override val message: String? = null) :
    Throwable()

internal fun stringToWeightUnitOrNull(
    text: String,
): WeightUnit? = when (val str = text.lowercase().trim()) {
    "µg" -> WeightUnit.Micrograms
    "g" -> WeightUnit.Gram
    "mg" -> WeightUnit.Milligram
    "kg" -> WeightUnit.Kilogram
    "oz" -> WeightUnit.Ounce
    "lbs" -> WeightUnit.Pound
    else -> null
}

/**
 * Tries to convert Remote formatted weight unit to [WeightUnit].
 *
 * throws [UnrecognizedWeightUnitFormat]
 */
internal fun stringToWeightUnit(
    text: String,
): WeightUnit = stringToWeightUnitOrNull(text = text) ?: throw (UnrecognizedWeightUnitFormat(message = text))

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

val FDCId.dataType: FDCDataType
    get() = when (this) {
        is BrandedFDCId -> FDCDataType.Branded
        is SurveyFdcId -> FDCDataType.Survey
        is LegacyFdcId -> FDCDataType.Legacy
        is FoundationFdcId -> FDCDataType.Foundation
    }

fun foodDataCentralId(
    id: Int,
    dataType: FDCDataType,
): FDCId = when (dataType) {
    FDCDataType.Foundation -> FoundationFdcId(id)
    FDCDataType.Survey -> SurveyFdcId(id)
    FDCDataType.Branded -> BrandedFDCId(id)
    FDCDataType.Legacy -> LegacyFdcId(id)
}

typealias FDCNutritionInfo = Nutrition

sealed interface FDCFoodItem {
    val fdcId: FDCId
    val description: String
    val nutritionalInfo: FDCNutritionInfo?
}

private fun parseNutrientNumber(
    amount: Number,
    unitName: String,
    nutrientNumber: String,
    nutrientId: Int,
): FDCNutritionInfo {
    val weightUnit = stringToWeightUnitOrNull(unitName)
    val energyUnit = stringToFoodEnergyUnit(unitName)
    return when (nutrientNumber) {
        "203" -> FDCNutritionInfo(totalProtein = weightUnit!!.weightOf(amount))
        "204" -> FDCNutritionInfo(totalFat = weightUnit!!.weightOf(amount))
        "205" -> FDCNutritionInfo(totalCarbohydrates = weightUnit!!.weightOf(amount))
        "208" -> FDCNutritionInfo(foodEnergy = energyUnit!!.energyOf(amount))
        "262" -> FDCNutritionInfo(/* caffeine */)
        "269" -> FDCNutritionInfo(sugar = weightUnit!!.weightOf(amount))
        "291" -> FDCNutritionInfo(fiber = weightUnit!!.weightOf(amount))
        "295".also { (nutrientId == 1082) } -> FDCNutritionInfo(/* fiberSoluble */)
        "301" -> FDCNutritionInfo(calcium = weightUnit!!.weightOf(amount))
        "303" -> FDCNutritionInfo(iron = weightUnit!!.weightOf(amount))
        "304" -> FDCNutritionInfo(magnesium = weightUnit!!.weightOf(amount))
        "305" -> FDCNutritionInfo(phosphorous = weightUnit!!.weightOf(amount))
        "306" -> FDCNutritionInfo(potassium = weightUnit!!.weightOf(amount))
        "307" -> FDCNutritionInfo(sodium = weightUnit!!.weightOf(amount))
        "309" -> FDCNutritionInfo(/* zinc */)
        "318" -> FDCNutritionInfo(/* vitaminA */)
        "324" -> FDCNutritionInfo(/* vitaminD (D2 + D3) */)
        "401" -> FDCNutritionInfo(vitaminC = weightUnit!!.weightOf(amount))
        "404" -> FDCNutritionInfo(/* thiamin */)
        "415" -> FDCNutritionInfo(/* vitaminB6 */)
        "417" -> FDCNutritionInfo(/* totalFolate */)
        "418" -> FDCNutritionInfo(/* vitaminB12 */)
        "431" -> FDCNutritionInfo(/* folicAcid */)
        "539" -> FDCNutritionInfo(/* sugar = weightUnit!!.weightOf(amount) */) // sugar added
        "601" -> FDCNutritionInfo(cholesterol = weightUnit!!.weightOf(amount))
        "605" -> FDCNutritionInfo(transFat = weightUnit!!.weightOf(amount))
        "606" -> FDCNutritionInfo(saturatedFat = weightUnit!!.weightOf(amount))
        "645" -> FDCNutritionInfo(monounsaturatedFat = weightUnit!!.weightOf(amount))
        "646" -> FDCNutritionInfo(polyunsaturatedFat = weightUnit!!.weightOf(amount))
        "960" -> FDCNutritionInfo(vitaminA = weightUnit!!.weightOf(amount))
        else -> throw (Throwable("Unsupported nutrient number: '$nutrientNumber'"))
    }
}

internal enum class NutrientDerivationType {
    Per100Units,
    LessThanPer100Units,
    PerServing,
    DailyValuePerServing,
    ;

    companion object {
        internal fun fromRemoteId(id: Int): NutrientDerivationType? = when (id) {
            71 -> PerServing
            75 -> DailyValuePerServing
            78 -> Per100Units
            79 -> LessThanPer100Units
            else -> null
        }
    }
}

internal enum class CombinedNutrientType {
    Per100,
    PerServing,
    PerServingDailyValue,
    ;
}

internal val CombinedNutrientType.isPerSering: Boolean
    get() = when (this) {
        CombinedNutrientType.Per100 -> false
        CombinedNutrientType.PerServing -> true
        CombinedNutrientType.PerServingDailyValue -> true
    }

internal fun List<FoodNutrientSchema>.toNutritionInfo(servingSize: Portion = Portion(100.grams)): Map<CombinedNutrientType, FDCNutritionInfo> =
    mapNotNull { schema ->
        val derivationType = schema.foodNutrientDerivation?.id?.let {
            NutrientDerivationType.fromRemoteId(it)
                ?: throw (Throwable("Unrecognized Nutrient Derivation Type: id: '${it}'"))
        } ?: return@mapNotNull null

        val nutrition = parseNutrientNumber(
            amount = schema.amount ?: return@mapNotNull null,
            unitName = schema.nutrient?.unitName ?: return@mapNotNull null,
            nutrientNumber = schema.nutrient.number ?: return@mapNotNull null,
            nutrientId = schema.nutrient.id ?: return@mapNotNull null
        )

        derivationType to nutrition
    }.fold<Pair<NutrientDerivationType, FDCNutritionInfo>, MutableMap<NutrientDerivationType, FDCNutritionInfo>>(
        mutableMapOf()
    ) { acc, (type, nutrition) ->
        acc[type] = acc[type]?.let { it + nutrition } ?: nutrition
        acc
    }.let { oldMap ->
        buildMap<CombinedNutrientType, FDCNutritionInfo> {
            for ((key, value) in oldMap) {
                val newKey = when (key) {
                    NutrientDerivationType.Per100Units, NutrientDerivationType.LessThanPer100Units -> CombinedNutrientType.Per100
                    NutrientDerivationType.PerServing -> CombinedNutrientType.PerServing
                    NutrientDerivationType.DailyValuePerServing -> CombinedNutrientType.PerServingDailyValue
                }
                set(newKey, get(newKey)?.plus(value) ?: value)
            }
        }.mapValues { (key, value) ->
            if (key.isPerSering)
                value.copy(portion = servingSize)
            else
                value.copy(portion = if (servingSize.weight != null) Portion(100.grams) else Portion(100.milliliters))
        }
    }

internal fun foldToOne(nutritionMap: Map<CombinedNutrientType, FDCNutritionInfo>): FDCNutritionInfo? {
    return nutritionMap.values.reduceOrNull { acc, nutrition ->
        acc.merge(nutrition)
    }
}

internal fun chooseBestFrom(nutritionMap: Map<CombinedNutrientType, FDCNutritionInfo>): FDCNutritionInfo? {
    return nutritionMap[CombinedNutrientType.Per100] ?: nutritionMap[CombinedNutrientType.PerServing]
    ?: nutritionMap[CombinedNutrientType.PerServingDailyValue]
}

internal fun Map<CombinedNutrientType, FDCNutritionInfo>.chooseBest(): FDCNutritionInfo? =
//    foldToOne(this)
    chooseBestFrom(this)
