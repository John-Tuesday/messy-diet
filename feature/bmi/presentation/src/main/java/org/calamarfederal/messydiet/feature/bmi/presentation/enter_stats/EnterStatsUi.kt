/******************************************************************************
 * Copyright (c) 2023 John Tuesday Picot                                      *
 *                                                                            *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  *
 * copies of the Software, and to permit persons to whom the Software is      *
 * furnished to do so, subject to the following conditions:                   *
 *                                                                            *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.                            *
 *                                                                            *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,   *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE*
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER     *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.                                                                  *
 ******************************************************************************/

package org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.calamarfederal.feature.bmi.data.model.Bmi
import org.calamarfederal.feature.bmi.data.model.BmiCategory
import org.calamarfederal.messydiet.feature.bmi.presentation.R
import org.calamarfederal.messydiet.measure.MeasuredUnitField
import org.calamarfederal.messydiet.measure.fullString
import org.calamarfederal.messydiet.measure.labelString
import org.calamarfederal.physical.measurement.MassUnit
import java.util.Locale

@Composable
fun EnterStatsScreen(
    onNavigateUp: () -> Unit,
    viewModel: EnterStatsViewModel,
) {
    val bmi by viewModel.bmiState.collectAsStateWithLifecycle()
    val inputState by viewModel.heightAndWeightInputState.collectAsStateWithLifecycle()

    val currentLocale = LocalConfiguration.current.locales[0]
    LaunchedEffect(currentLocale) {
        when (currentLocale) {
            Locale.US -> {
                inputState.heightInputType = HeightInputType.FeetAndInches
                inputState.weightInputUnit = MassUnit.Pound
            }

            Locale.CANADA -> {
                inputState.heightInputType = HeightInputType.FeetAndInches
                inputState.weightInputUnit = MassUnit.Pound
            }

            else -> {
                inputState.heightInputType = HeightInputType.OnlyMeters
                inputState.weightInputUnit = MassUnit.Kilogram
            }
        }
    }

    EnterStatsLayout(
        bmiState = bmi,
        inputState = inputState,
        onNavigateUp = onNavigateUp,
    )
}


@Composable
private fun EnterStatsLayout(
    inputState: HeightAndWeightInputState,
    bmiState: Bmi,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { EnterStatsTopBar(onNavigateUp) },
        modifier = modifier,
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                BmiDisplay(
                    bmi = bmiState,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                HeightAndWeightInputFields(
                    state = inputState,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EnterStatsTopBar(
    onNavigateUp: () -> Unit,
    showNavigationIcon: Boolean = true,
) = TopAppBar(
    title = { /*TODO*/ },
    navigationIcon = {
        if (showNavigationIcon) {
            IconButton(onClick = onNavigateUp) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }
    }
)

@Composable
private fun BmiDisplay(
    bmi: Bmi,
    modifier: Modifier = Modifier,
    contentColor: Color = when (bmi.category) {
        BmiCategory.VerySeverelyUnderweight, BmiCategory.SeverelyUnderweight -> MaterialTheme.colorScheme.tertiary
        BmiCategory.Underweight -> MaterialTheme.colorScheme.secondary
        BmiCategory.Normal -> MaterialTheme.colorScheme.primary
        BmiCategory.Overweight -> MaterialTheme.colorScheme.secondary
        BmiCategory.ModeratelyObese, BmiCategory.SeverelyObese, BmiCategory.VerySeverelyObese -> MaterialTheme.colorScheme.tertiary
    },
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.bmi),
                style = MaterialTheme.typography.titleLarge
            )
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                Text(
                    text = bmi.indexString,
                    style = MaterialTheme.typography.displayMedium,
                )
                Text(
                    text = bmi.category.text,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}

@Composable
private fun HeightAndWeightInputFields(
    state: HeightAndWeightInputState,
    modifier: Modifier = Modifier,
) {
    val heightOptions: List<String> = HeightInputType.entries.map { it.optionString }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TwoUnitHeightField(
            inputPrimary = state.heightInput,
            inputSecondary = state.heightInputOptional,
            heightInputType = state.heightInputType,
            possibleUnits = heightOptions,
            onInputPrimaryChange = { state.heightInput = it },
            onInputSecondaryChange = { state.heightInputOptional = it },
            onUnitChanged = { state.heightInputType = HeightInputType.entries[it] },
        )
        SingleUnitField(
            labelText = stringResource(id = R.string.weight),
            input = state.weightInput,
            onInputChange = { state.weightInput = it },
            unitLabel = state.weightInputUnit.labelString,
            possibleLengthUnits = MassUnit.entries.map { it.fullString },
            onLengthUnitChange = { state.weightInputUnit = MassUnit.entries[it] },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}


@Composable
private fun TwoUnitHeightField(
    inputPrimary: String,
    inputSecondary: String,
    onInputPrimaryChange: (String) -> Unit,
    onInputSecondaryChange: (String) -> Unit,
    heightInputType: HeightInputType,
    possibleUnits: List<String>,
    onUnitChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    labelFieldPadding: Dp = 8.dp,
    inputFieldPadding: Dp = 4.dp,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.End,
    ) {
        Text(
            text = stringResource(id = R.string.height),
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(labelFieldPadding)
                .weight(1f, fill = true)
                .alignByBaseline(),
        )
        val primaryError by remember(inputPrimary) {
            derivedStateOf { inputPrimary.toDoubleOrNull() == null && inputPrimary.isNotBlank() }
        }
        StatInputField(
            value = inputPrimary,
            onValueChange = onInputPrimaryChange,
            unitLabel = heightInputType.lengthUnit.labelString,
            unitChoices = possibleUnits,
            onUnitChange = onUnitChanged,
            isError = primaryError,
            placeholder = "0",
            modifier = Modifier
                .wrapContentWidth()
                .weight(1f, fill = false)
                .alignByBaseline(),
            unitPickerModifier = Modifier
                .weight(1f, fill = false)
                .alignByBaseline(),
        )
        if (heightInputType == HeightInputType.FeetAndInches) {
            val secondaryError by remember(inputSecondary) {
                derivedStateOf { inputSecondary.toDoubleOrNull() == null && inputSecondary.isNotBlank() }
            }
            Spacer(modifier = Modifier.width(inputFieldPadding))
            StatInputField(
                value = inputSecondary,
                onValueChange = onInputSecondaryChange,
                unitLabel = heightInputType.optionalLengthUnit!!.labelString,
                unitChoices = possibleUnits,
                onUnitChange = onUnitChanged,
                isError = secondaryError,
                placeholder = "0",
                modifier = Modifier
                    .wrapContentWidth()
                    .weight(1f, fill = false)
                    .alignByBaseline(),
                unitPickerModifier = Modifier
                    .weight(1f, fill = false)
                    .alignByBaseline(),
            )
        }
    }

}

@Composable
private fun SingleUnitField(
    labelText: String,
    input: String,
    onInputChange: (String) -> Unit,
    unitLabel: String,
    possibleLengthUnits: List<String>,
    onLengthUnitChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    labelFieldPadding: Dp = 8.dp,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.End,
    ) {
        Text(
            text = labelText,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(labelFieldPadding)
                .weight(1f, fill = true)
                .alignByBaseline(),
        )
        val inputError by remember(input) {
            derivedStateOf { input.toDoubleOrNull() == null && input.isNotBlank() }
        }
        StatInputField(
            value = input,
            onValueChange = onInputChange,
            unitLabel = unitLabel,
            unitChoices = possibleLengthUnits,
            onUnitChange = onLengthUnitChange,
            isError = inputError,
            placeholder = "0",
            unitPickerModifier = Modifier
                .weight(1f, fill = false)
                .alignByBaseline(),
            modifier = Modifier
                .weight(1f, fill = false)
                .alignByBaseline(),
        )
    }
}

@Composable
private fun StatInputField(
    value: String,
    onValueChange: (String) -> Unit,
    unitLabel: String,
    unitChoices: List<String>,
    onUnitChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    unitPickerModifier: Modifier = Modifier,
    unitPickerEnabled: Boolean = true,
    placeholder: String? = null,
    isError: Boolean = false,
    includeDecimal: Boolean = true,
) {
    MeasuredUnitField(
        value = value,
        onValueChange = onValueChange,
        unitLabel = unitLabel,
        unitChoices = unitChoices,
        onUnitChange = onUnitChange,
        unitPickerEnabled = unitPickerEnabled,
        unitPickerModifier = unitPickerModifier,
        placeholder = placeholder?.let { { Text(text = it) } },
        imeAction = ImeAction.Next,
        isError = isError,
        includeDecimal = includeDecimal,
        modifier = modifier
    )
}

@Preview
@Composable
private fun EnterStatsPreview() {
    EnterStatsLayout(
        bmiState = Bmi(),
        inputState = HeightAndWeightInputStateImpl(),
        onNavigateUp = {},
    )
}
