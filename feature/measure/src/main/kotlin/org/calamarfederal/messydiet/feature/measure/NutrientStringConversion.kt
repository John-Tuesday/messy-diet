package org.calamarfederal.messydiet.feature.measure

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.calamarfederal.messydiet.diet_data.model.Nutrients
import org.calamarfederal.messydiet.diet_data.model.Nutrients.*
import org.calamarfederal.messydiet.measure.R
import org.calamarfederal.messydiet.measure.R.string

/**
 * use [LocalContext] to get the current [Resources] and get the name of `this`
 */
val Nutrients.fullName: String
    @Composable
    get() = nutrientFullName(this, LocalContext.current.resources)

/**
 * get the [R.string] resource id full name of `this` [Nutrients]
 */
internal val Nutrients.stringResId: Int
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

private fun nutrientFullName(nutrient: Nutrients, resources: Resources): String =
    resources.getString(nutrient.stringResId)
