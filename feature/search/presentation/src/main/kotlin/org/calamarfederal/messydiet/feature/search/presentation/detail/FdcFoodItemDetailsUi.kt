package org.calamarfederal.messydiet.feature.search.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.diet_data.model.Portion
import org.calamarfederal.messydiet.feature.measure.NutritionInfoColumn
import org.calamarfederal.messydiet.feature.search.data.model.FoodDetailsStatus
import org.calamarfederal.messydiet.feature.search.data.model.FoodId
import org.calamarfederal.messydiet.feature.search.data.model.FoodIdDummy
import org.calamarfederal.messydiet.feature.search.data.model.FoodItemDetails
import org.calamarfederal.messydiet.feature.search.presentation.R
import org.calamarfederal.messydiet.measure.grams

@Composable
fun FdcFoodItemDetailsUi(
    foodId: FoodId,
    onNavigateUp: () -> Unit,
    viewModel: FdcFoodItemDetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(foodId) {
        viewModel.setFoodId(foodId)
    }
    val detailsState by viewModel.detailsStatusState.collectAsStateWithLifecycle()

    FoodItemDetailsScreen(
        detailsState = detailsState,
        onNavigateUp = onNavigateUp,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun FoodItemDetailsScreen(
    detailsState: FoodDetailsStatus?,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            FoodItemDetailsTopBar(onNavigateUp = onNavigateUp)
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
                .padding(horizontal = 16.dp)
        ) {
            FoodItemDetailsLayoutFromStatus(
                state = detailsState,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FoodItemDetailsTopBar(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {},
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(Icons.Default.ArrowBack, null)
            }
        }
    )

}

@Composable
internal fun FoodItemDetailsLayoutFromStatus(
    state: FoodDetailsStatus?,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        when (state) {
            is FoodDetailsStatus.Success -> {
                FoodItemDetailsColumn(
                    name = state.results.name,
                    nutrition = state.results.nutritionInfo as Nutrition,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }

            is FoodDetailsStatus.Loading -> {
                FoodDetailsLoading()
            }

            is FoodDetailsStatus.Failure -> {
                Column {
                    Text(text = stringResource(id = R.string.fetch_food_detail_failed_title))
                    Text(text = state.message)
                }
            }

            null -> {
//                Text(text = "...")
            }
        }
    }
}

@Composable
private fun FoodDetailsLoading(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.fetch_food_detail_loading),
        )
    }
}

@Composable
internal fun FoodItemDetailsColumn(
    name: String,
    nutrition: Nutrition,
    modifier: Modifier = Modifier,
    nameStyle: TextStyle = LocalTextStyle.current + MaterialTheme.typography.headlineMedium,
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            style = nameStyle,
        )
        Surface(
            shape = MaterialTheme.shapes.small,
            tonalElevation = 1.dp,
            modifier = Modifier
        ) {
            NutritionInfoColumn(
                nutrition = nutrition,
                modifier = Modifier.padding(
                    horizontal = 4.dp,
                    vertical = 2.dp
                )
            )
        }
    }
}

@Preview
@Composable
private fun FoodDetailsPreview() {
    val foodItem = FoodItemDetails(
        foodId = FoodIdDummy,
        name = "Sprite Bottle, 1.75 Liters",
        nutritionInfo = Nutrition(portion = Portion(1.grams)),
    )
    Surface {
        FoodItemDetailsScreen(
            detailsState = FoodDetailsStatus.Success(foodItem)
        )
    }
}
