package org.calamarfederal.messydiet.food.data.central

import org.calamarfederal.messydiet.diet_data.model.*
import org.calamarfederal.messydiet.food.data.central.model.BrandedFDCId
import org.calamarfederal.messydiet.food.data.central.model.FDCDataType
import org.calamarfederal.messydiet.food.data.central.remote.schema.DataTypeSchema
import org.calamarfederal.messydiet.measure.grams
import org.calamarfederal.messydiet.measure.milligrams
import org.calamarfederal.messydiet.measure.milliliters

internal fun prettyFormatDataClassString(str: String, tab: String = "    "): String = str
    .replace("(", "(\n$tab")
    .replace(")", "\n)")
    .replace(", ", ",\n$tab")

internal fun prettyPrint(nutrition: Nutrition, tab: String = "    ") {
    println(
        prettyFormatDataClassString(nutrition.toString(), tab = tab)
    )
}

internal data object SpriteTest {
    const val spriteUpc = "00049000057980"
    const val spriteFdcIdString = "2556784"
    const val spriteFdcIdInt = 2556784
    val spriteType = FDCDataType.Branded
    val spriteTypeSchema = DataTypeSchema.Branded
    val spriteFdcId = BrandedFDCId(spriteFdcIdInt)
    const val spriteName = "Sprite Bottle, 1.75 Liters"
    val spriteNutrition = Nutrition(
        portion = Portion(360.milliliters),
        totalProtein = 0.grams,
        totalFat = 0.grams,
        saturatedFat = 0.grams,
        transFat = 0.grams,
        totalCarbohydrates = 10.8.grams,
        foodEnergy = 39.kcal,
        //caffeine = 0.grams,
        sugar = 10.6.grams,
        fiber = 0.grams,
        cholesterol = 0.grams,
        calcium = 0.grams,
        iron = 0.grams,
        sodium = 19.0.milligrams,
        vitaminC = 0.milligrams,
    )
}

internal data object BadSpriteTest {
    const val spriteUpc = "00049000052091"
    const val spriteFdcIdString = "2556776"
    const val spriteFdcIdInt = 2556776
    val spriteType = FDCDataType.Branded
    val spriteTypeSchema = DataTypeSchema.Branded
    val spriteFdcId = BrandedFDCId(spriteFdcIdInt)
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

internal data object CheeriosTestA {
    const val cheeriosUpcGtin = "00016000275287"
    const val cheeriosFdcIdString = "2517161"
    const val cheeriosFdcIdInt = 2517161
    val cheeriosFdcId = BrandedFDCId(cheeriosFdcIdInt)
    val cheeriosSourceType = FDCDataType.Branded
    val cheeriosDataTypeSchema = DataTypeSchema.Branded
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
