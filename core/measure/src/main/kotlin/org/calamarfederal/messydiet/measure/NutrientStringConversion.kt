package org.calamarfederal.messydiet.measure

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.github.john.tuesday.nutrition.MassPortion
import io.github.john.tuesday.nutrition.NutrientType
import io.github.john.tuesday.nutrition.Portion
import io.github.john.tuesday.nutrition.VolumePortion
import org.calamarfederal.physical.measurement.MassUnit
import org.calamarfederal.physical.measurement.VolumeUnit
import org.calamarfederal.physical.measurement.inUnitsOf

/**
 * use [LocalContext] to get the current [Resources] and get the name of `this`
 */
val NutrientType.fullName: String
    @Composable
    get() = nutrientFullName(this, LocalContext.current.resources)

fun NutrientType.getFullName(resources: Resources): String = nutrientFullName(this, resources)

/**
 * get the [R.string] resource id full name of `this` [NutrientType]
 */
val NutrientType.stringResId: Int
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

fun NutrientType.defaultUnit(): MassUnit = when (this) {
    NutrientType.Protein -> MassUnit.Gram

    NutrientType.TotalCarbohydrate -> MassUnit.Gram
    NutrientType.Fiber -> MassUnit.Gram
    NutrientType.Sugar -> MassUnit.Gram
    NutrientType.SugarAlcohol -> MassUnit.Gram
    NutrientType.Starch -> MassUnit.Gram

    NutrientType.TotalFat -> MassUnit.Gram
    NutrientType.MonounsaturatedFat -> MassUnit.Gram
    NutrientType.PolyunsaturatedFat -> MassUnit.Gram
    NutrientType.Omega3 -> MassUnit.Gram
    NutrientType.Omega6 -> MassUnit.Gram
    NutrientType.SaturatedFat -> MassUnit.Gram
    NutrientType.TransFat -> MassUnit.Gram
    NutrientType.Cholesterol -> MassUnit.Milligram

    NutrientType.Calcium -> MassUnit.Milligram
    NutrientType.Chloride -> MassUnit.Milligram
    NutrientType.Iron -> MassUnit.Milligram
    NutrientType.Magnesium -> MassUnit.Milligram
    NutrientType.Phosphorous -> MassUnit.Milligram
    NutrientType.Potassium -> MassUnit.Milligram
    NutrientType.Sodium -> MassUnit.Milligram
    NutrientType.VitaminA -> MassUnit.Milligram
    NutrientType.VitaminC -> MassUnit.Milligram
}

fun Portion.inDefaultUnits(): Double = when (this) {
    is MassPortion -> mass.inUnitsOf(defaultUnit())
    is VolumePortion -> volume.inUnitsOf(defaultUnit())
}

fun Portion.defaultUnitLabel(resources: Resources): String = when (this) {
    is MassPortion -> weightUnitShortString(defaultUnit(), resources)
    is VolumePortion -> volumeUnitShortString(defaultUnit(), resources)
}

fun MassPortion.defaultUnit(): MassUnit = MassUnit.Gram
fun VolumePortion.defaultUnit(): VolumeUnit = VolumeUnit.Milliliter
