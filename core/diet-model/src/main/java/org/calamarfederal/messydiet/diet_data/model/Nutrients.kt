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

enum class Nutrients {
    Protein,

    Fiber,
    Sugar,
    SugarAlcohol,
    Starch,

    MonounsaturatedFat,
    PolyunsaturatedFat,
    Omega3,
    Omega6,
    SaturatedFat,
    TransFat,
    Cholesterol,

    Calcium,
    Chloride,
    Iron,
    Magnesium,
    Phosphorous,
    Potassium,
    Sodium,

    VitaminA,
    VitaminC,
}

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
    val vitaminA: Weight? get() = null
    val vitaminC: Weight? get() = null
}

interface NutritionInfo : MacroNutrients, Mineral, Vitamin {
    val portion: Portion
    val foodEnergy: FoodEnergy

    /**
     * Get the nutrient specified by [nutrient]. If nutrient is unset, it returns `null`
     */
    operator fun get(nutrient: Nutrients): Weight? {
        return when (nutrient) {
            Nutrients.Protein -> totalProtein

            Nutrients.Fiber -> fiber
            Nutrients.Sugar -> sugar
            Nutrients.SugarAlcohol -> sugarAlcohol
            Nutrients.Starch -> starch

            Nutrients.MonounsaturatedFat -> monounsaturatedFat
            Nutrients.PolyunsaturatedFat -> polyunsaturatedFat
            Nutrients.Omega3 -> omega3
            Nutrients.Omega6 -> omega6
            Nutrients.SaturatedFat -> saturatedFat
            Nutrients.TransFat -> transFat
            Nutrients.Cholesterol -> cholesterol

            Nutrients.Calcium -> calcium
            Nutrients.Chloride -> chloride
            Nutrients.Iron -> iron
            Nutrients.Magnesium -> magnesium
            Nutrients.Phosphorous -> phosphorous
            Nutrients.Potassium -> potassium
            Nutrients.Sodium -> sodium

            Nutrients.VitaminA -> vitaminA
            Nutrients.VitaminC -> vitaminC
        }
    }
}

fun NutritionInfo.isZeroOrUnset(): Boolean {
    return foodEnergy == 0.kcal
            && portion.weight in listOf(null, 0.grams)
            && portion.volume in listOf(null, 0.liters)
            && Nutrients.entries.all { nutrient -> (this[nutrient] ?: 0.grams) == 0.grams }
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
    override val vitaminA: Weight? = null,
    override val vitaminC: Weight? = null,
) : NutritionInfo {
//    private operator fun Weight?.plus(other: Weight?): Weight? = when {
//        this == null -> other
//        other == null -> this
//        else -> (this.inGrams() + other.inGrams()).grams
//    }

    operator fun plus(other: NutritionInfo): Nutrition {
        var result = Nutrition(
            portion = (portion + other.portion)!!,
            foodEnergy = foodEnergy + other.foodEnergy,
            totalCarbohydrates = totalCarbohydrates + other.totalCarbohydrates,
            totalFat = totalFat + other.totalFat,
        )
        for (nutrient in Nutrients.entries) {
            val weight = when (val thisWeight = this[nutrient]) {
                null -> other[nutrient]
                else -> other[nutrient]?.let { it + thisWeight } ?: thisWeight
            }
            result = result.copy(nutrient = nutrient, weight = weight)
        }
        return result
    }

    fun scaleToPortion(newPortion: Portion): Nutrition {
        val ratio = (newPortion / portion)!!
        var result = Nutrition(
            portion = newPortion,
            foodEnergy = foodEnergy * ratio,
            totalCarbohydrates = totalCarbohydrates * ratio,
            totalFat = totalFat * ratio,
        )
        for (nutrient in Nutrients.entries) {
            result = result.copy(nutrient = nutrient, weight = this[nutrient]?.let { it * ratio })
        }
        return result
    }
}

fun Nutrition.copy(nutrient: Nutrients, weight: Weight?): Nutrition {
    return when (nutrient) {
        Nutrients.Protein -> copy(totalProtein = weight ?: throw (Throwable("protein mut not be null")))

        Nutrients.Fiber -> copy(fiber = weight)
        Nutrients.Sugar -> copy(sugar = weight)
        Nutrients.SugarAlcohol -> copy(sugarAlcohol = weight)
        Nutrients.Starch -> copy(starch = weight)

        Nutrients.MonounsaturatedFat -> copy(monounsaturatedFat = weight)
        Nutrients.PolyunsaturatedFat -> copy(polyunsaturatedFat = weight)
        Nutrients.Omega3 -> copy(omega3 = weight)
        Nutrients.Omega6 -> copy(omega6 = weight)
        Nutrients.SaturatedFat -> copy(saturatedFat = weight)
        Nutrients.TransFat -> copy(transFat = weight)
        Nutrients.Cholesterol -> copy(cholesterol = weight)

        Nutrients.Calcium -> copy(calcium = weight)
        Nutrients.Chloride -> copy(chloride = weight)
        Nutrients.Iron -> copy(iron = weight)
        Nutrients.Magnesium -> copy(magnesium = weight)
        Nutrients.Phosphorous -> copy(phosphorous = weight)
        Nutrients.Potassium -> copy(potassium = weight)
        Nutrients.Sodium -> copy(sodium = weight)

        Nutrients.VitaminA -> copy(vitaminA = weight)
        Nutrients.VitaminC -> copy(vitaminC = weight)
    }
}

/**
 *
 * fill empty or zero fields of [other] with `this`
 */
fun Nutrition.merge(other: NutritionInfo): Nutrition {
    val ratio = if (other.portion == Portion() || portion == Portion()) 1 else (other.portion / portion)!!
    var result = Nutrition(
        portion = if (other.portion == Portion()) portion else other.portion,
        foodEnergy = if (other.foodEnergy == 0.kcal) foodEnergy * ratio else other.foodEnergy,
        totalCarbohydrates = if (other.totalCarbohydrates == 0.grams) totalCarbohydrates * ratio else other.totalCarbohydrates,
        totalFat = if (other.totalFat == 0.grams) totalFat * ratio else other.totalFat,
    )
    for (n in Nutrients.entries) {
        val weight = if (other[n] == null || other[n] == 0.grams) this[n]?.times(ratio) else other[n]
        result = result.copy(nutrient = n, weight = weight)
    }
    return result
}

fun NutritionInfo.compareWith(
    other: NutritionInfo,
    compareWeight: (Nutrients, Weight?, Weight?) -> Boolean,
    comparePortion: (Portion, Portion) -> Boolean,
    compareEnergy: (FoodEnergy, FoodEnergy) -> Boolean,
    breakOnFalse: Boolean = true,
): Boolean {
    val checks = sequence<Boolean> {
        yield(comparePortion(portion, other.portion))
        yield(compareEnergy(foodEnergy, other.foodEnergy))
        val nutrientSeq = Nutrients.entries.asSequence().map {
            compareWeight(it, this@compareWith[it], other[it])
        }
        yieldAll(nutrientSeq)
    }
    return if (breakOnFalse)
        checks.none { !it }
    else
        checks.all { it }
}
