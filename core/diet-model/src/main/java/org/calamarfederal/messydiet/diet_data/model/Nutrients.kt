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

import org.calamarfederal.physical.measurement.*

class Portion private constructor(
    val mass: Mass?,
    val volume: Volume?,
) {
    constructor(mass: Mass) : this(mass = mass, volume = null)
    constructor(volume: Volume) : this(mass = null, volume = volume)
    constructor() : this(null, null)

    override fun toString(): String =
        "${this::class.simpleName}(weight=${mass.toString()}, volume=${volume.toString()})"

    override fun equals(other: Any?): Boolean {
        if (other is Portion) {
            return mass == other.mass && volume == other.volume
        }
        return false
    }

    operator fun plus(other: Portion): Portion? {
        return when {
            other.mass != null && volume == null -> Portion((mass ?: 0.grams) + other.mass)
            other.volume != null && mass == null -> Portion((volume ?: 0.liters) + other.volume)
            volume == null && mass == null -> other
            other.volume == null && other.mass == null -> this
            else -> null
        }
    }

    operator fun div(other: Portion): Double? {
        if (other.mass != null && mass != null) {
            return mass / other.mass
        } else if (other.volume != null && volume != null) {
            return volume / other.volume
        }
        return null
    }

    override fun hashCode(): Int {
        var result = mass?.hashCode() ?: 0
        result = 31 * result + (volume?.hashCode() ?: 0)
        return result
    }
}

sealed interface Nutrient

enum class Nutrients {
    Protein,

    TotalCarbohydrate,
    Fiber,
    Sugar,
    SugarAlcohol,
    Starch,

    TotalFat,
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
    ;

    companion object {
        val fats: List<Nutrients>
            get() = listOf(
                TotalFat,
                MonounsaturatedFat,
                PolyunsaturatedFat,
                Omega3,
                Omega6,
                SaturatedFat,
                TransFat,
                Cholesterol,
            )

        val carbohydrates: List<Nutrients>
            get() = listOf(
                TotalCarbohydrate,
                Fiber,
                Sugar,
                SugarAlcohol,
                Starch,
            )

        val minerals: List<Nutrients>
            get() = listOf(
                Calcium,
                Chloride,
                Iron,
                Magnesium,
                Phosphorous,
                Potassium,
                Sodium,
            )
        val vitamins: List<Nutrients>
            get() = listOf(
                VitaminA,
                VitaminC,
            )
    }
}

interface Protein : Nutrient {
    val totalProtein: Mass
}

interface Carbohydrate : Nutrient {
    val fiber: Mass?
    val sugar: Mass?
    val sugarAlcohol: Mass?
    val starch: Mass?
    val totalCarbohydrates: Mass
}

interface Fat : Nutrient {
    val monounsaturatedFat: Mass?
    val polyunsaturatedFat: Mass?
    val omega3: Mass?
    val omega6: Mass?
    val saturatedFat: Mass?
    val transFat: Mass?
    val cholesterol: Mass?
    val totalFat: Mass
}

interface MacroNutrients : Protein, Carbohydrate, Fat

interface Mineral : Nutrient {
    val calcium: Mass?
    val chloride: Mass?
    val iron: Mass?
    val magnesium: Mass?
    val phosphorous: Mass?
    val potassium: Mass?
    val sodium: Mass?
}

interface Vitamin : Nutrient {
    val vitaminA: Mass? get() = null
    val vitaminC: Mass? get() = null
}

interface NutritionInfo : MacroNutrients, Mineral, Vitamin {
    val portion: Portion
    val foodEnergy: FoodEnergy

    /**
     * Get the nutrient specified by [nutrient]. If nutrient is unset, it returns `null`
     */
    operator fun get(nutrient: Nutrients): Mass? {
        return when (nutrient) {
            Nutrients.Protein -> totalProtein

            Nutrients.TotalCarbohydrate -> totalCarbohydrates
            Nutrients.Fiber -> fiber
            Nutrients.Sugar -> sugar
            Nutrients.SugarAlcohol -> sugarAlcohol
            Nutrients.Starch -> starch

            Nutrients.TotalFat -> totalFat
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

data class Nutrition(
    override val portion: Portion = Portion(),
    override val totalProtein: Mass = 0.grams,
    override val fiber: Mass? = null,
    override val sugar: Mass? = null,
    override val sugarAlcohol: Mass? = null,
    override val starch: Mass? = null,
    override val totalCarbohydrates: Mass = 0.grams,
    override val monounsaturatedFat: Mass? = null,
    override val polyunsaturatedFat: Mass? = null,
    override val omega3: Mass? = null,
    override val omega6: Mass? = null,
    override val saturatedFat: Mass? = null,
    override val transFat: Mass? = null,
    override val cholesterol: Mass? = null,
    override val totalFat: Mass = 0.grams,
    override val foodEnergy: FoodEnergy = 0.kcal,
    override val calcium: Mass? = null,
    override val chloride: Mass? = null,
    override val iron: Mass? = null,
    override val magnesium: Mass? = null,
    override val phosphorous: Mass? = null,
    override val potassium: Mass? = null,
    override val sodium: Mass? = null,
    override val vitaminA: Mass? = null,
    override val vitaminC: Mass? = null,
) : NutritionInfo {

    operator fun plus(other: NutritionInfo): Nutrition {
        var result = Nutrition(
            portion = (portion + other.portion)!!,
            foodEnergy = foodEnergy + other.foodEnergy,
            totalCarbohydrates = totalCarbohydrates + other.totalCarbohydrates,
            totalFat = totalFat + other.totalFat,
        )
        for (nutrient in Nutrients.entries) {
            val mass = when (val thisMass = this[nutrient]) {
                null -> other[nutrient]
                else -> other[nutrient]?.let { it + thisMass } ?: thisMass
            }
            result = result.copy(nutrient = nutrient, weight = mass)
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

fun Nutrition.copy(nutrient: Nutrients, weight: Mass?): Nutrition {
    return when (nutrient) {
        Nutrients.Protein -> copy(totalProtein = weight ?: throw (Throwable("protein mut not be null")))

        Nutrients.TotalCarbohydrate -> copy(totalCarbohydrates = weight!!)
        Nutrients.Fiber -> copy(fiber = weight)
        Nutrients.Sugar -> copy(sugar = weight)
        Nutrients.SugarAlcohol -> copy(sugarAlcohol = weight)
        Nutrients.Starch -> copy(starch = weight)

        Nutrients.TotalFat -> copy(totalFat = weight!!)
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
    compareWeight: (Nutrients, Mass?, Mass?) -> Boolean,
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
