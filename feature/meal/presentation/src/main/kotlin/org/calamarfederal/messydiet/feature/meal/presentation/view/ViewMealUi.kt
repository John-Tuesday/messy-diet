package org.calamarfederal.messydiet.feature.meal.presentation.view

import android.icu.number.LocalizedNumberFormatter
import android.icu.number.NumberFormatter
import android.icu.number.Precision
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.john.tuesday.nutrition.MassPortion
import io.github.john.tuesday.nutrition.VolumePortion
import io.github.john.tuesday.nutrition.scaleToPortion
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import org.calamarfederal.messydiet.feature.meal.presentation.R
import org.calamarfederal.messydiet.measure.*
import org.calamarfederal.physical.measurement.*

@Composable
fun ViewMealUi(
    onNavigateUp: () -> Unit,
    onEditMeal: (Long) -> Unit,
    viewModel: ViewMealViewModel,
) {
    val meal by viewModel.mealState.collectAsStateWithLifecycle()

    meal?.let {
        ViewMealScreenLayout(
            meal = it,
            onNavigateUp = onNavigateUp,
            onEditMeal = onEditMeal,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun ViewMealScreenLayout(
    meal: Meal,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    onEditMeal: (Long) -> Unit = {},
) {
    Scaffold(
        topBar = { ViewMealTopBar(onNavigateUp = onNavigateUp) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEditMeal(meal.id) }) {
                Icon(Icons.Default.Edit, null)
            }
        },
        modifier = modifier,
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            ViewMealInnerLayout(
                meal = meal,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewMealTopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = { /*TODO*/ },
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }
    )
}

@Composable
private fun ViewMealInnerLayout(
    meal: Meal,
    modifier: Modifier = Modifier,
    nameStyle: TextStyle = LocalTextStyle.current + MaterialTheme.typography.headlineMedium,
) {
    Column(modifier = modifier) {
        Text(
            text = meal.name,
            style = nameStyle,
        )

        fun extractPortion() = (meal.foodNutrition.portion.mass?.inGrams()
            ?: meal.foodNutrition.portion.volume?.inMilliliters())!!.toFloat()

        var portionState by remember(meal) {
            mutableFloatStateOf(extractPortion())
        }

        val adjustedNutrition by remember(meal) {
            derivedStateOf {
                meal.foodNutrition.scaleToPortion(
                    when (meal.foodNutrition.portion) {
                        is MassPortion -> MassPortion(portionState.grams)
                        is VolumePortion -> VolumePortion(portionState.milliliters)
                    }
                )
            }
        }
        Surface(
            shape = MaterialTheme.shapes.small,
            tonalElevation = 1.dp,
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
            ) {
                val nutritionInfoStyle = NutrientInfoTextStyle.default()
                val portionUnitString = when {
                    meal.foodNutrition.portion.volume != null -> VolumeUnit.Milliliter.labelString
                    meal.foodNutrition.portion.mass != null -> MassUnit.Gram.labelString
                    else -> throw (NoServingSizeSpecified())
                }

                AdjustableServingSize(
                    sliderValue = portionState,
                    onSliderValueChange = { portionState = it },
                    onResetSliderValue = { portionState = extractPortion() },
                    valueRange = 1f..1_000f,
                    steps = 1_000,
                    unitString = portionUnitString,
                    labelStyle = nutritionInfoStyle.servingSizeLabelStyle,
                    numberStyle = nutritionInfoStyle.servingSizeValueStyle,
                    unitStyle = nutritionInfoStyle.servingSizeValueStyle,
                )
                NutritionInfoColumn(
                    nutrition = adjustedNutrition,
                    hidePortion = true,
                    textStyles = nutritionInfoStyle,
                )
            }
        }
    }
}

@Composable
private fun AdjustableServingSize(
    sliderValue: Float,
    onSliderValueChange: (Float) -> Unit,
    onResetSliderValue: () -> Unit,
    unitString: String,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1_000f,
    steps: Int = 0,
    shape: Shape = MaterialTheme.shapes.small,
    numberFormatter: LocalizedNumberFormatter = NumberFormatter
        .withLocale(LocalConfiguration.current.locales[0])
        .precision(Precision.integer()),
    labelStyle: TextStyle = LocalTextStyle.current,
    numberStyle: TextStyle = LocalTextStyle.current,
    unitStyle: TextStyle = LocalTextStyle.current,
) {
    var inputMode by rememberSaveable {
        mutableIntStateOf(0)
    }
    val isEditMode by remember { derivedStateOf { inputMode == 1 } }
    val isViewOnlyMode by remember { derivedStateOf { inputMode == 0 } }
    fun toEditMode() {
        inputMode = 1
    }

    fun toViewOnlyMode() {
        inputMode = 0
    }

    val textFieldFocusRequester = remember { FocusRequester() }
    Surface(
        modifier = modifier,
        shape = shape,
        tonalElevation = LocalAbsoluteTonalElevation.current + 1.dp,
        onClick = { if (isEditMode) toViewOnlyMode() else toEditMode() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .semantics(true) {}) {
                Text(
                    text = stringResource(id = R.string.serving_size),
                    style = labelStyle,
                    modifier = Modifier
                        .alignByBaseline()
                        .padding(end = 16.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                if (isViewOnlyMode) {
                    Text(
                        text = numberFormatter.format(sliderValue).toString(),
                        style = numberStyle,
                        modifier = Modifier.alignByBaseline()
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                    Text(
                        text = unitString,
                        style = unitStyle,
                        modifier = Modifier.alignByBaseline()
                    )
                } else {
                    var inputText by rememberSaveable(sliderValue) {
                        mutableStateOf(numberFormatter.format(sliderValue).toString())
                    }
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputString ->
                            inputText = inputString
                            inputString.toFloatOrNull()?.let { onSliderValueChange(it) }
                        },
                        textStyle = numberStyle.copy(textAlign = TextAlign.End),
                        readOnly = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        keyboardActions = KeyboardActions { toViewOnlyMode() },
                        suffix = { Text(text = unitString) },
                        modifier = Modifier
                            .alignByBaseline()
                            .focusRequester(textFieldFocusRequester)
                    )

                    LaunchedEffect(Unit) {
                        textFieldFocusRequester.requestFocus()
                    }
                }
            }
            if (isEditMode) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
//                    FilledTonalIconButton(onClick = { textFieldFocusRequester.requestFocus() }) {
//                        Icon(painterResource(id = R.drawable.keyboard), null)
//                    }
                    TextButton(onClick = { onResetSliderValue() }) {
                        Icon(

                            Icons.Default.Refresh,
                            null
                        )
                    }
                    Slider(
                        value = sliderValue,
                        onValueChange = onSliderValueChange,
                        valueRange = valueRange,
                        steps = steps,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )
                    FilledTonalButton(onClick = { toViewOnlyMode() }) {
                        Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.rotate(180f))
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = "Preview",
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview
@Composable
private fun ViewMealPreview() {
    ViewMealScreenLayout(
        meal = Meal(
            name = "Test Name",
        ),
        onNavigateUp = {},
    )
}

@Preview
@Composable
private fun ViewMealAdjustableServingSize() {

}
