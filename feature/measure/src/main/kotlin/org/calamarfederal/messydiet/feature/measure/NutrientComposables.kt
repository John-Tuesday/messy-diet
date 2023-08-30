package org.calamarfederal.messydiet.feature.measure

import android.icu.number.LocalizedNumberFormatter
import android.icu.number.NumberFormatter
import android.icu.number.Precision
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontStyle.Companion
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.diet_data.model.NutritionInfo
import org.calamarfederal.messydiet.diet_data.model.inKilocalories
import org.calamarfederal.messydiet.diet_data.model.kcal
import org.calamarfederal.messydiet.measure.R
import org.calamarfederal.messydiet.measure.grams

val simpleFormatter: LocalizedNumberFormatter
    @Composable get() = NumberFormatter.withLocale(LocalConfiguration.current.locales[0]).precision(Precision.integer())

data class NutrientInfoTextStyle(
    val servingSizeLabelStyle: TextStyle,
    val servingSizeValueStyle: TextStyle,
    val caloriesLabelStyle: TextStyle,
    val caloriesValueStyle: TextStyle,
    val macroNutrientLabelStyle: TextStyle,
    val macroNutrientValueStyle: TextStyle,
    val childNutrientLabelStyle: TextStyle,
    val childNutrientValueStyle: TextStyle,
    val mineralLabelStyle: TextStyle,
    val mineralValueStyle: TextStyle,
) {
    companion object
}

private val NutritionInfo.hasMinerals: Boolean get() = sequenceOf(
    calcium,
    iron,
    magnesium,
    potassium,
    phosphorous,
    ).any { it != null }
private val NutritionInfo.hasVitamins: Boolean
    get() = sequenceOf(
        vitaminC,
    ).any { it != null }

@Composable
fun NutrientInfoTextStyle.Companion.default(
    servingSizeLabelStyle: TextStyle = LocalTextStyle.current + MaterialTheme.typography.titleLarge,
    servingSizeValueStyle: TextStyle = LocalTextStyle.current + MaterialTheme.typography.titleLarge,
    caloriesLabelStyle: TextStyle = LocalTextStyle.current + MaterialTheme.typography.titleMedium.copy(),
    caloriesValueStyle: TextStyle = LocalTextStyle.current + MaterialTheme.typography.titleLarge,
    macroNutrientLabelStyle: TextStyle = LocalTextStyle.current + MaterialTheme.typography.titleMedium.copy(),
    macroNutrientValueStyle: TextStyle = macroNutrientLabelStyle.copy(),
    childNutrientLabelStyle: TextStyle = LocalTextStyle.current + MaterialTheme.typography.bodyMedium.copy(),
    childNutrientValueStyle: TextStyle = childNutrientLabelStyle.copy(),
    mineralLabelStyle: TextStyle = LocalTextStyle.current + MaterialTheme.typography.bodySmall,
    mineralValueStyle: TextStyle = mineralLabelStyle.copy(),
): NutrientInfoTextStyle = NutrientInfoTextStyle(
    servingSizeLabelStyle = servingSizeLabelStyle,
    servingSizeValueStyle = servingSizeValueStyle,
    caloriesLabelStyle = caloriesLabelStyle,
    caloriesValueStyle = caloriesValueStyle,
    macroNutrientLabelStyle = macroNutrientLabelStyle,
    macroNutrientValueStyle = macroNutrientValueStyle,
    childNutrientLabelStyle = childNutrientLabelStyle,
    childNutrientValueStyle = childNutrientValueStyle,
    mineralLabelStyle = mineralLabelStyle,
    mineralValueStyle = mineralValueStyle,
)

@Composable
fun NutritionInfoColumn(
    nutrition: NutritionInfo,
    modifier: Modifier = Modifier,
    textStyles: NutrientInfoTextStyle = NutrientInfoTextStyle.default(),
) {
    Column(
        modifier = modifier,
    ) {
        NutritionRow(
            label = stringResource(id = R.string.serving_size),
            amount = nutrition.portion.inGrams(),
            unitLabel = stringResource(id = R.string.gram_label),
            labelStyle = textStyles.servingSizeLabelStyle,
            amountStyle = textStyles.servingSizeValueStyle,
            unitStyle = textStyles.servingSizeValueStyle,
        )

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 4.dp),
            thickness = 2.dp,
        )

        NutritionRow(
            label = stringResource(id = R.string.calories),
            amount = nutrition.foodEnergy.inKilocalories(),
            unitLabel = "",//stringResource(id = R.string.kilocalories_label),
            labelStyle = textStyles.caloriesLabelStyle,
            amountStyle = textStyles.caloriesValueStyle,
            unitStyle = textStyles.caloriesValueStyle,
        )

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 4.dp),
            thickness = 2.dp,
        )


        NutritionRow(
            label = stringResource(id = R.string.fat),
            amount = nutrition.totalFat.inGrams(),
            unitLabel = stringResource(id = R.string.gram_label),
            labelStyle = textStyles.macroNutrientLabelStyle,
            amountStyle = textStyles.macroNutrientValueStyle,
            unitStyle = textStyles.macroNutrientValueStyle,
        )
        NutrientGroup {
            nutrition.polyunsaturatedFat?.let {
                NutritionRow(
                    label = stringResource(id = R.string.polyunsaturated_fat),
                    amount = it.inGrams(),
                    unitLabel = stringResource(id = R.string.gram_label),
                    labelStyle = textStyles.childNutrientLabelStyle,
                    amountStyle = textStyles.childNutrientValueStyle,
                    unitStyle = textStyles.childNutrientValueStyle,
                )
            }
            nutrition.monounsaturatedFat?.let {
                NutritionRow(
                    label = stringResource(id = R.string.monounsaturated_fat),
                    amount = it.inGrams(),
                    unitLabel = stringResource(id = R.string.gram_label),
                    labelStyle = textStyles.childNutrientLabelStyle,
                    amountStyle = textStyles.childNutrientValueStyle,
                    unitStyle = textStyles.childNutrientValueStyle,
                )
            }
            nutrition.saturatedFat?.let {
                NutritionRow(
                    label = stringResource(id = R.string.saturated_fat),
                    amount = it.inGrams(),
                    unitLabel = stringResource(id = R.string.gram_label),
                    labelStyle = textStyles.childNutrientLabelStyle,
                    amountStyle = textStyles.childNutrientValueStyle,
                    unitStyle = textStyles.childNutrientValueStyle,
                )
            }
            nutrition.transFat?.let {
                NutritionRow(
                    label = stringResource(id = R.string.trans_fat),
                    amount = it.inGrams(),
                    unitLabel = stringResource(id = R.string.gram_label),
                    labelStyle = textStyles.childNutrientLabelStyle,
                    amountStyle = textStyles.childNutrientValueStyle,
                    unitStyle = textStyles.childNutrientValueStyle,
                )
            }
            nutrition.omega3?.let {
                NutritionRow(
                    label = stringResource(id = R.string.omega3_fat),
                    amount = it.inGrams(),
                    unitLabel = stringResource(id = R.string.gram_label),
                    labelStyle = textStyles.childNutrientLabelStyle,
                    amountStyle = textStyles.childNutrientValueStyle,
                    unitStyle = textStyles.childNutrientValueStyle,
                )
            }
            nutrition.omega6?.let {
                NutritionRow(
                    label = stringResource(id = R.string.omega6_fat),
                    amount = it.inGrams(),
                    unitLabel = stringResource(id = R.string.gram_label),
                    labelStyle = textStyles.childNutrientLabelStyle,
                    amountStyle = textStyles.childNutrientValueStyle,
                    unitStyle = textStyles.childNutrientValueStyle,
                )
            }
        }

        nutrition.sodium?.let {
            NutritionRow(
                label = stringResource(id = R.string.sodium),
                amount = it.inMilligrams(),
                unitLabel = stringResource(id = R.string.milligram_label),
                labelStyle = textStyles.macroNutrientLabelStyle,
                amountStyle = textStyles.macroNutrientValueStyle,
                unitStyle = textStyles.macroNutrientValueStyle,
            )
        }

        nutrition.cholesterol?.let {
            NutritionRow(
                label = stringResource(id = R.string.cholesterol),
                amount = it.inMilligrams(),
                unitLabel = stringResource(id = R.string.milligram_label),
                labelStyle = textStyles.macroNutrientLabelStyle,
                amountStyle = textStyles.macroNutrientValueStyle,
                unitStyle = textStyles.macroNutrientValueStyle,
            )
        }

        NutritionRow(
            label = stringResource(id = R.string.carbohydrates),
            amount = nutrition.totalCarbohydrates.inGrams(),
            unitLabel = stringResource(id = R.string.gram_label),
            labelStyle = textStyles.macroNutrientLabelStyle,
            amountStyle = textStyles.macroNutrientValueStyle,
            unitStyle = textStyles.macroNutrientValueStyle,
        )
        NutrientGroup {
            nutrition.fiber?.let {
                NutritionRow(
                    label = stringResource(id = R.string.fiber),
                    amount = it.inGrams(),
                    unitLabel = stringResource(id = R.string.gram_label),
                    labelStyle = textStyles.childNutrientLabelStyle,
                    amountStyle = textStyles.childNutrientValueStyle,
                    unitStyle = textStyles.childNutrientValueStyle,
                )
            }
            nutrition.starch?.let {
                NutritionRow(
                    label = stringResource(id = R.string.starch),
                    amount = it.inGrams(),
                    unitLabel = stringResource(id = R.string.gram_label),
                    labelStyle = textStyles.childNutrientLabelStyle,
                    amountStyle = textStyles.childNutrientValueStyle,
                    unitStyle = textStyles.childNutrientValueStyle,
                )
            }
            nutrition.sugar?.let {
                NutritionRow(
                    label = stringResource(id = R.string.sugar),
                    amount = it.inGrams(),
                    unitLabel = stringResource(id = R.string.gram_label),
                    labelStyle = textStyles.childNutrientLabelStyle,
                    amountStyle = textStyles.childNutrientValueStyle,
                    unitStyle = textStyles.childNutrientValueStyle,
                )
            }
            nutrition.sugarAlcohol?.let {
                NutritionRow(
                    label = stringResource(id = R.string.sugar_alcohol),
                    amount = it.inGrams(),
                    unitLabel = stringResource(id = R.string.gram_label),
                    labelStyle = textStyles.childNutrientLabelStyle,
                    amountStyle = textStyles.childNutrientValueStyle,
                    unitStyle = textStyles.childNutrientValueStyle,
                )
            }
        }
        NutritionRow(
            label = stringResource(id = R.string.protein),
            amount = nutrition.totalProtein.inGrams(),
            unitLabel = stringResource(id = R.string.gram_label),
            labelStyle = textStyles.macroNutrientLabelStyle,
            amountStyle = textStyles.macroNutrientValueStyle,
            unitStyle = textStyles.macroNutrientValueStyle,
        )

        if (nutrition.hasMinerals || nutrition.hasVitamins) {
            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }

        nutrition.calcium?.let {
            NutritionRow(
                label = stringResource(id = R.string.calcium),
                amount = it.inMilligrams(),
                unitLabel = stringResource(id = R.string.milligram_label),
                labelStyle = textStyles.mineralLabelStyle,
                amountStyle = textStyles.mineralValueStyle,
                unitStyle = textStyles.mineralValueStyle,
            )
        }
        nutrition.iron?.let {
            NutritionRow(
                label = stringResource(id = R.string.iron),
                amount = it.inMilligrams(),
                unitLabel = stringResource(id = R.string.milligram_label),
                labelStyle = textStyles.mineralLabelStyle,
                amountStyle = textStyles.mineralValueStyle,
                unitStyle = textStyles.mineralValueStyle,
            )
        }
        nutrition.magnesium?.let {
            NutritionRow(
                label = stringResource(id = R.string.magnesium),
                amount = it.inMilligrams(),
                unitLabel = stringResource(id = R.string.milligram_label),
                labelStyle = textStyles.mineralLabelStyle,
                amountStyle = textStyles.mineralValueStyle,
                unitStyle = textStyles.mineralValueStyle,
            )
        }

        nutrition.potassium?.let {
            NutritionRow(
                label = stringResource(id = R.string.potassium),
                amount = it.inMilligrams(),
                unitLabel = stringResource(id = R.string.milligram_label),
                labelStyle = textStyles.mineralLabelStyle,
                amountStyle = textStyles.mineralValueStyle,
                unitStyle = textStyles.mineralValueStyle,
            )
        }
        nutrition.phosphorous?.let {
            NutritionRow(
                label = stringResource(id = R.string.phosphorus),
                amount = it.inMilligrams(),
                unitLabel = stringResource(id = R.string.milligram_label),
                labelStyle = textStyles.mineralLabelStyle,
                amountStyle = textStyles.mineralValueStyle,
                unitStyle = textStyles.mineralValueStyle,
            )
        }
        nutrition.vitaminC?.let {
            NutritionRow(
                label = stringResource(id = R.string.vitamin_c),
                amount = it.inMilligrams(),
                unitLabel = stringResource(id = R.string.milligram_label),
                labelStyle = textStyles.mineralLabelStyle,
                amountStyle = textStyles.mineralValueStyle,
                unitStyle = textStyles.mineralValueStyle,
            )
        }
    }

}

@Composable
private fun NutrientGroup(
    modifier: Modifier = Modifier,
    indentStartWidth: Dp = 8.dp,
    indentEndWidth: Dp = 8.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Row(modifier = modifier) {
        Column(
            modifier = Modifier.padding(start = indentStartWidth, end = indentEndWidth),
            content = content,
        )
    }
}

@Composable
private fun NutritionRow(
    label: String,
    amount: Double,
    unitLabel: String,
    modifier: Modifier = Modifier,
    amountFormatter: LocalizedNumberFormatter = simpleFormatter,
    labelStyle: TextStyle = LocalTextStyle.current + MaterialTheme.typography.bodyMedium,
    amountStyle: TextStyle = LocalTextStyle.current + MaterialTheme.typography.bodyMedium,
    unitStyle: TextStyle = LocalTextStyle.current + MaterialTheme.typography.bodyMedium,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            text = label,
            style = labelStyle,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.width(IntrinsicSize.Max)
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
                .widthIn(min = 8.dp)
        )
        Text(
            text = amountFormatter.format(amount).toString(),
            style = amountStyle,
        )
        Spacer(modifier = Modifier.width(1.dp))
        Text(
            text = unitLabel,
            style = unitStyle,
        )
    }

}

@Preview
@Composable
private fun NutritionInfoPreview() {
    MaterialTheme {
        Surface {
            val allFilledNutrition = Nutrition(
                totalFat = 1.grams,
                monounsaturatedFat = 2.grams,
                polyunsaturatedFat = 3.grams,
                transFat = 4.grams,
                saturatedFat = 5.grams,
                omega3 = 6.grams,
                omega6 = 7.grams,
                totalCarbohydrates = 8.grams,
                sugar = 9.grams,
                fiber = 10.grams,
                sugarAlcohol = 11.grams,
                starch = 12.grams,
                totalProtein = 13.grams,
                cholesterol = 14.grams,
                foodEnergy = 15.kcal,
                portion = 16.grams,
                vitaminC = 17.grams,
                magnesium = 18.grams,
                iron = 19.grams,
                calcium = 20.grams,
                chloride = 21.grams,
                phosphorous = 22.grams,
                potassium = 23.grams,
                sodium = 24.grams,
            )
            NutritionInfoColumn(
                nutrition = allFilledNutrition,
                modifier = Modifier.width(IntrinsicSize.Min)
            )
        }
    }
}
