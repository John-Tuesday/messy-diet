/******************************************************************************
 * Copyright (c) 2023 John Tuesday Picot                                      *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  *
 * copies of the Software, and to permit persons to whom the Software is      *
 * furnished to do so, subject to the following conditions:                   *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.                            *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,   *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE*
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER     *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.                                                                  *
 ******************************************************************************/

package org.calamarfederal.messydiet.diet_data.model

import org.calamarfederal.messydiet.measure.*

class Portion private constructor(
    val weight: Weight?,
    val volume: Volume?,
) {
    constructor(weight: Weight) : this(weight = weight, volume = null)
    constructor(volume: Volume) : this(weight = null, volume = volume)
    constructor() : this(null, null)

    override fun toString(): String =
        "${this::class.simpleName}(weight=${weight.toString()}, volume=${volume.toString()})"

    override fun equals(other: Any?): Boolean {
        if (other is Portion) {
            return weight == other.weight && volume == other.volume
        }
        return false
    }

    operator fun plus(other: Portion): Portion? {
        return when {
            other.weight != null && volume == null -> Portion((weight ?: 0.grams) + other.weight)
            other.volume != null && weight == null -> Portion((volume ?: 0.liters) + other.volume)
            volume == null && weight == null -> other
            other.volume == null && other.weight == null -> this
            else -> null
        }
    }

    operator fun div(other: Portion): Double? {
        if (other.weight != null && weight != null) {
            return weight / other.weight
        } else if (other.volume != null && volume != null) {
            return volume / other.volume
        }
        return null
    }

    override fun hashCode(): Int {
        var result = weight?.hashCode() ?: 0
        result = 31 * result + (volume?.hashCode() ?: 0)
        return result
    }
}

sealed interface Nutrient

interface Protein : Nutrient {
    val totalProtein: Weight
}

interface Carbohydrate : Nutrient {
    val fiber: Weight?
    val sugar: Weight?
    val sugarAlcohol: Weight?
    val starch: Weight?
    val totalCarbohydrates: Weight
}

interface Fat : Nutrient {
    val monounsaturatedFat: Weight?
    val polyunsaturatedFat: Weight?
    val omega3: Weight?
    val omega6: Weight?
    val saturatedFat: Weight?
    val transFat: Weight?
    val cholesterol: Weight?
    val totalFat: Weight
}

interface MacroNutrients : Protein, Carbohydrate, Fat

interface Mineral : Nutrient {
    val calcium: Weight?
    val chloride: Weight?
    val iron: Weight?
    val magnesium: Weight?
    val phosphorous: Weight?
    val potassium: Weight?
    val sodium: Weight?
}

interface Vitamin : Nutrient {
    val vitaminC: Weight? get() = null
}

interface NutritionInfo : MacroNutrients, Mineral, Vitamin {
    val portion: Portion
    val foodEnergy: FoodEnergy
}

data class Nutrition(
    override val portion: Portion = Portion(),
    override val totalProtein: Weight = 0.grams,
    override val fiber: Weight? = null,
    override val sugar: Weight? = null,
    override val sugarAlcohol: Weight? = null,
    override val starch: Weight? = null,
    override val totalCarbohydrates: Weight = 0.grams,
    override val monounsaturatedFat: Weight? = null,
    override val polyunsaturatedFat: Weight? = null,
    override val omega3: Weight? = null,
    override val omega6: Weight? = null,
    override val saturatedFat: Weight? = null,
    override val transFat: Weight? = null,
    override val cholesterol: Weight? = null,
    override val totalFat: Weight = 0.grams,
    override val foodEnergy: FoodEnergy = 0.kcal,
    override val calcium: Weight? = null,
    override val chloride: Weight? = null,
    override val iron: Weight? = null,
    override val magnesium: Weight? = null,
    override val phosphorous: Weight? = null,
    override val potassium: Weight? = null,
    override val sodium: Weight? = null,
    override val vitaminC: Weight? = null,
) : NutritionInfo {
    operator fun Weight?.plus(other: Weight?): Weight? = when {
        this == null -> other
        other == null -> this
        else -> (this.inGrams() + other.inGrams()).grams
    }

    operator fun plus(other: NutritionInfo): Nutrition {
        return copy(
            portion = (portion + other.portion)!!,
            totalProtein = (totalProtein + other.totalProtein)!!,
            fiber = fiber + other.fiber,
            sugar = sugar + other.sugar,
            sugarAlcohol = sugarAlcohol + other.sugarAlcohol,
            starch = starch + other.starch,
            totalCarbohydrates = (totalCarbohydrates + other.totalCarbohydrates)!!,
            monounsaturatedFat = monounsaturatedFat + other.monounsaturatedFat,
            polyunsaturatedFat = polyunsaturatedFat + other.polyunsaturatedFat,
            omega3 = omega3 + other.omega3,
            omega6 = omega6 + other.omega6,
            saturatedFat = saturatedFat + other.saturatedFat,
            transFat = transFat + other.transFat,
            cholesterol = cholesterol + other.cholesterol,
            totalFat = (totalFat + other.totalFat)!!,
            foodEnergy = foodEnergy + other.foodEnergy,
            calcium = calcium + other.calcium,
            chloride = chloride + other.chloride,
            iron = iron + other.iron,
            magnesium = magnesium + other.magnesium,
            phosphorous = phosphorous + other.phosphorous,
            potassium = potassium + other.potassium,
            sodium = sodium + other.sodium,
            vitaminC = vitaminC + other.vitaminC,
        )
    }

    fun scaleToPortion(newPortion: Portion): Nutrition {
        val ratio = (newPortion / portion)!!

        return copy(
            portion = newPortion,
            totalProtein = totalProtein * ratio,
            fiber = fiber?.times(ratio),
            sugar = sugar?.times(ratio),
            sugarAlcohol = sugarAlcohol?.times(ratio),
            starch = starch?.times(ratio),
            totalCarbohydrates = totalCarbohydrates * ratio,
            monounsaturatedFat = monounsaturatedFat?.times(ratio),
            polyunsaturatedFat = polyunsaturatedFat?.times(ratio),
            omega3 = omega3?.times(ratio),
            omega6 = omega6?.times(ratio),
            saturatedFat = saturatedFat?.times(ratio),
            transFat = transFat?.times(ratio),
            cholesterol = cholesterol?.times(ratio),
            totalFat = totalFat * ratio,
            foodEnergy = foodEnergy * ratio,
            calcium = calcium?.times(ratio),
            chloride = chloride?.times(ratio),
            iron = iron?.times(ratio),
            magnesium = magnesium?.times(ratio),
            phosphorous = phosphorous?.times(ratio),
            potassium = potassium?.times(ratio),
            sodium = sodium?.times(ratio),
            vitaminC = vitaminC?.times(ratio),
        )
    }
}

fun NutritionInfo.asSequence(): Sequence<Weight?> = sequenceOf(
    totalProtein,
    fiber,
    sugar,
    sugarAlcohol,
    starch,
    totalCarbohydrates,
    monounsaturatedFat,
    polyunsaturatedFat,
    omega3,
    omega6,
    saturatedFat,
    transFat,
    cholesterol,
    totalFat,
    calcium,
    chloride,
    iron,
    magnesium,
    phosphorous,
    potassium,
    sodium,
    vitaminC,
)

fun NutritionInfo.compareWith(
    other: NutritionInfo,
    compareWeight: (Weight?, Weight?) -> Boolean,
    comparePortion: (Portion, Portion) -> Boolean,
    compareEnergy: (FoodEnergy, FoodEnergy) -> Boolean,
    breakOnFalse: Boolean = true,
): Boolean {
    val checks = sequence<Boolean> {
        yield(comparePortion(portion, other.portion))
        yield(compareEnergy(foodEnergy, other.foodEnergy))
        val nutrientSeq = asSequence().zip(other.asSequence())
        yieldAll(nutrientSeq.map { compareWeight(it.first, it.second) })
    }
    return if (breakOnFalse)
        checks.none { !it }
    else
        checks.all { it }
}
