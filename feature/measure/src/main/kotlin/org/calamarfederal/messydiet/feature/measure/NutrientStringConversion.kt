package org.calamarfederal.messydiet.feature.measure

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.calamarfederal.messydiet.diet_data.model.Nutrients
import org.calamarfederal.messydiet.diet_data.model.Nutrients.*

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
        Protein -> R.string.protein

        TotalCarbohydrate -> R.string.carbohydrates
        Fiber -> R.string.fiber
        Sugar -> R.string.sugar
        SugarAlcohol -> R.string.sugar_alcohol
        Starch -> R.string.starch

        TotalFat -> R.string.fat
        MonounsaturatedFat -> R.string.monounsaturated_fat
        PolyunsaturatedFat -> R.string.polyunsaturated_fat
        Omega3 -> R.string.omega3_fat
        Omega6 -> R.string.omega6_fat
        SaturatedFat -> R.string.saturated_fat
        TransFat -> R.string.trans_fat
        Cholesterol -> R.string.cholesterol

        Calcium -> R.string.calcium
        Chloride -> R.string.chloride
        Iron -> R.string.iron
        Magnesium -> R.string.magnesium
        Phosphorous -> R.string.phosphorous
        Potassium -> R.string.potassium
        Sodium -> R.string.sodium
        VitaminA -> R.string.vitamin_a
        VitaminC -> R.string.vitamin_c
    }

private fun nutrientFullName(nutrient: Nutrients, resources: Resources): String =
    resources.getString(nutrient.stringResId)
