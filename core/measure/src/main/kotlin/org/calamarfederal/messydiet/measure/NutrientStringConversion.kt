package org.calamarfederal.messydiet.measure

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.github.john.tuesday.nutrition.NutrientType

/**
 * use [LocalContext] to get the current [Resources] and get the name of `this`
 */
val NutrientType.fullName: String
    @Composable
    get() = nutrientFullName(this, LocalContext.current.resources)

/**
 * get the [R.string] resource id full name of `this` [NutrientType]
 */
internal val NutrientType.stringResId: Int
    @StringRes
    get() = when (this) {
        NutrientType.Protein -> R.string.protein

        NutrientType.TotalCarbohydrate -> R.string.carbohydrates
        NutrientType.Fiber -> R.string.fiber
        NutrientType.Sugar -> R.string.sugar
        NutrientType.SugarAlcohol -> R.string.sugar_alcohol
        NutrientType.Starch -> R.string.starch

        NutrientType.TotalFat -> R.string.fat
        NutrientType.MonounsaturatedFat -> R.string.monounsaturated_fat
        NutrientType.PolyunsaturatedFat -> R.string.polyunsaturated_fat
        NutrientType.Omega3 -> R.string.omega3_fat
        NutrientType.Omega6 -> R.string.omega6_fat
        NutrientType.SaturatedFat -> R.string.saturated_fat
        NutrientType.TransFat -> R.string.trans_fat
        NutrientType.Cholesterol -> R.string.cholesterol

        NutrientType.Calcium -> R.string.calcium
        NutrientType.Chloride -> R.string.chloride
        NutrientType.Iron -> R.string.iron
        NutrientType.Magnesium -> R.string.magnesium
        NutrientType.Phosphorous -> R.string.phosphorous
        NutrientType.Potassium -> R.string.potassium
        NutrientType.Sodium -> R.string.sodium
        NutrientType.VitaminA -> R.string.vitamin_a
        NutrientType.VitaminC -> R.string.vitamin_c
    }

internal fun nutrientFullName(nutrient: NutrientType, resources: Resources): String =
    resources.getString(nutrient.stringResId)
