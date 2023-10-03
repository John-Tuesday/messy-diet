package org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats

import android.icu.number.NumberFormatter
import android.icu.number.NumberFormatter.DecimalSeparatorDisplay
import android.icu.number.Precision
import androidx.compose.runtime.*
import org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats.HeightInputType.OnlyMeters
import org.calamarfederal.physical.measurement.*
import java.math.RoundingMode
import java.util.Locale

private fun formatWeight(unit: MassUnit, weight: Mass, locale: java.util.Locale): String = NumberFormatter
    .withLocale(locale)
    .precision(
        Precision.maxFraction(
            when (unit) {
                MassUnit.Microgram -> 0
                MassUnit.Milligram -> 0
                MassUnit.Gram -> 0
                MassUnit.Kilogram -> 2
                MassUnit.Pound -> 0
                MassUnit.Ounce -> 0
            }
        )
    ).decimal(DecimalSeparatorDisplay.AUTO)
    .format(weight.inUnitsOf(unit))
    .toString()

private fun formatHeight(type: HeightInputType, height: Length, locale: java.util.Locale): Pair<String, String> {
    val standardFormatter = NumberFormatter
        .withLocale(locale)
        .precision(Precision.maxFraction(2))
        .roundingMode(RoundingMode.HALF_UP)
        .decimal(DecimalSeparatorDisplay.AUTO)
    val footWithInchFormatter = NumberFormatter
        .withLocale(locale)
        .precision(Precision.integer())
        .roundingMode(RoundingMode.DOWN)
        .decimal(DecimalSeparatorDisplay.AUTO)

    return if (type == HeightInputType.FeetAndInches) {
        footWithInchFormatter.format(height.inFeet()).toString() to standardFormatter
            .precision(Precision.maxFraction(1))
            .format(
                height.inInches().rem(1.feet.inInches())
            ).toString()
    } else {
        standardFormatter.format(height.inUnitsOf(type.lengthUnit)).toString() to ""
    }
}

enum class HeightInputType(val lengthUnit: LengthUnit, val optionalLengthUnit: LengthUnit? = null) {
    OnlyMeters(LengthUnit.Meter),
    OnlyFeet(LengthUnit.Foot),
    OnlyInches(LengthUnit.Inch),
    FeetAndInches(LengthUnit.Foot, LengthUnit.Inch),
    ;
}

@Stable
interface HeightAndWeightInputState {
    var heightInput: String
    var heightInputOptional: String
    var heightInputType: HeightInputType

    var weightInput: String
    var weightInputUnit: MassUnit
}

internal class HeightAndWeightInputStateImpl : HeightAndWeightInputState {
    override var heightInput: String by mutableStateOf("")
    override var heightInputOptional: String by mutableStateOf("")

    val heightState = derivedStateOf {
        Length(
            heightInput.toDoubleOrNull() ?: 0.00,
            heightInputType.lengthUnit
        ) + (heightInputType.optionalLengthUnit?.let { Length(heightInputOptional.toDoubleOrNull() ?: 0.00, it) }
            ?: 0.meters)
    }

    fun setHeight(height: Length, locale: Locale = Locale.getDefault()) {
        val (main, secondary) = formatHeight(heightInputType, height, locale)
        heightInput = main
        heightInputOptional = secondary
    }

    private val _heightInputTypeState = mutableStateOf(OnlyMeters)
    override var heightInputType: HeightInputType
        get() = _heightInputTypeState.value
        set(value) {
            convertHeightUnits(value)
        }

    private fun convertHeightUnits(inputType: HeightInputType) {
        val height = heightState.value
        _heightInputTypeState.value = inputType
        setHeight(height)
    }

    override var weightInput: String by mutableStateOf("")
    private val _weightInputUnitState = mutableStateOf(MassUnit.Kilogram)
    override var weightInputUnit: MassUnit
        get() = _weightInputUnitState.value
        set(value) {
            convertWeightUnits(value)
        }

    private fun convertWeightUnits(weightUnit: MassUnit) {
        val weight = weightState.value
        _weightInputUnitState.value = weightUnit
        setWeight(weight)
    }

    val weightState = derivedStateOf {
        Mass(weightInput.toDoubleOrNull() ?: 0.00, weightInputUnit)
    }

    fun setWeight(weight: Mass, locale: Locale = Locale.getDefault()) {
        weightInput = formatWeight(weightInputUnit, weight, locale)
    }
}
