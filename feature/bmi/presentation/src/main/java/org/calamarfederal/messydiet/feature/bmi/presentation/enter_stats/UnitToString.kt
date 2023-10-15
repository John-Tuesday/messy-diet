package org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats

import android.content.res.Resources
import android.icu.number.IntegerWidth
import android.icu.number.Notation
import android.icu.number.NumberFormatter
import android.icu.number.NumberFormatter.DecimalSeparatorDisplay
import android.icu.number.Precision
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.calamarfederal.feature.bmi.data.model.Bmi
import org.calamarfederal.feature.bmi.data.model.BmiCategory
import org.calamarfederal.messydiet.feature.bmi.presentation.R
import org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats.HeightInputType.FeetAndInches
import org.calamarfederal.messydiet.measure.lengthUnitFullString


internal fun heightInputTypeToOptionString(
    type: HeightInputType,
    resource: Resources,
) = if (type == FeetAndInches)
    resource.getString(R.string.feet_inch_height)
else
    lengthUnitFullString(type.lengthUnit, resource)

internal val HeightInputType.optionString: String
    @Composable
    get() = heightInputTypeToOptionString(this, LocalContext.current.resources)

internal val Bmi.indexString: String
    @Composable
    get() = bmiIndexToString(value, LocalConfiguration.current.locales[0])

internal fun bmiIndexToString(bmi: Double, locale: java.util.Locale): String {
    return NumberFormatter
        .withLocale(locale)
        .precision(Precision.maxFraction(1))
        .integerWidth(IntegerWidth.zeroFillTo(1))
        .notation(Notation.simple())
        .decimal(DecimalSeparatorDisplay.AUTO)
        .format(bmi)
        .toString()
}

internal val BmiCategory.text: String
    @Composable
    get() = stringResource(bmiCategoryRId(this))

internal fun bmiCategoryRId(category: BmiCategory) = when (category) {
    BmiCategory.VerySeverelyObese -> R.string.bmi_very_severely_obese
    BmiCategory.SeverelyObese -> R.string.bmi_severely_obese
    BmiCategory.ModeratelyObese -> R.string.bmi_moderately_obese
    BmiCategory.Overweight -> R.string.bmi_overweight
    BmiCategory.Normal -> R.string.bmi_normal
    BmiCategory.Underweight -> R.string.bmi_underweight
    BmiCategory.SeverelyUnderweight -> R.string.bmi_severely_underweight
    BmiCategory.VerySeverelyUnderweight -> R.string.bmi_very_severely_underweight
}
