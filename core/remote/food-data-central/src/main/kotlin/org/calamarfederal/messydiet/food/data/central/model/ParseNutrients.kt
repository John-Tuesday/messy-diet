package org.calamarfederal.messydiet.food.data.central.model

import org.calamarfederal.messydiet.diet_data.model.FoodEnergyUnit
import org.calamarfederal.messydiet.diet_data.model.Portion
import org.calamarfederal.messydiet.diet_data.model.energyOf
import org.calamarfederal.messydiet.diet_data.model.merge
import org.calamarfederal.messydiet.food.data.central.model.FoodDataCentralError.UnrecognizedNutrientNumber
import org.calamarfederal.messydiet.food.data.central.model.FoodDataCentralError.UnrecognizedWeightUnitFormat
import org.calamarfederal.messydiet.food.data.central.remote.schema.AbridgedFoodNutrientSchema
import org.calamarfederal.messydiet.food.data.central.remote.schema.FoodNutrientSchema
import org.calamarfederal.physical.measurement.*

/**
 * Tries to convert [text] to [WeightUnit].
 */
internal fun stringToMassUnitOrNull(
    text: String,
): MassUnit? = when (text.lowercase().trim()) {
    "µg" -> MassUnit.Microgram
    "g" -> MassUnit.Gram
    "mg" -> MassUnit.Milligram
    "kg" -> MassUnit.Kilogram
    "oz" -> MassUnit.Ounce
    "lbs" -> MassUnit.Pound
    else -> null
}

/**
 * Tries to convert Remote formatted weight unit to [MassUnit].
 *
 * throws [UnrecognizedWeightUnitFormat]
 */
internal fun stringToWeightUnit(
    text: String,
): MassUnit = stringToMassUnitOrNull(text = text) ?: throw (UnrecognizedWeightUnitFormat(input = text))

internal fun stringToFoodEnergyUnit(
    text: String,
): FoodEnergyUnit? = when (text.lowercase().trim()) {
    "kcal" -> FoodEnergyUnit.Kilocalories
    else -> null
}

//private fun nutrientsFromNumber(number: String): Nutrients? {
//    return when (number) {
//        "203" -> Nutrients.Protein//FDCNutritionInfo(totalProtein = weightUnit!!.weightOf(amount))
//        "204" -> Nutrients.TotalFat//FDCNutritionInfo(totalFat = weightUnit!!.weightOf(amount))
//        "205" -> Nutrients.TotalCarbohydrate//FDCNutritionInfo(totalCarbohydrates = weightUnit!!.weightOf(amount))
//        "208" -> null//FDCNutritionInfo(foodEnergy = energyUnit!!.energyOf(amount))
//        "262" -> null//FDCNutritionInfo(/* caffeine */)
//        "269" -> Nutrients.Sugar//FDCNutritionInfo(sugar = weightUnit!!.weightOf(amount))
//        "291" -> Nutrients.Fiber//FDCNutritionInfo(fiber = weightUnit!!.weightOf(amount))
//        "295" -> null//FDCNutritionInfo(/* fiberSoluble */)
//        "299" -> FDCNutritionInfo(sugarAlcohol = weightUnit!!.weightOf(amount))
//        "301" -> FDCNutritionInfo(calcium = weightUnit!!.weightOf(amount))
//        "303" -> FDCNutritionInfo(iron = weightUnit!!.weightOf(amount))
//        "304" -> FDCNutritionInfo(magnesium = weightUnit!!.weightOf(amount))
//        "305" -> FDCNutritionInfo(phosphorous = weightUnit!!.weightOf(amount))
//        "306" -> FDCNutritionInfo(potassium = weightUnit!!.weightOf(amount))
//        "307" -> FDCNutritionInfo(sodium = weightUnit!!.weightOf(amount))
//        "309" -> FDCNutritionInfo(/* zinc */)
//        "318" -> FDCNutritionInfo(/* vitaminA */)
//        "324" -> FDCNutritionInfo(/* vitaminD (D2 + D3) */)
//        "328" -> FDCNutritionInfo(/* vitaminD (D2 + D3) */)
//        "401" -> FDCNutritionInfo(vitaminC = weightUnit!!.weightOf(amount))
//        "404" -> FDCNutritionInfo(/* thiamin */)
//        "415" -> FDCNutritionInfo(/* vitaminB6 */)
//        "417" -> FDCNutritionInfo(/* totalFolate */)
//        "418" -> FDCNutritionInfo(/* vitaminB12 */)
//        "431" -> FDCNutritionInfo(/* folicAcid */)
//        "539" -> FDCNutritionInfo(/* sugar = weightUnit!!.weightOf(amount) */) // sugar added
//        "601" -> FDCNutritionInfo(cholesterol = weightUnit!!.weightOf(amount))
//        "605" -> FDCNutritionInfo(transFat = weightUnit!!.weightOf(amount))
//        "606" -> FDCNutritionInfo(saturatedFat = weightUnit!!.weightOf(amount))
//        "645" -> FDCNutritionInfo(monounsaturatedFat = weightUnit!!.weightOf(amount))
//        "646" -> FDCNutritionInfo(polyunsaturatedFat = weightUnit!!.weightOf(amount))
//        "960" -> FDCNutritionInfo(vitaminA = weightUnit!!.weightOf(amount))
//        else -> throw (Throwable("Unsupported nutrient number: '$nutrientNumber'"))
//    }
//}

/**
 * Parse the FDC nutrient information to a [FDCNutritionInfo]
 *
 * throws [UnrecognizedNutrientNumber] if [nutrientNumber] is not recognized.
 */
internal fun parseNutrientNumber(
    amount: Number,
    unitName: String,
    nutrientNumber: String,
): FDCNutritionInfo {
    val weightUnit = stringToMassUnitOrNull(unitName)
    val energyUnit = stringToFoodEnergyUnit(unitName)
    val mass = weightUnit?.let { Mass(amount, it) }
    return when (nutrientNumber) {
        "203" -> FDCNutritionInfo(totalProtein = mass!!)
        "204" -> FDCNutritionInfo(totalFat = mass!!)
        "205" -> FDCNutritionInfo(totalCarbohydrates = mass!!)
        "208" -> FDCNutritionInfo(foodEnergy = energyUnit!!.energyOf(amount))
        "262" -> FDCNutritionInfo(/* caffeine */)
        "269" -> FDCNutritionInfo(sugar = mass)
        "291" -> FDCNutritionInfo(fiber = mass)
        "295" -> FDCNutritionInfo(/* fiberSoluble */)
        "299" -> FDCNutritionInfo(sugarAlcohol = mass)
        "301" -> FDCNutritionInfo(calcium = mass)
        "303" -> FDCNutritionInfo(iron = mass)
        "304" -> FDCNutritionInfo(magnesium = mass)
        "305" -> FDCNutritionInfo(phosphorous = mass)
        "306" -> FDCNutritionInfo(potassium = mass)
        "307" -> FDCNutritionInfo(sodium = mass)
        "309" -> FDCNutritionInfo(/* zinc */)
        "318" -> FDCNutritionInfo(/* vitaminA */)
        "324" -> FDCNutritionInfo(/* vitaminD (D2 + D3) */)
        "328" -> FDCNutritionInfo(/* vitaminD (D2 + D3) */)
        "401" -> FDCNutritionInfo(vitaminC = mass)
        "404" -> FDCNutritionInfo(/* thiamin */)
        "415" -> FDCNutritionInfo(/* vitaminB6 */)
        "417" -> FDCNutritionInfo(/* totalFolate */)
        "418" -> FDCNutritionInfo(/* vitaminB12 */)
        "431" -> FDCNutritionInfo(/* folicAcid */)
        "539" -> FDCNutritionInfo(/* sugar = mass */) // sugar added
        "601" -> FDCNutritionInfo(cholesterol = mass)
        "605" -> FDCNutritionInfo(transFat = mass)
        "606" -> FDCNutritionInfo(saturatedFat = mass)
        "645" -> FDCNutritionInfo(monounsaturatedFat = mass)
        "646" -> FDCNutritionInfo(polyunsaturatedFat = mass)
        "960" -> FDCNutritionInfo(vitaminA = mass)
        else -> throw (UnrecognizedNutrientNumber(number = nutrientNumber))
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
            70 -> Per100Units
            71 -> PerServing
            75 -> DailyValuePerServing
            78 -> Per100Units
            79 -> LessThanPer100Units
            else -> null
        }

        internal fun fromRemoteCode(code: String): NutrientDerivationType? = when (code) {
//            "LCCL" -> PerServing
            "LCCD" -> DailyValuePerServing
            "LCCS" -> Per100Units
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

internal fun processMap(
    info: List<Pair<NutrientDerivationType, FDCNutritionInfo>>,
    servingSize: Portion = Portion(100.grams),
): Map<CombinedNutrientType, FDCNutritionInfo> =
    info.fold<Pair<NutrientDerivationType, FDCNutritionInfo>, MutableMap<NutrientDerivationType, FDCNutritionInfo>>(
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
                value.copy(portion = if (servingSize.mass != null) Portion(100.grams) else Portion(100.milliliters))
        }
    }

internal fun List<AbridgedFoodNutrientSchema>.toNutritionInfo(): Map<CombinedNutrientType, FDCNutritionInfo> {
    val infoMap = mapNotNull { schema ->
        val derivationType = schema.derivationCode?.let { NutrientDerivationType.fromRemoteCode(it) }
            ?: NutrientDerivationType.Per100Units

        val nutrition = parseNutrientNumber(
            amount = schema.amount ?: return@mapNotNull null,
            unitName = schema.unitName ?: return@mapNotNull null,
            nutrientNumber = schema.number ?: return@mapNotNull null,
        )

        derivationType to nutrition
    }

    return processMap(infoMap)
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
        )

        derivationType to nutrition
    }.let { processMap(it, servingSize) }
//    .fold<Pair<NutrientDerivationType, FDCNutritionInfo>, MutableMap<NutrientDerivationType, FDCNutritionInfo>>(
//        mutableMapOf()
//    ) { acc, (type, nutrition) ->
//        acc[type] = acc[type]?.let { it + nutrition } ?: nutrition
//        acc
//    }.let { oldMap ->
//        buildMap<CombinedNutrientType, FDCNutritionInfo> {
//            for ((key, value) in oldMap) {
//                val newKey = when (key) {
//                    NutrientDerivationType.Per100Units, NutrientDerivationType.LessThanPer100Units -> CombinedNutrientType.Per100
//                    NutrientDerivationType.PerServing -> CombinedNutrientType.PerServing
//                    NutrientDerivationType.DailyValuePerServing -> CombinedNutrientType.PerServingDailyValue
//                }
//                set(newKey, get(newKey)?.plus(value) ?: value)
//            }
//        }.mapValues { (key, value) ->
//            if (key.isPerSering)
//                value.copy(portion = servingSize)
//            else
//                value.copy(portion = if (servingSize.weight != null) Portion(100.grams) else Portion(100.milliliters))
//        }
//    }

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
