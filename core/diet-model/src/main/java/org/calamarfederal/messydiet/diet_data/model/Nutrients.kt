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

import org.calamarfederal.messydiet.measure.Weight
import org.calamarfederal.messydiet.measure.grams
import org.calamarfederal.messydiet.measure.plus

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
    val portion: Weight
    val foodEnergy: FoodEnergy
}

data class Nutrition constructor(
    override val portion: Weight = 0.grams,
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

    operator fun plus(other: NutritionInfo): Nutrition{
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
}
