package org.calamarfederal.messydiet.food.data.central.model

import io.github.john.tuesday.nutrition.*
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
): EnergyUnit? = when (text.lowercase().trim()) {
    "kcal" -> EnergyUnit.Kilocalorie
    else -> null
}

/**
 * returns `null` when type should be skipped or is unrecognized if [strict]
 *
 * @throws UnrecognizedNutrientNumber when [strict] is `true` and [nutrientNumber] is not a recognized [NutrientType]
 */
internal fun nutrientNumberToNutrientType(nutrientNumber: String, strict: Boolean = false): NutrientType? {
    return when (nutrientNumber) {
        "203" -> NutrientType.Protein
        "204" -> NutrientType.TotalFat//FDCNutritionInfo(totalFat = mass!!)
        "205" -> NutrientType.TotalCarbohydrate//FDCNutritionInfo(totalCarbohydrates = mass!!)
        "208" -> null//FDCNutritionInfo(foodEnergy = energyUnit!!.energyOf(amount))
        "262" -> null//FDCNutritionInfo(/* caffeine */)
        "269" -> NutrientType.Sugar//FDCNutritionInfo(sugar = mass)
        "291" -> NutrientType.Fiber//FDCNutritionInfo(fiber = mass)
        "295" -> null//FDCNutritionInfo(/* fiberSoluble */)
        "299" -> NutrientType.SugarAlcohol//FDCNutritionInfo(sugarAlcohol = mass)
        "301" -> NutrientType.Calcium//FDCNutritionInfo(calcium = mass)
        "303" -> NutrientType.Iron//FDCNutritionInfo(iron = mass)
        "304" -> NutrientType.Magnesium//FDCNutritionInfo(magnesium = mass)
        "305" -> NutrientType.Phosphorous//FDCNutritionInfo(phosphorous = mass)
        "306" -> NutrientType.Potassium//FDCNutritionInfo(potassium = mass)
        "307" -> NutrientType.Sodium//FDCNutritionInfo(sodium = mass)
        "309" -> null//FDCNutritionInfo(/* zinc */)
        "318" -> NutrientType.VitaminA//FDCNutritionInfo(/* vitaminA */)
        "324" -> null//FDCNutritionInfo(/* vitaminD (D2 + D3) */)
        "328" -> null//FDCNutritionInfo(/* vitaminD (D2 + D3) */)
        "401" -> NutrientType.VitaminC//FDCNutritionInfo(vitaminC = mass)
        "404" -> null//FDCNutritionInfo(/* thiamin */)
        "415" -> null//FDCNutritionInfo(/* vitaminB6 */)
        "417" -> null//FDCNutritionInfo(/* totalFolate */)
        "418" -> null//FDCNutritionInfo(/* vitaminB12 */)
        "431" -> null//FDCNutritionInfo(/* folicAcid */)
        "539" -> null//FDCNutritionInfo(/* sugar = mass */) // sugar added
        "601" -> NutrientType.Cholesterol//FDCNutritionInfo(cholesterol = mass)
        "605" -> NutrientType.TransFat//FDCNutritionInfo(transFat = mass)
        "606" -> NutrientType.SaturatedFat//FDCNutritionInfo(saturatedFat = mass)
        "645" -> NutrientType.MonounsaturatedFat//FDCNutritionInfo(monounsaturatedFat = mass)
        "646" -> NutrientType.PolyunsaturatedFat//FDCNutritionInfo(polyunsaturatedFat = mass)
        "960" -> NutrientType.VitaminA//FDCNutritionInfo(vitaminA = mass)
        else -> if (strict) throw (UnrecognizedNutrientNumber(number = nutrientNumber)) else null
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

//internal fun processMap(
//    info: List<Pair<NutrientDerivationType, FDCNutritionInfo>>,
//    servingSize: Portion = Portion(100.grams),
//): Map<CombinedNutrientType, FDCNutritionInfo> =
//    info.fold<Pair<NutrientDerivationType, FDCNutritionInfo>, MutableMap<NutrientDerivationType, FDCNutritionInfo>>(
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
//                value.copy(portion = if (servingSize.mass != null) Portion(100.grams) else Portion(100.milliliters))
//        }
//    }

internal data class ProtoNutrient(
    val amount: Number,
    val unitName: String,
    val nutrientNumber: String,
    val derivationCode: String,
)

internal fun parseToNutritionalInfo(
    nutrientSequence: Sequence<ProtoNutrient>,
    getPortion: (NutrientDerivationType) -> Portion,
): Map<NutrientDerivationType, FDCNutritionInfo> {
    val energyMap: MutableMap<NutrientDerivationType, Energy> = mutableMapOf()
    val nutritionMapMap: MutableMap<NutrientDerivationType, MutableMap<NutrientType, Mass>> = mutableMapOf()
    for ((amount, unitName, nutrientNumber, derivationCode) in nutrientSequence) {
        val derivationType = NutrientDerivationType.fromRemoteCode(derivationCode) ?: continue

        if (nutrientNumber == "208") {
            val energyUnit = stringToFoodEnergyUnit(unitName)!!
            energyMap[derivationType] = Energy(amount, energyUnit)
        }

        val nutrientType = nutrientNumberToNutrientType(nutrientNumber, strict = false) ?: continue
        val massUnit = stringToMassUnitOrNull(unitName)!!
        val mass = Mass(amount, massUnit)

        val nutrientMap = nutritionMapMap.getOrElse(derivationType) { mutableMapOf() }
        nutrientMap[nutrientType] = mass
        nutritionMapMap[derivationType] = nutrientMap
    }

    return nutritionMapMap.mapValues { (derivationType, nutritionMap) ->
        FoodNutrition(
            portion = getPortion(derivationType),
            foodEnergy = energyMap[derivationType]!!,
            nutritionMap = nutritionMap
        )
    }.toMap()
}

internal fun List<AbridgedFoodNutrientSchema>.toNutritionInfo(): Map<NutrientDerivationType, FDCNutritionInfo> {
    return parseToNutritionalInfo(
        nutrientSequence = asSequence().map { schema ->
            ProtoNutrient(
                amount = schema.amount!!,
                unitName = schema.unitName!!,
                nutrientNumber = schema.number!!,
                derivationCode = schema.derivationCode!!,
            )
        },
        getPortion = { Portion(0.grams) },
    )
//    val infoMap = mapNotNull { schema ->
//        val derivationType = schema.derivationCode?.let { NutrientDerivationType.fromRemoteCode(it) }
//            ?: NutrientDerivationType.Per100Units
//        val nutrition = parseNutrientNumber(
//            amount = schema.amount ?: return@mapNotNull null,
//            unitName = schema.unitName ?: return@mapNotNull null,
//            nutrientNumber = schema.number ?: return@mapNotNull null,
//        )
//
//        derivationType to nutrition
//    }
//
//    return processMap(infoMap)
}

internal fun List<FoodNutrientSchema>.toNutritionInfo(servingSize: Portion = Portion(100.grams)): Map<NutrientDerivationType, FDCNutritionInfo> {
    return parseToNutritionalInfo(
        nutrientSequence = asSequence().map { schema ->
            ProtoNutrient(
                amount = schema.amount!!,
                unitName = schema.nutrient!!.unitName!!,
                nutrientNumber = schema.nutrient.number!!,
                derivationCode = schema.foodNutrientDerivation!!.code!!,
            )
        },
        getPortion = { Portion(0.grams) },
    )
}
//    mapNotNull { schema ->
//        val derivationType = schema.foodNutrientDerivation?.id?.let {
//            NutrientDerivationType.fromRemoteId(it)
//                ?: throw (Throwable("Unrecognized Nutrient Derivation Type: id: '${it}'"))
//        } ?: return@mapNotNull null
//
//        val nutrition = parseNutrientNumber(
//            amount = schema.amount ?: return@mapNotNull null,
//            unitName = schema.nutrient?.unitName ?: return@mapNotNull null,
//            nutrientNumber = schema.nutrient.number ?: return@mapNotNull null,
//        )
//
//        derivationType to nutrition
//    }.let { processMap(it, servingSize) }

//internal fun chooseBestFrom(nutritionMap: Map<CombinedNutrientType, FDCNutritionInfo>): FDCNutritionInfo? {
//    return nutritionMap[CombinedNutrientType.Per100] ?: nutritionMap[CombinedNutrientType.PerServing]
//    ?: nutritionMap[CombinedNutrientType.PerServingDailyValue]
//}
//
//internal fun Map<CombinedNutrientType, FDCNutritionInfo>.chooseBest(): FDCNutritionInfo? =
//    chooseBestFrom(this)
internal fun Map<NutrientDerivationType, FoodNutrition>.chooseBest(): FoodNutrition? =
    get(NutrientDerivationType.Per100Units) ?: values.firstOrNull()
