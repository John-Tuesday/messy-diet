package org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats

import android.icu.number.NumberFormatter
import android.icu.number.NumberFormatter.DecimalSeparatorDisplay
import android.icu.number.Precision
import androidx.compose.runtime.*
import org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats.HeightInputType.OnlyMeters
import org.calamarfederal.messydiet.measure.*
import java.math.RoundingMode
import java.util.Locale

private fun formatWeight(unit: WeightUnit, weight: Weight, locale: java.util.Locale): String = NumberFormatter
    .withLocale(locale)
    .precision(
        Precision.maxFraction(
            when (unit) {
                WeightUnit.Micrograms -> 0
                WeightUnit.Milligram -> 0
                WeightUnit.Gram -> 0
                WeightUnit.Kilogram -> 2
                WeightUnit.Pound -> 0
                WeightUnit.Ounce -> 0
            }
        )
    ).decimal(DecimalSeparatorDisplay.AUTO)
    .format(weight.inUnits(unit))
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
        standardFormatter.format(height.inUnits(type.lengthUnit)).toString() to ""
    }
}

enum class HeightInputType(val lengthUnit: LengthUnit, val optionalLengthUnit: LengthUnit? = null) {
    OnlyMeters(LengthUnit.Meter),
    OnlyFeet(LengthUnit.Feet),
    OnlyInches(LengthUnit.Inch),
    FeetAndInches(LengthUnit.Feet, LengthUnit.Inch),
    ;
}

@Stable
interface HeightAndWeightInputState {
    var heightInput: String
    var heightInputOptional: String
    var heightInputType: HeightInputType

    var weightInput: String
    var weightInputUnit: WeightUnit
}

internal class HeightAndWeightInputStateImpl : HeightAndWeightInputState {
    override var heightInput: String by mutableStateOf("")
    override var heightInputOptional: String by mutableStateOf("")

    val heightState = derivedStateOf {
        heightInputType.lengthUnit.lengthOf(
            heightInput.toDoubleOrNull() ?: 0.00
        ).plus(
            heightInputType.optionalLengthUnit?.lengthOf(
                heightInputOptional.toDoubleOrNull() ?: 0.00
            ) ?: lengthOf()
        )
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
    private val _weightInputUnitState = mutableStateOf(WeightUnit.Kilogram)
    override var weightInputUnit: WeightUnit
        get() = _weightInputUnitState.value
        set(value) {
            convertWeightUnits(value)
        }

    private fun convertWeightUnits(weightUnit: WeightUnit) {
        val weight = weightState.value
        _weightInputUnitState.value = weightUnit
        setWeight(weight)
    }

    val weightState = derivedStateOf {
        weightInputUnit.weightOf(
            weightInput.toDoubleOrNull() ?: 0.00
        )
    }

    fun setWeight(weight: Weight, locale: Locale = Locale.getDefault()) {
        weightInput = formatWeight(weightInputUnit, weight, locale)
    }
}
