package org.calamarfederal.messydiet.feature.measure

import androidx.annotation.StringRes
import org.calamarfederal.messydiet.diet_data.model.Nutrients
import org.calamarfederal.messydiet.diet_data.model.Nutrients.*
import org.calamarfederal.messydiet.measure.R.string

val Nutrients.stringResId: Int
    @StringRes
    get() = when (this) {
        Protein -> string.protein

        TotalCarbohydrate -> string.carbohydrates
        Fiber -> string.fiber
        Sugar -> string.sugar
        SugarAlcohol -> string.sugar_alcohol
        Starch -> string.starch

        TotalFat -> string.fat
        MonounsaturatedFat -> string.monounsaturated_fat
        PolyunsaturatedFat -> string.polyunsaturated_fat
        Omega3 -> string.omega3_fat
        Omega6 -> string.omega6_fat
        SaturatedFat -> string.saturated_fat
        TransFat -> string.trans_fat
        Cholesterol -> string.cholesterol

        Calcium -> string.calcium
        Chloride -> string.chloride
        Iron -> string.iron
        Magnesium -> string.magnesium
        Phosphorous -> string.phosphorous
        Potassium -> string.potassium
        Sodium -> string.sodium
        VitaminA -> string.vitamin_a
        VitaminC -> string.vitamin_c
    }
