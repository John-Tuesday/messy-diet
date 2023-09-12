package org.calamarfederal.messydiet.test.food.data.central

import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.diet_data.model.Portion
import org.calamarfederal.messydiet.diet_data.model.kcal
import org.calamarfederal.messydiet.food.data.central.model.FDCDataType
import org.calamarfederal.messydiet.food.data.central.model.foodDataCentralId
import org.calamarfederal.messydiet.measure.grams
import org.calamarfederal.messydiet.measure.micrograms
import org.calamarfederal.messydiet.measure.milligrams
import org.calamarfederal.messydiet.measure.milliliters

data object FoodItemExpect {
    data object SpriteTest {
        const val spriteUpc = "00049000057980"
        const val spriteFdcIdString = "2613419"//"2556784"
        const val spriteFdcIdInt = 2613419
        val spriteFdcId = foodDataCentralId(id = spriteFdcIdInt, dataType = FDCDataType.Branded)
        const val spriteName = "Sprite Bottle, 1.75 Liters"
        const val spriteSearchDescription = spriteName
        const val publicationDateYear = 2023
        const val publicationDateMonthOfYear = 8
        const val publicationDateDayOfMonth = 31
//        val oldSpriteNutritionPerServing = Nutrition(
//            portion = Portion(360.milliliters),
//            foodEnergy = 39.kcal,
//            totalProtein = 0.grams,
//            totalFat = 0.grams,
//            saturatedFat = 0.grams,
//            transFat = 0.grams,
//            cholesterol = 0.grams,
//            totalCarbohydrates = 10.83.grams,
//            //caffeine = 0.grams,
//            sugar = 10.56.grams, // 10.6 added
//            fiber = 0.grams,
//            calcium = 0.grams,
//            iron = 0.grams,
//            sodium = 19.0.milligrams,
//            vitaminC = 0.milligrams,
//        )

        val spriteNutritionPerServing = Nutrition(
            portion = Portion(360.milliliters),
            foodEnergy = 39.kcal,
            totalProtein = 0.grams,
            totalFat = 0.grams,
            saturatedFat = 0.grams,
            transFat = 0.grams,
            cholesterol = 0.grams,
            totalCarbohydrates = 10.83.grams,
            //caffeine = 0.grams,
            sugar = 10.56.grams, // 10.6 added
            fiber = 0.grams,
            calcium = 0.grams,
            iron = 0.grams,
            sodium = 19.0.milligrams,
            vitaminC = 0.milligrams,
            vitaminA = 0.micrograms,
        )
    }

    data object BadSpriteTest {
        const val spriteUpc = "00049000052091"
        const val spriteFdcIdString = "2556776"
        const val spriteFdcIdInt = 2556776
        val spriteFdcId = foodDataCentralId(id = spriteFdcIdInt, dataType = FDCDataType.Branded)
        const val spriteName = "Sprite Bottle, 2 Liters"
        val spriteNutrition = Nutrition(
            totalProtein = 0.grams,
            totalFat = 0.grams,
            totalCarbohydrates = 0.grams,
            foodEnergy = 0.kcal,
            //caffeine = 0.grams,
            sugar = 0.grams,
            fiber = 0.grams,
            calcium = 0.grams,
            iron = 0.grams,
            sodium = 0.milligrams,
        )
    }

    data object CheeriosTestA {
        const val cheeriosUpcGtin = "00016000275287"
        const val cheeriosFdcIdString = "2517161"
        const val cheeriosFdcIdInt = 2517161
        val cheeriosFdcId = foodDataCentralId(id = cheeriosFdcIdInt, dataType = FDCDataType.Branded)
        const val cheeriosName = "Cheerios Cereal"
        val cheeriosNutritionPer100 = Nutrition(
            portion = Portion(100.grams),
            foodEnergy = 359.kcal,
            totalProtein = 12.8.grams,
            totalFat = 6.41.grams,
            totalCarbohydrates = 74.4.grams,
            fiber = 10.3.grams,
            sugar = 5.13.grams, // 5.1 added
            calcium = 333.milligrams,
            iron = 32.3.milligrams,
//        magnesium = 700.milligrams, // 700 from DV
//        phosphorous = 123.milligrams, // 256 from DV ?? 750
            potassium = 641.milligrams,
            sodium = 487.milligrams,
//        zinc
//        vitaminC = 90.milligrams, // ?? 15.4 3.7
            saturatedFat = 1.28.grams,
            monounsaturatedFat = 2.56.grams,
            polyunsaturatedFat = 2.56.grams,
            transFat = 0.grams,
            cholesterol = 0.milligrams,
        )
        val cheeriosNutritionPerServing = Nutrition(
            portion = Portion(20.grams),
            foodEnergy = 117.kcal,
            totalProtein = 5.56.grams,
            totalFat = 1.85.grams,
            totalCarbohydrates = 21.6.grams,
            fiber = 2.5.grams,
            sugar = 4.93.grams, // 1.2 added
            calcium = 160.milligrams,
            iron = 7.78.milligrams,
            magnesium = 37.milligrams,
//        phosphorous = 123.milligrams,
            potassium = 278.milligrams,
            sodium = 487.milligrams,
//        zinc
            vitaminC = 90.milligrams, // ?? 15.4 3.7
            saturatedFat = 0.62.grams,
            monounsaturatedFat = 0.62.grams,
            polyunsaturatedFat = 0.62.grams,
            transFat = 0.grams,
            cholesterol = 0.milligrams,
        )
    }
}
