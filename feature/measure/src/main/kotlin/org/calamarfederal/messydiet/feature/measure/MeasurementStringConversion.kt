package org.calamarfederal.messydiet.feature.measure

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.calamarfederal.messydiet.measure.LengthUnit
import org.calamarfederal.messydiet.measure.VolumeUnit
import org.calamarfederal.messydiet.measure.WeightUnit

/**
 * Unit symbol / abbreviation
 */
val WeightUnit.labelString: String
    @Composable
    get() = stringResource(id = weightUnitToShortRId(this))

/**
 * Unit full name
 */
val WeightUnit.fullString: String
    @Composable
    get() = stringResource(id = weightUnitToFullRId(this))

/**
 * Unit symbol / abbreviation
 */
val LengthUnit.labelString: String
    @Composable
    get() = stringResource(id = lengthUnitToShortRId(this))

/**
 * Unit full name
 */
val LengthUnit.fullString: String
    @Composable
    get() = stringResource(id = lengthUnitToFullRId(this))

/**
 * Unit symbol / abbreviation
 */
val VolumeUnit.labelString: String
    @Composable
    get() = stringResource(id = volumeUnitToShortRId(this))

/**
 * Unit full name
 */
val VolumeUnit.fullString: String
    @Composable
    get() = stringResource(id = volumeUnitToFullRId(this))

/**
 * Unit full name
 */
fun weightUnitFullString(
    unitType: WeightUnit,
    resources: Resources,
): String = resources.getString(weightUnitToFullRId(unitType))

/**
 * Unit symbol / abbreviation
 */
fun weightUnitShortString(
    unitType: WeightUnit,
    resources: Resources,
): String = resources.getString(weightUnitToShortRId(unitType))

/**
 * Unit full name
 */
fun lengthUnitFullString(
    unitType: LengthUnit,
    resources: Resources,
): String = resources.getString(lengthUnitToFullRId(unitType))

/**
 * Unit symbol / abbreviation
 */
fun lengthUnitShortString(
    unitType: LengthUnit,
    resources: Resources,
): String = resources.getString(lengthUnitToShortRId(unitType))

/**
 * Unit full name
 */
fun volumeUnitFullString(
    unitType: VolumeUnit,
    resources: Resources,
): String = resources.getString(volumeUnitToFullRId(unitType))

/**
 * Unit symbol / abbreviation
 */
fun volumeUnitShortString(
    unitType: VolumeUnit,
    resources: Resources,
): String = resources.getString(volumeUnitToShortRId(unitType))

/**
 * Unit full name
 */
@StringRes
internal fun weightUnitToFullRId(
    unitType: WeightUnit,
): Int = when (unitType) {
    WeightUnit.Kilogram -> R.string.kilogram
    WeightUnit.Gram -> R.string.gram
    WeightUnit.Milligram -> R.string.milligram
    WeightUnit.Micrograms -> R.string.microgram
    WeightUnit.Pound -> R.string.pound_weight
    WeightUnit.Ounce -> R.string.ounce_weight
}

/**
 * Unit symbol / abbreviation
 */
@StringRes
internal fun weightUnitToShortRId(
    unitType: WeightUnit,
): Int = when (unitType) {
    WeightUnit.Kilogram -> R.string.kilogram_label
    WeightUnit.Gram -> R.string.gram_label
    WeightUnit.Milligram -> R.string.milligram_label
    WeightUnit.Micrograms -> R.string.microgram_label
    WeightUnit.Pound -> R.string.pound_weight_label
    WeightUnit.Ounce -> R.string.ounce_weight_label
}

/**
 * Unit full name
 */
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

/**
 * Unit symbol / abbreviation
 */
@StringRes
internal fun lengthUnitToShortRId(
    unitType: LengthUnit,
): Int = when (unitType) {
    LengthUnit.Kilometer -> R.string.kilometer_label
    LengthUnit.Meter -> R.string.meter_label
    LengthUnit.Centimeter -> R.string.centimeter_label
    LengthUnit.Millimeter -> R.string.millimeter_label
    LengthUnit.Mile -> R.string.mile_label
    LengthUnit.Feet -> R.string.feet_label
    LengthUnit.Inch -> R.string.inch_label
}

/**
 * Unit full name
 */
@StringRes
internal fun volumeUnitToFullRId(
    unitType: VolumeUnit,
): Int = when (unitType) {
    VolumeUnit.Milliliter -> R.string.milliliter
    VolumeUnit.Liter -> R.string.liter
    VolumeUnit.UsGallon -> R.string.us_gallon
    VolumeUnit.UsTablespoon -> R.string.us_tablespoon
    VolumeUnit.UsTeaspoon -> R.string.us_teaspoon
}

/**
 * Unit symbol / abbreviation
 */
@StringRes
internal fun volumeUnitToShortRId(
    unitType: VolumeUnit,
): Int = when (unitType) {
    VolumeUnit.Milliliter -> R.string.milliliter_label
    VolumeUnit.Liter -> R.string.liter_label
    VolumeUnit.UsGallon -> R.string.us_gallon_label
    VolumeUnit.UsTablespoon -> R.string.us_tablespoon_label
    VolumeUnit.UsTeaspoon -> R.string.us_teaspoon_label
}
