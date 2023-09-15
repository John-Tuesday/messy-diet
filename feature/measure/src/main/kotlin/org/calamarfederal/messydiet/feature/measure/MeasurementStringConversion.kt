package org.calamarfederal.messydiet.feature.measure

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.calamarfederal.messydiet.measure.LengthUnit
import org.calamarfederal.messydiet.measure.R
import org.calamarfederal.messydiet.measure.VolumeUnit
import org.calamarfederal.messydiet.measure.WeightUnit

val WeightUnit.labelString: String
    @Composable
    get() = stringResource(id = weightUnitToShortRId(this))

val WeightUnit.fullString: String
    @Composable
    get() = stringResource(id = weightUnitToFullRId(this))

val LengthUnit.labelString: String
    @Composable
    get() = stringResource(id = lengthUnitToShortRId(this))

val LengthUnit.fullString: String
    @Composable
    get() = stringResource(id = lengthUnitToFullRId(this))

val VolumeUnit.labelString: String
    @Composable
    get() = stringResource(id = volumeUnitToFullRId(this))

val VolumeUnit.fullString: String
    @Composable
    get() = stringResource(id = volumeUnitToShortRId(this))

fun weightUnitFullString(
    unitType: WeightUnit,
    resources: Resources,
) = resources.getString(weightUnitToFullRId(unitType))

fun weightUnitShortString(
    unitType: WeightUnit,
    resources: Resources,
) = resources.getString(weightUnitToShortRId(unitType))

fun lengthUnitFullString(
    unitType: LengthUnit,
    resources: Resources,
) = resources.getString(lengthUnitToFullRId(unitType))

fun lengthUnitShortString(
    unitType: LengthUnit,
    resources: Resources,
) = resources.getString(lengthUnitToShortRId(unitType))

fun volumeUnitFullString(
    unitType: VolumeUnit,
    resources: Resources,
) = resources.getString(volumeUnitToFullRId(unitType))

fun volumeUnitShortString(
    unitType: VolumeUnit,
    resources: Resources,
) = resources.getString(volumeUnitToShortRId(unitType))

@StringRes
internal fun weightUnitToFullRId(
    unitType: WeightUnit,
) = when (unitType) {
    WeightUnit.Kilogram -> R.string.kilogram
    WeightUnit.Gram -> R.string.gram
    WeightUnit.Milligram -> R.string.milligram
    WeightUnit.Micrograms -> R.string.microgram
    WeightUnit.Pound -> R.string.pound_weight
    WeightUnit.Ounce -> R.string.ounce_weight
}

@StringRes
internal fun weightUnitToShortRId(
    unitType: WeightUnit,
) = when (unitType) {
    WeightUnit.Kilogram -> R.string.kilogram_label
    WeightUnit.Gram -> R.string.gram_label
    WeightUnit.Milligram -> R.string.milligram_label
    WeightUnit.Micrograms -> R.string.microgram_label
    WeightUnit.Pound -> R.string.pound_weight_label
    WeightUnit.Ounce -> R.string.ounce_weight_label
}

@StringRes
internal fun lengthUnitToFullRId(
    unitType: LengthUnit,
): Int {
    return when (unitType) {
        LengthUnit.Kilometer -> R.string.kilometer
        LengthUnit.Meter -> R.string.meter
        LengthUnit.Centimeter -> R.string.centimeter
        LengthUnit.Millimeter -> R.string.millimeter
        LengthUnit.Mile -> R.string.mile
        LengthUnit.Feet -> R.string.feet
        LengthUnit.Inch -> R.string.inch
    }
}

@StringRes
internal fun lengthUnitToShortRId(
    unitType: LengthUnit,
) = when (unitType) {
    LengthUnit.Kilometer -> R.string.kilometer_label
    LengthUnit.Meter -> R.string.meter_label
    LengthUnit.Centimeter -> R.string.centimeter_label
    LengthUnit.Millimeter -> R.string.millimeter_label
    LengthUnit.Mile -> R.string.mile_label
    LengthUnit.Feet -> R.string.feet_label
    LengthUnit.Inch -> R.string.inch_label
}

@StringRes
internal fun volumeUnitToFullRId(
    unitType: VolumeUnit,
) = when (unitType) {
    VolumeUnit.Milliliter -> R.string.milliliter
    VolumeUnit.Liter -> R.string.liter
    VolumeUnit.UsGallon -> R.string.us_gallon
    VolumeUnit.UsTablespoon -> R.string.us_tablespoon
    VolumeUnit.UsTeaspoon -> R.string.us_teaspoon
}

@StringRes
internal fun volumeUnitToShortRId(
    unitType: VolumeUnit,
) = when (unitType) {
    VolumeUnit.Milliliter -> R.string.milliliter_label
    VolumeUnit.Liter -> R.string.liter_label
    VolumeUnit.UsGallon -> R.string.us_gallon_label
    VolumeUnit.UsTablespoon -> R.string.us_tablespoon_label
    VolumeUnit.UsTeaspoon -> R.string.us_teaspoon_label
}
