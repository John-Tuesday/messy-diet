package org.calamarfederal.messydiet.feature.search.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.calamarfederal.messydiet.feature.search.data.model.FoodDetailsStatus
import org.calamarfederal.messydiet.feature.search.data.model.FoodId
import org.calamarfederal.messydiet.feature.search.data.model.SearchStatus
import org.calamarfederal.messydiet.feature.search.presentation.R
import org.calamarfederal.messydiet.feature.search.presentation.detail.FoodItemDetailsLayoutFromStatus

@Composable
internal fun SearchFdcUi(
    onNavigateUp: () -> Unit,
    toAllMeals: () -> Unit,
    toBarcodeScanner: () -> Unit,
    viewModel: SearchFdcViewModel,
) {
    val searchStatus by viewModel.searchStatusState.collectAsStateWithLifecycle()
    val foodItemDetails by viewModel.detailsStatusState.collectAsStateWithLifecycle()

    SearchFdcScreen(
        onNavigateUp = onNavigateUp,
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
internal fun SearchFdcScreen(
    queryInput: String,
    onQueryChange: (String) -> Unit,
    onSubmitQuery: () -> Unit,
    getFoodItemDetails: (FoodId) -> Unit,
    saveFoodItemDetails: () -> Unit,
    useBarcodeScanner: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    foodItemDetailStatus: FoodDetailsStatus? = null,
    searchStatus: SearchStatus? = null,
) {
    var searchBarActive: Boolean by remember {
        mutableStateOf(false)
    }
    val onChangeSearchBarActive: (Boolean) -> Unit = remember {
        { searchBarActive = it }
    }

    LaunchedEffect(searchStatus) {
        if (searchStatus is SearchStatus.Loading)
            onChangeSearchBarActive(true)
    }
    LaunchedEffect(foodItemDetailStatus) {
        if (foodItemDetailStatus is FoodDetailsStatus.Loading)
            onChangeSearchBarActive(false)
    }

    Scaffold(
        topBar = {
            SearchFdcTopBar(
                queryInput = queryInput,
                onQueryChange = onQueryChange,
                onSubmitQuery = onSubmitQuery,
                active = searchBarActive,
                onActiveChange = onChangeSearchBarActive,
                searchStatus = searchStatus,
                onNavigateUp = onNavigateUp,
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
                Icon(painterResource(id = R.drawable.barcode), null)
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
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onNavigateUp: () -> Unit,
    onFoodItemClick: (FoodId) -> Unit,
    useBarcodeScanner: () -> Unit,
    modifier: Modifier = Modifier,
    searchStatus: SearchStatus? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        SearchBar(
            query = queryInput,
            onQueryChange = onQueryChange,
            onSearch = { onSubmitQuery() },
            active = active,
            onActiveChange = onActiveChange,
            placeholder = {
                Text(text = stringResource(id = R.string.search_searchbar_hint))
            },
            trailingIcon = {
                IconButton(onClick = useBarcodeScanner) {
                    Icon(painterResource(id = R.drawable.barcode), null)
                }
            },
            leadingIcon = {
                if (active) {
                    IconButton(onClick = { onActiveChange(false) }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                } else
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
            },
        ) {
            SearchContent(
                searchStatus = searchStatus,
                modifier = Modifier.fillMaxSize(),
                onFoodItemClick = {
                    onFoodItemClick(it)
                    onActiveChange(false)
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
            /* TODO show more details about the error to the user */
        }
    }
}

@Preview
@Composable
fun SearchFdcPreview() {
    val searchStatus = null//SearchStatus.Loading
    val detailStatus = null
//    val detailStatus = FoodDetailsStatus.Loading
    SearchFdcScreen(
        queryInput = "Sample Input",
        onQueryChange = {},
        onSubmitQuery = {},
        getFoodItemDetails = {},
        saveFoodItemDetails = {},
        searchStatus = searchStatus,
        foodItemDetailStatus = detailStatus,
        useBarcodeScanner = {},
        onNavigateUp = {},
    )
}
