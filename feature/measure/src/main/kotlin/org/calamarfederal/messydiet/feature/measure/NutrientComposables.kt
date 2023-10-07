package org.calamarfederal.messydiet.feature.measure

import android.icu.number.LocalizedNumberFormatter
import android.icu.number.NumberFormatter
import android.icu.number.Precision
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.john.tuesday.nutrition.*
import org.calamarfederal.physical.measurement.*

val simpleFormatter: LocalizedNumberFormatter
    @Composable get() = NumberFormatter.withLocale(LocalConfiguration.current.locales[0]).precision(Precision.integer())

/**
 * Exception when [Portion.mass] and [Portion.volume] are both null
 */
class NoServingSizeSpecified : Throwable("Expected portion.weight or portion.volume to not be null")

/**
 * Specifies all [TextStyle] in [NutritionInfoColumn]
 */
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

/**
 * Combine the current [LocalTextStyle] with [MaterialTheme] to form the default [NutrientInfoTextStyle]
 */
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

/**
 * Read only column of all nutritional info in [nutrition].
 *
 * when [hidePortion] is `true`, [nutrition] should have a [Portion] with either
 * [Portion.mass] or [Portion.volume] (exclusive).
 *
 * @throws NoServingSizeSpecified when [nutrition] does not provide a [Portion] with either a valid weight or volume
 */
@Composable
fun NutritionInfoColumn(
    nutrition: FoodNutrition,
    modifier: Modifier = Modifier,
    textStyles: NutrientInfoTextStyle = NutrientInfoTextStyle.default(),
    hidePortion: Boolean = false,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        if (!hidePortion)
            item(key = R.string.serving_size) {
                NutritionRow(
                    label = stringResource(id = R.string.serving_size),
                    amount = nutrition.portion.mass?.inGrams() ?: nutrition.portion.volume?.inMilliliters()
                    ?: throw (NoServingSizeSpecified()),
                    unitLabel = if (nutrition.portion.volume != null)
                        stringResource(id = R.string.milliliter_label)
                    else
                        stringResource(id = R.string.gram_label),
                    labelStyle = textStyles.servingSizeLabelStyle,
                    amountStyle = textStyles.servingSizeValueStyle,
                    unitStyle = textStyles.servingSizeValueStyle,
                )
            }


        item(key = R.string.calories) {
            NutritionRow(
                label = stringResource(id = R.string.calories),
                amount = nutrition.foodEnergy.inKilocalories(),
                unitLabel = "",//stringResource(id = R.string.kilocalories_label),
                labelStyle = textStyles.caloriesLabelStyle,
                amountStyle = textStyles.caloriesValueStyle,
                unitStyle = textStyles.caloriesValueStyle,
            )
        }


        item(key = R.string.fat) {
            NutritionRow(
                label = stringResource(id = R.string.fat),
                amount = nutrition.totalFat?.inGrams() ?: 0.00,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.macroNutrientLabelStyle,
                amountStyle = textStyles.macroNutrientValueStyle,
                unitStyle = textStyles.macroNutrientValueStyle,
            )
        }

        childNutritionRow(
            nutrition.polyunsaturatedFat?.inGrams(),
            key = R.string.polyunsaturated_fat,
        ) {
            NutritionRow(
                label = stringResource(id = R.string.polyunsaturated_fat),
                amount = it,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.childNutrientLabelStyle,
                amountStyle = textStyles.childNutrientValueStyle,
                unitStyle = textStyles.childNutrientValueStyle,
            )
        }
        childNutritionRow(
            nutrition.monounsaturatedFat?.inGrams(),
            key = R.string.monounsaturated_fat,
        ) {
            NutritionRow(
                label = stringResource(id = R.string.monounsaturated_fat),
                amount = it,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.childNutrientLabelStyle,
                amountStyle = textStyles.childNutrientValueStyle,
                unitStyle = textStyles.childNutrientValueStyle,
            )
        }
        childNutritionRow(
            nutrition.saturatedFat?.inGrams(),
            key = R.string.saturated_fat,
        ) {
            NutritionRow(
                label = stringResource(id = R.string.saturated_fat),
                amount = it,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.childNutrientLabelStyle,
                amountStyle = textStyles.childNutrientValueStyle,
                unitStyle = textStyles.childNutrientValueStyle,
            )
        }
        childNutritionRow(
            nutrition.transFat?.inGrams(),
            key = R.string.trans_fat,
        ) {
            NutritionRow(
                label = stringResource(id = R.string.trans_fat),
                amount = it,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.childNutrientLabelStyle,
                amountStyle = textStyles.childNutrientValueStyle,
                unitStyle = textStyles.childNutrientValueStyle,
            )
        }
        childNutritionRow(
            nutrition.omega3?.inGrams(),
            key = R.string.omega3_fat,
        ) {
            NutritionRow(
                label = stringResource(id = R.string.omega3_fat),
                amount = it,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.childNutrientLabelStyle,
                amountStyle = textStyles.childNutrientValueStyle,
                unitStyle = textStyles.childNutrientValueStyle,
            )
        }
        childNutritionRow(
            nutrition.omega6?.inGrams(),
            key = R.string.omega6_fat,
        ) {
            NutritionRow(
                label = stringResource(id = R.string.omega6_fat),
                amount = it,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.childNutrientLabelStyle,
                amountStyle = textStyles.childNutrientValueStyle,
                unitStyle = textStyles.childNutrientValueStyle,
            )
        }

        nutrition.cholesterol?.let {
            item(key = R.string.cholesterol) {
                NutritionRow(
                    label = stringResource(id = R.string.cholesterol),
                    amount = it.inMilligrams(),
                    unitLabel = stringResource(id = R.string.milligram_label),
                    labelStyle = textStyles.macroNutrientLabelStyle,
                    amountStyle = textStyles.macroNutrientValueStyle,
                    unitStyle = textStyles.macroNutrientValueStyle,
                )
            }
        }

        item(key = R.string.carbohydrates) {
            NutritionRow(
                label = stringResource(id = R.string.carbohydrates),
                amount = nutrition.totalCarbohydrate?.inGrams() ?: 0.00,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.macroNutrientLabelStyle,
                amountStyle = textStyles.macroNutrientValueStyle,
                unitStyle = textStyles.macroNutrientValueStyle,
            )
        }

        childNutritionRow(
            nutrition.fiber?.inGrams(),
            key = R.string.fiber,
        ) {
            NutritionRow(
                label = stringResource(id = R.string.fiber),
                amount = it,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.childNutrientLabelStyle,
                amountStyle = textStyles.childNutrientValueStyle,
                unitStyle = textStyles.childNutrientValueStyle,
            )
        }
        childNutritionRow(
            nutrition.starch?.inGrams(),
            key = R.string.starch,
        ) {
            NutritionRow(
                label = stringResource(id = R.string.starch),
                amount = it,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.childNutrientLabelStyle,
                amountStyle = textStyles.childNutrientValueStyle,
                unitStyle = textStyles.childNutrientValueStyle,
            )
        }
        childNutritionRow(
            nutrition.sugar?.inGrams(),
            key = R.string.sugar,
        ) {
            NutritionRow(
                label = stringResource(id = R.string.sugar),
                amount = it,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.childNutrientLabelStyle,
                amountStyle = textStyles.childNutrientValueStyle,
                unitStyle = textStyles.childNutrientValueStyle,
            )
        }
        childNutritionRow(
            nutrition.sugarAlcohol?.inGrams(),
            key = R.string.sugar_alcohol,
        ) {
            NutritionRow(
                label = stringResource(id = R.string.sugar_alcohol),
                amount = it,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.childNutrientLabelStyle,
                amountStyle = textStyles.childNutrientValueStyle,
                unitStyle = textStyles.childNutrientValueStyle,
            )
        }

        item(key = R.string.protein) {
            NutritionRow(
                label = stringResource(id = R.string.protein),
                amount = nutrition.protein?.inGrams() ?: 0.00,
                unitLabel = stringResource(id = R.string.gram_label),
                labelStyle = textStyles.macroNutrientLabelStyle,
                amountStyle = textStyles.macroNutrientValueStyle,
                unitStyle = textStyles.macroNutrientValueStyle,
            )
        }


        nutrition.sodium?.let {
            item(key = R.string.sodium) {
                NutritionRow(
                    label = stringResource(id = R.string.sodium),
                    amount = it.inMilligrams(),
                    unitLabel = stringResource(id = R.string.milligram_label),
                    labelStyle = textStyles.macroNutrientLabelStyle,
                    amountStyle = textStyles.macroNutrientValueStyle,
                    unitStyle = textStyles.macroNutrientValueStyle,
                )
            }
        }

        nutrition.calcium?.let {
            item(R.string.calcium) {
                NutritionRow(
                    label = stringResource(id = R.string.calcium),
                    amount = it.inMilligrams(),
                    unitLabel = stringResource(id = R.string.milligram_label),
                    labelStyle = textStyles.mineralLabelStyle,
                    amountStyle = textStyles.mineralValueStyle,
                    unitStyle = textStyles.mineralValueStyle,
                )
            }
        }
        nutrition.chloride?.let {
            item(R.string.chloride) {
                NutritionRow(
                    label = stringResource(id = R.string.chloride),
                    amount = it.inMilligrams(),
                    unitLabel = stringResource(id = R.string.milligram_label),
                    labelStyle = textStyles.mineralLabelStyle,
                    amountStyle = textStyles.mineralValueStyle,
                    unitStyle = textStyles.mineralValueStyle,
                )
            }
        }
        nutrition.iron?.let {
            item(R.string.iron) {
                NutritionRow(
                    label = stringResource(id = R.string.iron),
                    amount = it.inMilligrams(),
                    unitLabel = stringResource(id = R.string.milligram_label),
                    labelStyle = textStyles.mineralLabelStyle,
                    amountStyle = textStyles.mineralValueStyle,
                    unitStyle = textStyles.mineralValueStyle,
                )
            }
        }
        nutrition.magnesium?.let {
            item(R.string.magnesium) {
                NutritionRow(
                    label = stringResource(id = R.string.magnesium),
                    amount = it.inMilligrams(),
                    unitLabel = stringResource(id = R.string.milligram_label),
                    labelStyle = textStyles.mineralLabelStyle,
                    amountStyle = textStyles.mineralValueStyle,
                    unitStyle = textStyles.mineralValueStyle,
                )
            }
        }
        nutrition.phosphorous?.let {
            item(key = R.string.phosphorous) {
                NutritionRow(
                    label = stringResource(id = R.string.phosphorous),
                    amount = it.inMilligrams(),
                    unitLabel = stringResource(id = R.string.milligram_label),
                    labelStyle = textStyles.mineralLabelStyle,
                    amountStyle = textStyles.mineralValueStyle,
                    unitStyle = textStyles.mineralValueStyle,
                )
            }
        }
        nutrition.potassium?.let {
            item(key = R.string.potassium) {
                NutritionRow(
                    label = stringResource(id = R.string.potassium),
                    amount = it.inMilligrams(),
                    unitLabel = stringResource(id = R.string.milligram_label),
                    labelStyle = textStyles.mineralLabelStyle,
                    amountStyle = textStyles.mineralValueStyle,
                    unitStyle = textStyles.mineralValueStyle,
                )
            }
        }
        nutrition.vitaminA?.let {
            item(key = R.string.vitamin_a) {
                NutritionRow(
                    label = stringResource(id = R.string.vitamin_a),
                    amount = it.inMilligrams(),
                    unitLabel = stringResource(id = R.string.milligram_label),
                    labelStyle = textStyles.mineralLabelStyle,
                    amountStyle = textStyles.mineralValueStyle,
                    unitStyle = textStyles.mineralValueStyle,
                )
            }
        }
        nutrition.vitaminC?.let {
            item(key = R.string.vitamin_c) {
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

}

private fun LazyListScope.childNutritionRow(
    amount: Double?,
    key: Any? = null,
    indentStartWidth: Dp = 8.dp,
    indentEndWidth: Dp = 8.dp,
    content: @Composable (Double) -> Unit,
) {
    if (amount != null) {
        item(key = key) {
            Box(
                propagateMinConstraints = true,
                modifier = Modifier.padding(start = indentStartWidth, end = indentEndWidth)
            ) {
                content(amount)
            }
        }
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
        modifier = modifier.semantics(true) {},
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
            val allFilledNutrition = FoodNutrition(
                foodEnergy = 15.kilocalories,
                portion = Portion(16.grams),
                nutritionMap = mapOf(
                    NutrientType.TotalFat to 1.grams,
                    NutrientType.MonounsaturatedFat to 2.grams,
                    NutrientType.PolyunsaturatedFat to 3.grams,
                    NutrientType.TransFat to 4.grams,
                    NutrientType.SaturatedFat to 5.grams,
                    NutrientType.Omega3 to 6.grams,
                    NutrientType.Omega6 to 7.grams,
                    NutrientType.TotalCarbohydrate to 8.grams,
                    NutrientType.Sugar to 9.grams,
                    NutrientType.Fiber to 10.grams,
                    NutrientType.SugarAlcohol to 11.grams,
                    NutrientType.Starch to 12.grams,
                    NutrientType.Protein to 13.grams,
                    NutrientType.Cholesterol to 14.grams,
                    NutrientType.VitaminC to 17.grams,
                    NutrientType.Magnesium to 18.grams,
                    NutrientType.Iron to 19.grams,
                    NutrientType.Calcium to 20.grams,
                    NutrientType.Chloride to 21.grams,
                    NutrientType.Phosphorous to 22.grams,
                    NutrientType.Potassium to 23.grams,
                    NutrientType.Sodium to 24.grams,
                ),
            )
            NutritionInfoColumn(
                nutrition = allFilledNutrition,
            )
        }
    }
}
