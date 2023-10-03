package org.calamarfederal.messydiet.test.measure

import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.diet_data.model.Portion
import org.calamarfederal.messydiet.diet_data.model.kcal
import org.calamarfederal.physical.measurement.grams
import org.calamarfederal.physical.measurement.milligrams

data object MeasureSamples {
    val filledNutritionA = Nutrition(
        portion = Portion(100.grams),
        foodEnergy = 359.kcal,
        totalProtein = 12.8.grams,
        totalFat = 6.41.grams,
        totalCarbohydrates = 74.4.grams,
        fiber = 10.3.grams,
        starch = 8.11.grams,
        sugar = 5.13.grams,
        sugarAlcohol = 7.4.grams,
        calcium = 333.milligrams,
        iron = 32.3.milligrams,
        magnesium = 154.milligrams,
        phosphorous = 123.milligrams,
        potassium = 641.milligrams,
        sodium = 487.milligrams,
        saturatedFat = 1.28.grams,
        monounsaturatedFat = 2.56.grams,
        polyunsaturatedFat = 2.56.grams,
        transFat = 0.grams,
        cholesterol = 0.milligrams,
        omega3 = 1.33.grams,
        omega6 = 1.24.grams,
        chloride = 13.grams,
        vitaminA = 10.milligrams,
        vitaminC = 90.milligrams,
    )

    val filledNonZeroNutritionA = Nutrition(
        portion = Portion(100.grams),
        foodEnergy = 359.kcal,
        totalProtein = 12.8.grams,
        totalFat = 6.41.grams,
        totalCarbohydrates = 74.4.grams,
        fiber = 10.3.grams,
        starch = 8.11.grams,
        sugar = 5.13.grams,
        sugarAlcohol = 7.4.grams,
        calcium = 333.milligrams,
        iron = 32.3.milligrams,
        magnesium = 154.milligrams,
        phosphorous = 123.milligrams,
        potassium = 641.milligrams,
        sodium = 487.milligrams,
        saturatedFat = 1.28.grams,
        monounsaturatedFat = 2.56.grams,
        polyunsaturatedFat = 2.56.grams,
        transFat = 2.31.grams,
        cholesterol = 3.21.milligrams,
        omega3 = 1.33.grams,
        omega6 = 1.24.grams,
        chloride = 13.grams,
        vitaminA = 10.milligrams,
        vitaminC = 90.milligrams,
    )
}
