package org.calamarfederal.messydiet.remote.food.data.central.test

import io.github.john.tuesday.nutrition.FoodNutrition
import io.github.john.tuesday.nutrition.NutrientType
import io.github.john.tuesday.nutrition.Portion
import org.calamarfederal.messydiet.food.data.central.model.FDCDataType
import org.calamarfederal.messydiet.food.data.central.model.FDCId
import org.calamarfederal.messydiet.food.data.central.model.foodDataCentralId
import org.calamarfederal.physical.measurement.*

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
    abstract val foodNutrition: FoodNutrition
    open val nutritionPerServing: FoodNutrition get() = foodNutrition
    open val nutritionPer100: FoodNutrition get() = foodNutrition

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
        override val foodNutrition: FoodNutrition = FoodNutrition(
            portion = Portion(360.milliliters),
            foodEnergy = 39.kilocalories,
            nutritionMap = mapOf(
                NutrientType.Protein to 0.grams,
                NutrientType.TotalFat to 0.grams,
                NutrientType.SaturatedFat to 0.grams,
                NutrientType.TransFat to 0.grams,
                NutrientType.Cholesterol to 0.grams,
                NutrientType.TotalCarbohydrate to 10.83.grams,
                //caffeine to 0.grams,
                NutrientType.Sugar to 10.56.grams, // 10.6 added
                NutrientType.Fiber to 0.grams,
                NutrientType.Calcium to 0.grams,
                NutrientType.Iron to 0.grams,
                NutrientType.Sodium to 19.0.milligrams,
                NutrientType.VitaminC to 0.milligrams,
                NutrientType.VitaminA to 0.micrograms,
            ),
        )

        override val nutritionPerServing: FoodNutrition = foodNutrition
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
        override val nutritionPer100: FoodNutrition = FoodNutrition(
            portion = Portion(100.grams),
            foodEnergy = 359.kilocalories,
            nutritionMap = mapOf(
                NutrientType.Protein to 12.8.grams,
                NutrientType.TotalFat to 6.41.grams,
                NutrientType.TotalCarbohydrate to 74.4.grams,
                NutrientType.Fiber to 10.3.grams,
                NutrientType.Sugar to 5.13.grams, // 5.1 added
                NutrientType.Calcium to 333.milligrams,
                NutrientType.Iron to 32.3.milligrams,
//        NutrientType.Magnesium to 700.milligrams, // 700 from DV
//        NutrientType.Phosphorous to 123.milligrams, // 256 from DV ?? 750
                NutrientType.Potassium to 641.milligrams,
                NutrientType.Sodium to 487.milligrams,
//        zinc
//        NutrientType.VitaminC to 90.milligrams, // ?? 15.4 3.7
                NutrientType.SaturatedFat to 1.28.grams,
                NutrientType.MonounsaturatedFat to 2.56.grams,
                NutrientType.PolyunsaturatedFat to 2.56.grams,
                NutrientType.TransFat to 0.grams,
                NutrientType.Cholesterol to 0.milligrams,
            ),
        )
        override val nutritionPerServing: FoodNutrition = FoodNutrition(
            portion = Portion(20.grams),
            foodEnergy = 117.kilocalories,
            nutritionMap = mapOf(
                NutrientType.Protein to 5.56.grams,
                NutrientType.TotalFat to 1.85.grams,
                NutrientType.TotalCarbohydrate to 21.6.grams,
                NutrientType.Fiber to 2.5.grams,
                NutrientType.Sugar to 4.93.grams, // 1.2 added
                NutrientType.Calcium to 160.milligrams,
                NutrientType.Iron to 7.78.milligrams,
                NutrientType.Magnesium to 37.milligrams,
//        NutrientType.Phosphorous to 123.milligrams,
                NutrientType.Potassium to 278.milligrams,
                NutrientType.Sodium to 487.milligrams,
//        zinc
                NutrientType.VitaminC to 90.milligrams, // ?? 15.4 3.7
                NutrientType.SaturatedFat to 0.62.grams,
                NutrientType.MonounsaturatedFat to 0.62.grams,
                NutrientType.PolyunsaturatedFat to 0.62.grams,
                NutrientType.TransFat to 0.grams,
                NutrientType.Cholesterol to 0.milligrams,
            ),
        )

        override val foodNutrition: FoodNutrition = nutritionPerServing
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
        override val nutritionPerServing: FoodNutrition
            get() = TODO("Not yet implemented")
        override val nutritionPer100: FoodNutrition = FoodNutrition(
            portion = Portion(100.grams),
            foodEnergy = 263.kilocalories,
            nutritionMap = mapOf(
                NutrientType.Protein to 0.grams,
                NutrientType.TotalFat to 0.grams,
                NutrientType.TotalCarbohydrate to 52.6.grams,
                NutrientType.Sugar to 0.grams,
                NutrientType.SugarAlcohol to 53.grams,
                NutrientType.Sodium to 0.grams,
            ),
        )

        override val foodNutrition: FoodNutrition = CheeriosTestA.nutritionPerServing
    }
}
