package org.calamarfederal.messydiet.feature.meal.data.model

import org.calamarfederal.messydiet.diet_data.model.FoodEnergy
import org.calamarfederal.messydiet.diet_data.model.NutritionInfo
import org.calamarfederal.messydiet.diet_data.model.Portion
import org.calamarfederal.messydiet.diet_data.model.kcal
import org.calamarfederal.messydiet.feature.meal.data.local.MealEntity
import org.calamarfederal.messydiet.measure.Weight
import org.calamarfederal.messydiet.measure.grams

data class Meal(
    val id: Long = MealEntity.UNSET_ID,
    val name: String = "",
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
    override val calcium: Weight? = null,
    override val chloride: Weight? = null,
    override val iron: Weight? = null,
    override val magnesium: Weight? = null,
    override val phosphorous: Weight? = null,
    override val potassium: Weight? = null,
    override val sodium: Weight? = null,
    override val portion: Portion = Portion(),
    override val foodEnergy: FoodEnergy = 0.kcal,
) : NutritionInfo {
    constructor(name: String, nutritionInfo: NutritionInfo, id: Long = MealEntity.UNSET_ID) : this(
        id = id,
        name = name,
        totalProtein = nutritionInfo.totalProtein,
        fiber = nutritionInfo.fiber,
        sugar = nutritionInfo.sugar,
        sugarAlcohol = nutritionInfo.sugarAlcohol,
        starch = nutritionInfo.starch,
        totalCarbohydrates = nutritionInfo.totalCarbohydrates,
        monounsaturatedFat = nutritionInfo.monounsaturatedFat,
        polyunsaturatedFat = nutritionInfo.polyunsaturatedFat,
        omega3 = nutritionInfo.omega3,
        omega6 = nutritionInfo.omega6,
        saturatedFat = nutritionInfo.saturatedFat,
        transFat = nutritionInfo.transFat,
        cholesterol = nutritionInfo.cholesterol,
        totalFat = nutritionInfo.totalFat,
        calcium = nutritionInfo.calcium,
        chloride = nutritionInfo.chloride,
        iron = nutritionInfo.iron,
        magnesium = nutritionInfo.magnesium,
        phosphorous = nutritionInfo.phosphorous,
        potassium = nutritionInfo.potassium,
        sodium = nutritionInfo.sodium,
        portion = nutritionInfo.portion,
        foodEnergy = nutritionInfo.foodEnergy,
    )
}
