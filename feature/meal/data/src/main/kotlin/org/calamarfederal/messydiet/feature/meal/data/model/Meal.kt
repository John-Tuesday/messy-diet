package org.calamarfederal.messydiet.feature.meal.data.model

import org.calamarfederal.messydiet.diet_data.model.FoodEnergy
import org.calamarfederal.messydiet.diet_data.model.NutritionInfo
import org.calamarfederal.messydiet.diet_data.model.Portion
import org.calamarfederal.messydiet.diet_data.model.kcal
import org.calamarfederal.messydiet.feature.meal.data.local.MealEntity
import org.calamarfederal.physical.measurement.Mass
import org.calamarfederal.physical.measurement.grams

data class Meal(
    val id: Long = MealEntity.UNSET_ID,
    val name: String = "",
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
    override val calcium: Mass? = null,
    override val chloride: Mass? = null,
    override val iron: Mass? = null,
    override val magnesium: Mass? = null,
    override val phosphorous: Mass? = null,
    override val potassium: Mass? = null,
    override val sodium: Mass? = null,
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
