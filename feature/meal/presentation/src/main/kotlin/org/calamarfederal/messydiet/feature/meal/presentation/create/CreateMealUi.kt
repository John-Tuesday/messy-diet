package org.calamarfederal.messydiet.feature.meal.presentation.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.calamarfederal.messydiet.feature.meal.presentation.R
import org.calamarfederal.messydiet.feature.measure.*
import org.calamarfederal.messydiet.measure.R as M

private fun String.filterWithDecimalFormat(decimalSeparator: Char = '.'): String {
    var hasDecimalSeparator = false
    return this.filter {
        if (it == decimalSeparator && !hasDecimalSeparator) {
            hasDecimalSeparator = true
            true
        } else it.isDigit()
    }
}

internal enum class CreateMealListKey {
    Name,
    ServingSize,
}

@Composable
fun CreateMealUi(
    viewModel: CreateMealViewModel = hiltViewModel(),
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
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
                .fillMaxSize()
        ) {
            var fatExpanded by remember { mutableStateOf(false) }
            var carbohydrateExpanded by remember { mutableStateOf(false) }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                item(key = CreateMealListKey.Name) {
                    NameField(
                        input = state.nameInput,
                        onInputChange = { state.nameInput = it },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item(key = CreateMealListKey.ServingSize) {
                    NutrientWeightField(
                        state = state.servingSizeInput,
                        label = stringResource(id = R.string.serving_size)
                    )
                }
                item(key = R.string.meal_protein_label) {
                    NutrientWeightField(
                        state = state.proteinInput,
                        label = stringResource(id = R.string.meal_protein_label)
                    )
                }

                fatGroup(
                    state = state,
                    expanded = fatExpanded,
                    onExpandChangeRequest = { fatExpanded = it },
                )
                carbohydrateGroup(
                    state = state,
                    expanded = carbohydrateExpanded,
                    onExpandChangeRequest = { carbohydrateExpanded = it },
                )
            }
        }
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

private fun LazyListScope.carbohydrateGroup(
    state: CreateMealUiState,
    expanded: Boolean = false,
    onExpandChangeRequest: (Boolean) -> Unit = {},
) {
    item(key = R.string.meal_total_carbohydrates_label) {
        NutrientGroupHeaderField(
            state = state.carbohydrateTotalInput,
            label = stringResource(id = R.string.meal_total_carbohydrates_label),
            expanded = expanded,
            onExpandChangeRequest = onExpandChangeRequest,
            iconButtonContentDescription = stringResource(id = R.string.expand_carbohydrate_group),
        )
    }
    nutrientWeightOptionalField(
        state = state.sugarInput,
        label = { stringResource(id = M.string.sugar) },
        forceShow = expanded,
        key = M.string.sugar,
    )
    nutrientWeightOptionalField(
        state = state.sugarAlcoholInput,
        label = { stringResource(id = M.string.sugar_alcohol)},
        forceShow = expanded,
        key = M.string.sugar_alcohol,
    )
    nutrientWeightOptionalField(
        state = state.starchInput,
        label = { stringResource(id = M.string.starch) },
        forceShow = expanded,
        key = M.string.starch,
    )
    nutrientWeightOptionalField(
        state = state.fiberInput,
        label = { stringResource(id = M.string.fiber) },
        forceShow = expanded,
        key = M.string.fiber,
    )
}

private fun LazyListScope.fatGroup(
    state: CreateMealUiState,
    expanded: Boolean = false,
    onExpandChangeRequest: (Boolean) -> Unit = {},
) {
    item(key = R.string.meal_total_fats_label) {
        NutrientGroupHeaderField(
            state = state.fatTotalInput,
            label = stringResource(id = R.string.meal_total_fats_label),
            expanded = expanded,
            onExpandChangeRequest = onExpandChangeRequest,
            iconButtonContentDescription = stringResource(id = R.string.expand_fat_group),
        )
    }
    nutrientWeightOptionalField(
        state = state.monounsaturatedFatInput,
        label = { stringResource(id = M.string.monounsaturated_fat) },
        forceShow = expanded,
        key = M.string.monounsaturated_fat,
    )
    nutrientWeightOptionalField(
        state = state.polyunsaturatedFatInput,
        label = { stringResource(id = M.string.polyunsaturated_fat) },
        forceShow = expanded,
        key = M.string.polyunsaturated_fat,
    )
    nutrientWeightOptionalField(
        state = state.omega3Input,
        label = { stringResource(id = M.string.omega3_fat) },
        forceShow = expanded,
        key = M.string.omega3_fat,
    )
    nutrientWeightOptionalField(
        state = state.omega6Input,
        label = { stringResource(id = M.string.omega6_fat) },
        forceShow = expanded,
        key = M.string.omega6_fat,
    )
    nutrientWeightOptionalField(
        state = state.saturatedFatInput,
        label = { stringResource(id = M.string.saturated_fat) },
        forceShow = expanded,
        key = M.string.saturated_fat
    )
    nutrientWeightOptionalField(
        state = state.transFatInput,
        label = { stringResource(id = M.string.trans_fat) },
        forceShow = expanded,
        key = M.string.trans_fat,
    )
    nutrientWeightOptionalField(
        state = state.cholesterolFatInput,
        label = { stringResource(id = M.string.cholesterol) },
        forceShow = expanded,
        key = M.string.cholesterol,
    )
}

private fun LazyListScope.nutrientWeightOptionalField(
    state: WeightInputState,
    label: @Composable () -> String,
    modifier: Modifier = Modifier,
    forceShow: Boolean = false,
    placeholderString: String = "0",
    key: Any? = null,
) {
    if (forceShow || state.input.isNotEmpty()) {
        item(key = key) {
            NutrientWeightField(
                state = state,
                label = label(),
                modifier = modifier,
                placeholderString = placeholderString,
            )
        }
    }
}

@Composable
private fun NutrientWeightField(
    state: WeightInputState,
    label: String,
    modifier: Modifier = Modifier,
    placeholderString: String = "0",
) {
    val resources = LocalContext.current.resources
    val weightUnitChoices = remember(state.weightUnitChoices, resources) {
        state.weightUnitChoices.map { weightUnitFullString(it, resources) }
    }
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

@Preview
@Composable
private fun CreateMealPreview() {
    CreateMealLayout(
        state = CreateMealUiState(),
        onClose = {},
    )
}
