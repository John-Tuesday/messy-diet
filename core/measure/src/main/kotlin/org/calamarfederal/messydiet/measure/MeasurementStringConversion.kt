package org.calamarfederal.messydiet.measure

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.calamarfederal.physical.measurement.LengthUnit
import org.calamarfederal.physical.measurement.MassUnit
import org.calamarfederal.physical.measurement.VolumeUnit

/**
 * Unit symbol / abbreviation
 */
val MassUnit.labelString: String
    @Composable
    get() = stringResource(id = weightUnitToShortRId(this))

/**
 * Unit full name
 */
val MassUnit.fullString: String
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
    unitType: MassUnit,
    resources: Resources,
): String = resources.getString(weightUnitToFullRId(unitType))

/**
 * Unit symbol / abbreviation
 */
fun weightUnitShortString(
    unitType: MassUnit,
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
    unitType: MassUnit,
): Int = when (unitType) {
    MassUnit.Kilogram -> R.string.kilogram
    MassUnit.Gram -> R.string.gram
    MassUnit.Milligram -> R.string.milligram
    MassUnit.Microgram -> R.string.microgram
    MassUnit.Pound -> R.string.pound_weight
    MassUnit.Ounce -> R.string.ounce_weight
}

/**
 * Unit symbol / abbreviation
 */
@StringRes
internal fun weightUnitToShortRId(
    unitType: MassUnit,
): Int = when (unitType) {
    MassUnit.Kilogram -> R.string.kilogram_label
    MassUnit.Gram -> R.string.gram_label
    MassUnit.Milligram -> R.string.milligram_label
    MassUnit.Microgram -> R.string.microgram_label
    MassUnit.Pound -> R.string.pound_weight_label
    MassUnit.Ounce -> R.string.ounce_weight_label
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
        LengthUnit.Foot -> R.string.feet
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
    LengthUnit.Foot -> R.string.feet_label
    LengthUnit.Inch -> R.string.inch_label
}

/**
 * Unit full name
 */
@StringRes
internal fun volumeUnitToFullRId(
    unitType: VolumeUnit,
): Int = when (unitType) {
    VolumeUnit.Kiloliter -> R.string.kiloliter
    VolumeUnit.Milliliter -> R.string.milliliter
    VolumeUnit.Liter -> R.string.liter
    VolumeUnit.UsGallon -> R.string.us_gallon
    VolumeUnit.UsFluidOunce -> R.string.us_fluid_ounce
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
    VolumeUnit.Kiloliter -> R.string.kiloliter_label
    VolumeUnit.UsGallon -> R.string.us_gallon_label
    VolumeUnit.UsTablespoon -> R.string.us_tablespoon_label
    VolumeUnit.UsTeaspoon -> R.string.us_teaspoon_label
    VolumeUnit.UsFluidOunce -> R.string.us_fluid_ounce_label
}
