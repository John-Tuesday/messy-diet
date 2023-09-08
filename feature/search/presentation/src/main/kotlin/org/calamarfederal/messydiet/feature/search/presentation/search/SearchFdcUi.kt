package org.calamarfederal.messydiet.feature.search.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.calamarfederal.messydiet.feature.search.data.model.FoodDetailsStatus
import org.calamarfederal.messydiet.feature.search.data.model.FoodId
import org.calamarfederal.messydiet.feature.search.data.model.SearchStatus
import org.calamarfederal.messydiet.feature.search.presentation.R
import org.calamarfederal.messydiet.feature.search.presentation.detail.FoodItemDetailsLayoutFromStatus

@Composable
fun SearchFdcUi(
    toFoodDetails: (FoodId) -> Unit,
    toAllMeals: () -> Unit,
    toBarcodeScanner: () -> Unit,
    viewModel: SearchFdcViewModel = hiltViewModel(),
) {
    val searchStatus by viewModel.searchStatusState.collectAsStateWithLifecycle()
    val foodItemDetails by viewModel.detailsStatusState.collectAsStateWithLifecycle()

    SearchFdcScreen(
        queryInput = viewModel.query,
        onQueryChange = { viewModel.query = it },
        onSubmitQuery = viewModel::submitSearchQuery,
        searchStatus = searchStatus,
        getFoodItemDetails = viewModel::getFoodDetails,
        foodItemDetailStatus = foodItemDetails,
        useBarcodeScanner = toBarcodeScanner,
        saveFoodItemDetails = {
            viewModel.saveFoodDetails()
            toAllMeals()
        },
    )
}

@Composable
fun SearchFdcScreen(
    queryInput: String,
    onQueryChange: (String) -> Unit,
    onSubmitQuery: () -> Unit,
    getFoodItemDetails: (FoodId) -> Unit,
    saveFoodItemDetails: () -> Unit,
    useBarcodeScanner: () -> Unit,
    foodItemDetailStatus: FoodDetailsStatus?,
    modifier: Modifier = Modifier,
    searchStatus: SearchStatus? = null,
) {
    Scaffold(
        topBar = {
            SearchFdcTopBar(
                queryInput = queryInput,
                onQueryChange = onQueryChange,
                onSubmitQuery = onSubmitQuery,
                searchStatus = searchStatus,
                onNavigateUp = {},
                onFoodItemClick = getFoodItemDetails,
                useBarcodeScanner = useBarcodeScanner,
            )
        },
        floatingActionButton = {
            SearchFoodFab(
                foodItemDetailStatus = foodItemDetailStatus,
                saveFoodItemDetails = saveFoodItemDetails,
                useBarcodeScanner = useBarcodeScanner,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = modifier,
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            FoodItemDetailsLayoutFromStatus(
                state = foodItemDetailStatus,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp),
            )
        }
    }
}

@Composable
private fun SearchFoodFab(
    foodItemDetailStatus: FoodDetailsStatus?,
    saveFoodItemDetails: () -> Unit,
    useBarcodeScanner: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (foodItemDetailStatus) {
        is FoodDetailsStatus.Success -> {
            ExtendedFloatingActionButton(
                onClick = saveFoodItemDetails,
                text = { Text(text = stringResource(id = R.string.add_meal)) },
                icon = { Icon(Icons.Default.Add, null) },
                modifier = modifier,
            )
        }

        null -> {
            FloatingActionButton(onClick = useBarcodeScanner) {
                Icon(Icons.Outlined.Info, null)
            }
        }

        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchFdcTopBar(
    queryInput: String,
    onQueryChange: (String) -> Unit,
    onSubmitQuery: () -> Unit,
    onNavigateUp: () -> Unit,
    onFoodItemClick: (FoodId) -> Unit,
    useBarcodeScanner: () -> Unit,
    modifier: Modifier = Modifier,
    searchStatus: SearchStatus? = null,
) {
    var active by rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        SearchBar(
            query = queryInput,
            onQueryChange = onQueryChange,
            onSearch = { onSubmitQuery() },
            active = active,
            onActiveChange = { active = it },
            placeholder = {
                Text(text = stringResource(id = R.string.search_searchbar_hint))
            },
            trailingIcon = {
                IconButton(onClick = useBarcodeScanner) {
                    Icon(Icons.Outlined.Info, null)
                }
            },
            leadingIcon = {
                if (active) {
                    IconButton(onClick = { active = false }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                } else
                    Icon(Icons.Default.Search, null)
            },
        ) {
            SearchContent(
                searchStatus = searchStatus,
                modifier = Modifier.fillMaxSize(),
                onFoodItemClick = {
                    onFoodItemClick(it)
                    active = false
                },
            )
        }
    }
}

@Composable
private fun SearchContent(
    searchStatus: SearchStatus?,
    onFoodItemClick: (FoodId) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (searchStatus) {
        is SearchStatus.Success -> SearchSuccess(
            status = searchStatus,
            onFoodItemClick = onFoodItemClick,
            modifier = modifier
        )

        is SearchStatus.Loading -> SearchLoading(modifier = modifier)
        is SearchStatus.Failure -> SearchFailure(status = searchStatus, modifier = modifier)
        null -> SearchNotExistent(modifier = modifier)
    }
}

@Composable
private fun SearchNotExistent(
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.search_nonexistent))
        }
    }

}

@Composable
private fun SearchSuccess(
    status: SearchStatus.Success,
    onFoodItemClick: (FoodId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        if (status.results.isEmpty()) {
            Text(text = stringResource(id = R.string.search_no_results))
        }
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(items = status.results, key = { it.id }) { foodItem ->
                ListItem(
                    headlineContent = { Text(text = foodItem.name) },
                    supportingContent = {},
                    modifier = Modifier
                        .clickable(role = Role.Button) { onFoodItemClick(foodItem.foodId) }
                )
            }
        }
    }
}

@Composable
private fun SearchLoading(
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id = R.string.search_loading))
        }
    }
}

@Composable
private fun SearchFailure(
    status: SearchStatus.Failure,
    modifier: Modifier = Modifier,
) {

    Surface(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Text(text = stringResource(id = R.string.search_failed))
            Text(text = status.message)
        }
    }
}

@Preview
@Composable
fun SearchFdcPreview() {
    SearchFdcScreen(
        queryInput = "Sample Input",
        onQueryChange = {},
        onSubmitQuery = {},
        getFoodItemDetails = {},
        searchStatus = null,
        saveFoodItemDetails = {},
        foodItemDetailStatus = null,
        useBarcodeScanner = {},
    )
}
