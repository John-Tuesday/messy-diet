package org.calamarfederal.messydiet.test.measure

import org.calamarfederal.messydiet.diet_data.model.Nutrients
import org.calamarfederal.messydiet.diet_data.model.NutritionInfo

fun NutritionInfo.toPrettyString(
    separator: String = ",\n    ",
    fieldsPrefix: String = "\n    ",
): String {
    return buildString {
        append("${NutritionInfo::class.simpleName}($fieldsPrefix")

        append("Portion= $portion$separator")
        append("Food energy= $foodEnergy")
        for (nutrient in Nutrients.entries) {
            append(separator)
            append("$nutrient= ${get(nutrient)}")
        }
    }
}

fun prettyPrint(nutritionInfo: NutritionInfo) {
    println(nutritionInfo.toPrettyString())
}
