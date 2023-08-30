package org.calamarfederal.messydiet.food.data.central

import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.diet_data.model.kcal
import org.calamarfederal.messydiet.food.data.central.model.BrandedFDCId
import org.calamarfederal.messydiet.food.data.central.model.FDCDataType
import org.calamarfederal.messydiet.food.data.central.remote.schema.DataTypeSchema
import org.calamarfederal.messydiet.measure.grams
import org.calamarfederal.messydiet.measure.milligrams

internal fun prettyPrint(nutrition: Nutrition) {
    println(nutrition.toString().replace(' ', '\n'))
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
        totalProtein = 0.grams,
        totalFat = 0.grams,
        totalCarbohydrates = 10.8.grams,
        foodEnergy = 39.kcal,
        //caffeine = 0.grams,
        sugar = 10.6.grams,
        fiber = 0.grams,
        calcium = 0.grams,
        iron = 0.grams,
        sodium = 19.0.milligrams,
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
