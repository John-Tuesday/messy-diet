package org.calamarfederal.messydiet.test.food.data.central

import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.diet_data.model.NutritionInfo
import org.calamarfederal.messydiet.diet_data.model.Portion
import org.calamarfederal.messydiet.diet_data.model.kcal
import org.calamarfederal.messydiet.food.data.central.model.FDCDataType
import org.calamarfederal.messydiet.food.data.central.model.FDCId
import org.calamarfederal.messydiet.food.data.central.model.foodDataCentralId
import org.calamarfederal.physical.measurement.grams
import org.calamarfederal.physical.measurement.micrograms
import org.calamarfederal.physical.measurement.milligrams
import org.calamarfederal.physical.measurement.milliliters

sealed class FoodItemExpectCase(
    val gtinUpc: String,
    val fdcIdString: String,
    val fdcIdInt: Int,
    open val fdcId: FDCId,

    val searchDescription: String,

    val publicationDateYear: Int,
    val publicationDateMonthOfYear: Int,
    val publicationDateDayOfMonth: Int,

    ) {
    abstract val nutritionPerServing: NutritionInfo
    abstract val nutritionPer100: NutritionInfo

    open val name: String get() = searchDescription
}

data object FoodItemExpect {
    data object SpriteTest : FoodItemExpectCase(
        gtinUpc = "00049000057980",
        fdcIdString = "2613419",
        fdcIdInt = 2613419,
        fdcId = foodDataCentralId(id = 2613419, dataType = FDCDataType.Branded),
        searchDescription = "Sprite Bottle, 1.75 Liters",
        publicationDateMonthOfYear = 2023,
        publicationDateDayOfMonth = 31,
        publicationDateYear = 8,
    ) {
        override val nutritionPer100: NutritionInfo
            get() = TODO("Not yet implemented")

        override val nutritionPerServing: Nutrition = Nutrition(
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

    data object CheeriosTestA : FoodItemExpectCase(
        gtinUpc = "00016000275287",
        fdcIdString = "2517161",
        fdcIdInt = 2517161,
        fdcId = foodDataCentralId(id = 2517161, dataType = FDCDataType.Branded),
        searchDescription = "Cheerios Cereal",
        publicationDateMonthOfYear = 2023,
        publicationDateDayOfMonth = 31,
        publicationDateYear = 8,
    ) {
        override val nutritionPer100: Nutrition = Nutrition(
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
        override val nutritionPerServing: Nutrition = Nutrition(
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

    data object TridentGumTest : FoodItemExpectCase(
        gtinUpc = "012546011099",
        fdcIdString = "2494589",
        fdcIdInt = 2494589,
        fdcId = foodDataCentralId(
            id = 2494589,
            dataType = FDCDataType.Branded,
        ),
        searchDescription = "ORIGINAL SUGAR FREE GUM WITH XYLITOL, ORIGINAL",
        publicationDateYear = 2023,
        publicationDateMonthOfYear = 3,
        publicationDateDayOfMonth = 16,
    ) {
        override val nutritionPerServing: NutritionInfo
            get() = TODO("Not yet implemented")
        override val nutritionPer100: Nutrition = Nutrition(
            portion = Portion(100.grams),
            foodEnergy = 263.kcal,
            totalProtein = 0.grams,
            totalFat = 0.grams,
            totalCarbohydrates = 52.6.grams,
            sugar = 0.grams,
            sugarAlcohol = 53.grams,
            sodium = 0.grams,
        )
    }
}
