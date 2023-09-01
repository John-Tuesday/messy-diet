package org.calamarfederal.messydiet.feature.meal.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.diet_data.model.Portion
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import org.calamarfederal.messydiet.feature.measure.NutritionInfoColumn
import org.calamarfederal.messydiet.measure.grams
import org.calamarfederal.messydiet.measure.milliliters

@Composable
fun ViewMealUi(
    onNavigateUp: () -> Unit,
    onEditMeal: (Long) -> Unit,
    viewModel: ViewMealViewModel = hiltViewModel(),
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
                Icon(Icons.Default.ArrowBack, null)
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
        var portionState by remember(meal) {
            mutableFloatStateOf(
                (meal.portion.weight?.inGrams() ?: meal.portion.volume?.inMilliliters())!!.toFloat()
            )
        }

        val adjustedNutrition by remember(meal) {
            derivedStateOf {
                val portion = meal.portion.weight?.let {
                    Portion(portionState.grams)
                } ?: meal.portion.volume?.let {
                    Portion(portionState.milliliters)
                } ?: Portion()
                (Nutrition() + meal).scaleToPortion(portion)
            }
        }
        Slider(
            value = portionState,
            onValueChange = { portionState = it },
            valueRange = 0f..1000f,
            steps = 1001 / 5,
        )
        Surface(
            shape = MaterialTheme.shapes.small,
            tonalElevation = 1.dp,
        ) {
            NutritionInfoColumn(
                nutrition = adjustedNutrition,
                modifier = Modifier
                    .padding(
                        horizontal = 4.dp,
                        vertical = 2.dp,
                    )
            )
        }
    }
}

@Preview
@Composable
private fun ViewMealPreview() {

    ViewMealScreenLayout(
        meal = Meal(name = "Test Name"),
        onNavigateUp = {},
    )
}
