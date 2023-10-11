package org.calamarfederal.messydiet.food.data.central.model

import io.github.john.tuesday.nutrition.*
import org.calamarfederal.messydiet.food.data.central.model.FoodDataCentralError.*
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
 * @throws [UnrecognizedWeightUnitFormat]
 */
internal fun stringToMassUnit(
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

internal enum class NutrientDerivationType(val code: String?, val id: Int? = null) {
    Per100Units(code = "LCCS", id = 70),
    LessThanPer100Units(code = null, id = 79),
    PerServing(code = "LCSA", id = 71),
    DailyValuePerServing(code = "LCCD", id = 75),
    ;

    companion object {
        internal fun fromRemoteId(id: Int): NutrientDerivationType? = entries.firstOrNull { it.id == id }

        internal fun fromRemoteCode(code: String): NutrientDerivationType? =
            entries.firstOrNull { it.code == code }
    }
}

internal val NutrientDerivationType.isPerServing: Boolean
    get() = when (this) {
        NutrientDerivationType.PerServing, NutrientDerivationType.DailyValuePerServing -> true
        else -> false
    }

/**
 * Intermediate nutrient information when converting FDC nutrients to [FoodNutrition]
 */
internal data class ProtoNutrient(
    val amount: Number,
    val unitName: String,
    val nutrientNumber: String,
    val derivationCode: String,
    val derivationDescription: String?,
)

/**
 * Grouping by [NutrientDerivationType], convert FDC nutrient information into [FoodNutrition]
 *
 * This is meant to hold the abstract conversion logic for lists of FDC nutrients of different types. That is to say
 * a list of [AbridgedFoodNutrientSchema] or [FoodNutrientSchema]. [nutrientSequence] yields common intermediate data
 * holders to represent the necessary nutrient information.
 *
 * [Portion] information is required for [FoodNutrition] but is not defined in the lists of nutrients, so [getPortion]
 * is used to inject the [Portion] if necessary
 *
 * [strict] is a flag to signify if unrecognized inputs should throw an exception or be ignored
 *
 * @throws[UnrecognizedNutrientDerivation] only if [strict] is true
 * @throws[UnrecognizedNutrientNumber] if [strict] is true
 * @throws[UnrecognizedWeightUnitFormat] if [strict] is true
 */
internal fun parseToNutritionalInfo(
    nutrientSequence: Sequence<ProtoNutrient>,
    getPortion: (NutrientDerivationType) -> Portion,
    strict: Boolean = false,
): Map<NutrientDerivationType, FDCNutritionInfo> {
    val energyMap: MutableMap<NutrientDerivationType, Energy> = mutableMapOf()
    val nutritionMapMap: MutableMap<NutrientDerivationType, MutableMap<NutrientType, Mass>> = mutableMapOf()
    for ((amount, unitName, nutrientNumber, derivationCode, derivationDescription) in nutrientSequence) {
        val derivationType = NutrientDerivationType.fromRemoteCode(derivationCode)
            ?: if (strict)
                throw (UnrecognizedNutrientDerivation(
                    code = derivationCode,
                    description = derivationDescription
                ))
            else continue

        // Check for energy manually because it's a compatible return type for [nutrientNumberToNutrientType]
        if (nutrientNumber == "208") {
            val energyUnit = stringToFoodEnergyUnit(unitName)!!
            energyMap[derivationType] = Energy(amount, energyUnit)
            continue
        }

        val nutrientType = nutrientNumberToNutrientType(nutrientNumber, strict = strict) ?: continue
        val massUnit = stringToMassUnit(unitName)
        val mass = Mass(amount, massUnit)

        val nutrientMap = nutritionMapMap.getOrPut(derivationType) { mutableMapOf() }
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

private fun servingSizeOr100Units(servingSize: Portion, nutrientDerivationType: NutrientDerivationType): Portion =
    if (nutrientDerivationType.isPerServing)
        servingSize
    else when (servingSize) {
        is MassPortion -> MassPortion(100.grams)
        is VolumePortion -> VolumePortion(100.milliliters)
    }

@JvmName("abridgedToNutritionInfo")
internal fun List<AbridgedFoodNutrientSchema>.toNutritionInfo(
    servingSize: Portion = Portion(0.grams),
    strict: Boolean = false,
): Map<NutrientDerivationType, FDCNutritionInfo> =
    parseToNutritionalInfo(
        nutrientSequence = asSequence().map { schema ->
            ProtoNutrient(
                amount = schema.amount!!,
                unitName = schema.unitName!!,
                nutrientNumber = schema.number!!,
                derivationCode = schema.derivationCode!!,
                derivationDescription = schema.derivationDescription,
            )
        },
        getPortion = { servingSizeOr100Units(servingSize, it) },
        strict = strict,
    )

internal fun List<FoodNutrientSchema>.toNutritionInfo(
    servingSize: Portion = Portion(0.grams),
    strict: Boolean = false,
): Map<NutrientDerivationType, FDCNutritionInfo> =
    parseToNutritionalInfo(
        nutrientSequence = asSequence().map { schema ->
            ProtoNutrient(
                amount = schema.amount!!,
                unitName = schema.nutrient!!.unitName!!,
                nutrientNumber = schema.nutrient.number!!,
                derivationCode = schema.foodNutrientDerivation!!.code!!,
                derivationDescription = schema.foodNutrientDerivation.description,
            )
        },
        getPortion = { servingSizeOr100Units(servingSize, it) },
//        strict = strict,
    )

internal fun Map<NutrientDerivationType, FoodNutrition>.chooseBest(): FoodNutrition? =
    get(NutrientDerivationType.Per100Units) ?: values.firstOrNull()
