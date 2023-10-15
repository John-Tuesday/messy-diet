package org.calamarfederal.messydiet.feature.meal.presentation.create

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.calamarfederal.messydiet.feature.meal.presentation.R
import org.calamarfederal.messydiet.feature.measure.*
import org.calamarfederal.messydiet.feature.measure.R as M

private fun String.filterWithDecimalFormat(decimalSeparator: Char = '.'): String {
    var hasDecimalSeparator = false
    return this.filter {
        if (it == decimalSeparator && !hasDecimalSeparator) {
            hasDecimalSeparator = true
            true
        } else it.isDigit()
    }
}

@Composable
fun CreateMealUi(
    viewModel: CreateMealViewModel,
    onNavigateUp: () -> Unit,
) {
    val saveEnabled by viewModel.enableSaveState.collectAsStateWithLifecycle()
    CreateMealLayout(
        state = viewModel.uiState,
        onClose = onNavigateUp,
        onSave = {
            viewModel.saveMeal()
            onNavigateUp()
        },
        enableSave = saveEnabled,
    )
}

@Composable
internal fun CreateMealLayout(
    state: CreateMealUiState,
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    onSave: () -> Unit = {},
    enableSave: Boolean = false,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CreateMealTopBar(
                onCloseClick = onClose,
                onSaveClick = onSave,
                enableSave = enableSave,
            )
        },
        contentWindowInsets = WindowInsets.safeContent,
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize()
            ) {
                CreateMealInputColumn(
                    state = state,
                )
            }
        }
    }
}

@Composable
private fun CreateMealInputColumn(
    state: CreateMealUiState,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) {
    var carbohydrateExpanded by remember { mutableStateOf(false) }
    var fatExpanded by remember { mutableStateOf(false) }
    var showMoreExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.verticalScroll(state = scrollState),
    ) {
        NameField(
            input = state.nameInput,
            onInputChange = { state.nameInput = it },
        )
        Spacer(modifier = Modifier.height(8.dp))

        PortionField(
            state = state.portionInput,
            label = stringResource(id = R.string.serving_size),
        )
        FoodEnergyField(
            input = state.foodEnergyInput,
            onInputChange = { state.foodEnergyInput = it },
        )

        NutrientWeightField(
            state = state.proteinInput,
            label = stringResource(id = R.string.meal_protein_label)
        )

        NutrientGroupHeaderField(
            state = state.carbohydrateTotalInput,
            label = stringResource(id = R.string.meal_total_carbohydrates_label),
            expanded = carbohydrateExpanded,
            onExpandChangeRequest = { carbohydrateExpanded = it },
            iconButtonContentDescription = stringResource(id = R.string.expand_carbohydrate_group),
        )
        NutrientWeightField(
            state = state.sugarInput,
            label = stringResource(id = M.string.sugar),
            hiddenOnEmpty = !carbohydrateExpanded,
        )
        NutrientWeightField(
            state = state.sugarAlcoholInput,
            label = stringResource(id = M.string.sugar_alcohol),
            hiddenOnEmpty = !carbohydrateExpanded,
        )
        NutrientWeightField(
            state = state.starchInput,
            label = stringResource(id = M.string.starch),
            hiddenOnEmpty = !carbohydrateExpanded,
        )
        NutrientWeightField(
            state = state.fiberInput,
            label = stringResource(id = M.string.fiber),
            hiddenOnEmpty = !carbohydrateExpanded,
        )

        NutrientGroupHeaderField(
            state = state.fatTotalInput,
            label = stringResource(id = R.string.meal_total_fats_label),
            expanded = fatExpanded,
            onExpandChangeRequest = { fatExpanded = it },
            iconButtonContentDescription = stringResource(id = R.string.expand_fat_group),
        )
        NutrientWeightField(
            state = state.monounsaturatedFatInput,
            label = stringResource(id = M.string.monounsaturated_fat),
            hiddenOnEmpty = !fatExpanded,
        )
        NutrientWeightField(
            state = state.polyunsaturatedFatInput,
            label = stringResource(id = M.string.polyunsaturated_fat),
            hiddenOnEmpty = !fatExpanded,
        )
        NutrientWeightField(
            state = state.omega3Input,
            label = stringResource(id = M.string.omega3_fat),
            hiddenOnEmpty = !fatExpanded,
        )
        NutrientWeightField(
            state = state.omega6Input,
            label = stringResource(id = M.string.omega6_fat),
            hiddenOnEmpty = !fatExpanded,
        )
        NutrientWeightField(
            state = state.saturatedFatInput,
            label = stringResource(id = M.string.saturated_fat),
            hiddenOnEmpty = !fatExpanded,
        )
        NutrientWeightField(
            state = state.transFatInput,
            label = stringResource(id = M.string.trans_fat),
            hiddenOnEmpty = !fatExpanded,
        )
        NutrientWeightField(
            state = state.cholesterolFatInput,
            label = stringResource(id = M.string.cholesterol),
            hiddenOnEmpty = !fatExpanded,
        )

        Spacer(modifier = Modifier.height(8.dp))

        ShowMoreDivider(
            expanded = showMoreExpanded,
            onExpandChange = { showMoreExpanded = it },
        )

        NutrientWeightField(
            state = state.calciumInput,
            label = stringResource(id = M.string.calcium),
            hiddenOnEmpty = !showMoreExpanded,
        )

        NutrientWeightField(
            state = state.chlorideInput,
            label = stringResource(id = M.string.chloride),
            hiddenOnEmpty = !showMoreExpanded,
        )

        NutrientWeightField(
            state = state.ironInput,
            label = stringResource(id = M.string.iron),
            hiddenOnEmpty = !showMoreExpanded,
        )

        NutrientWeightField(
            state = state.magnesiumInput,
            label = stringResource(id = M.string.magnesium),
            hiddenOnEmpty = !showMoreExpanded,
        )

        NutrientWeightField(
            state = state.phosphorousInput,
            label = stringResource(id = M.string.phosphorous),
            hiddenOnEmpty = !showMoreExpanded,
        )

        NutrientWeightField(
            state = state.potassiumInput,
            label = stringResource(id = M.string.potassium),
            hiddenOnEmpty = !showMoreExpanded,
        )

        NutrientWeightField(
            state = state.sodiumInput,
            label = stringResource(id = M.string.sodium),
            hiddenOnEmpty = !showMoreExpanded,
        )

        NutrientWeightField(
            state = state.vitaminA,
            label = stringResource(id = M.string.vitamin_a),
            hiddenOnEmpty = !showMoreExpanded,
        )

        NutrientWeightField(
            state = state.vitaminC,
            label = stringResource(id = M.string.vitamin_c),
            hiddenOnEmpty = !showMoreExpanded,
        )
    }
}

@Composable
private fun ShowMoreDivider(
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onExpandChange(!expanded) },
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = stringResource(id = R.string.show_more),
        )

        Icon(
            Icons.Default.ArrowDropDown,
            null,
            modifier = Modifier
                .rotate(if (expanded) 180f else 0f)
        )

        HorizontalDivider(modifier = Modifier.weight(1f))
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateMealTopBar(
    onCloseClick: () -> Unit,
    onSaveClick: () -> Unit,
    enableSave: Boolean,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.create_meal_title),
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Default.Close, null)
            }
        },
        actions = {
            Button(onClick = onSaveClick, enabled = enableSave) {
                Text(text = stringResource(id = R.string.save))
            }
        },
    )
}

@Composable
private fun NameField(
    input: String,
    onInputChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = input,
        onValueChange = onInputChange,
        placeholder = { Text(text = stringResource(id = R.string.meal_name_placeholder)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            autoCorrect = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
        ),
        modifier = modifier,
    )
}

@Composable
private fun NutrientGroupHeaderField(
    state: WeightInputState,
    label: String,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onExpandChangeRequest: (Boolean) -> Unit = {},
    iconButtonContentDescription: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
    ) {
        NutrientWeightField(
            state = state,
            label = label,
            modifier = Modifier.alignByBaseline()
        )
        IconButton(
            onClick = { onExpandChangeRequest(!expanded) },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = iconButtonContentDescription,
                modifier = Modifier
                    .rotate(if (expanded) 180f else 0f)
                    .alignByBaseline()
            )
        }
    }
}

@Composable
private fun PortionField(
    state: PortionInputState,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholderString: String = "",
) {
    val resources = LocalContext.current.resources
    val combinedUnitChoices = remember(state.volumeUnitChoices, state.weightUnitChoices, resources) {
        state.weightUnitChoices.map {
            weightUnitFullString(it, resources)
        } + state.volumeUnitChoices.map { volumeUnitFullString(it, resources) }
    }

    MeasuredUnitField(
        modifier = modifier,
        value = state.input,
        onValueChange = { state.input = it },
        unitLabel = state.weightUnit?.labelString ?: state.volumeUnit?.labelString!!,
        unitChoices = combinedUnitChoices,
        onUnitChange = { unadjustedIndex ->
            val weightIndices = state.weightUnitChoices.indices
            val volumeIndices = state.volumeUnitChoices.indices
            val adjustedIndex = unadjustedIndex - weightIndices.last - 1
            if (unadjustedIndex in weightIndices)
                state.changeToWeightUnit(unadjustedIndex)
            else if (adjustedIndex in volumeIndices)
                state.changeToVolumeUnit(adjustedIndex)
        },
        placeholder = { Text(text = placeholderString) },
        label = { Text(text = label) },
        imeAction = ImeAction.Next,
    )
}

@Composable
private fun FoodEnergyField(
    input: String,
    onInputChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = stringResource(id = R.string.food_energy_label),
) {
    MeasuredUnitField(
        modifier = modifier,
        value = input,
        onValueChange = onInputChange,
        unitLabel = stringResource(id = M.string.kilocalories_label),
        unitChoices = listOf(),
        onUnitChange = {},
        unitPickerEnabled = false,
        label = { Text(text = label) },
        imeAction = ImeAction.Next,
    )

}

@Composable
private fun NutrientWeightField(
    state: WeightInputState,
    label: String,
    modifier: Modifier = Modifier,
    placeholderString: String = "0",
    hiddenOnEmpty: Boolean = false,
) {
    val resources = LocalContext.current.resources
    val weightUnitChoices = remember(state.weightUnitChoices, resources) {
        state.weightUnitChoices.map { weightUnitFullString(it, resources) }
    }
    val showField by remember(hiddenOnEmpty) {
        derivedStateOf { state.input.isNotEmpty() || !hiddenOnEmpty }
    }
    if (showField) {
        MeasuredUnitField(
            modifier = modifier,
            value = state.input,
            onValueChange = { input ->
                state.input = input.filterWithDecimalFormat()
            },
            unitLabel = state.weightUnit.labelString,
            unitChoices = weightUnitChoices,
            onUnitChange = state::changeWeightUnitByIndex,
            placeholder = { Text(text = placeholderString) },
            label = { Text(text = label) },
            imeAction = ImeAction.Next,
        )
    }
}

@Preview
@Composable
private fun CreateMealPreview() {
    CreateMealLayout(
        state = CreateMealUiState(),
        onClose = {},
    )
}
