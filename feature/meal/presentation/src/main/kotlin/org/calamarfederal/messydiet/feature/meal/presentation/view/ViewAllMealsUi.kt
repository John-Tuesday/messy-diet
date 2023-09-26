package org.calamarfederal.messydiet.feature.meal.presentation.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import org.calamarfederal.messydiet.feature.meal.presentation.R

@Composable
fun ViewAllMealsUi(
    onCreateMeal: () -> Unit,
    onViewMeal: (Long) -> Unit,
    onEditMeal: (Long) -> Unit,
    onSearchRemoteMeal: () -> Unit,
    viewModel: ViewAllMealViewModel = hiltViewModel(),
) {
    val meals by viewModel.allMealsState.collectAsStateWithLifecycle()

    ViewAllMealsLayout(
        meals = meals,
        onCreateMeal = onCreateMeal,
        onViewMeal = onViewMeal,
        onSearchRemoteMeal = onSearchRemoteMeal,
        onEditMeal = onEditMeal,
        onDeleteMeals = viewModel::deleteMeals,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllMealsLayout(
    meals: List<Meal>,
    modifier: Modifier = Modifier,
    onSearchRemoteMeal: () -> Unit = {},
    onCreateMeal: () -> Unit = {},
    onViewMeal: (Long) -> Unit = {},
    onEditMeal: (Long) -> Unit = {},
    onDeleteMeals: (List<Long>) -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerScope = rememberCoroutineScope()
    BackHandler(enabled = drawerState.isOpen) {
        drawerScope.launch { drawerState.close() }
    }
    ViewAllDrawerSheet(
        onCreateMeal = onCreateMeal,
        onSearchRemoteMeal = onSearchRemoteMeal,
        drawerState = drawerState,
    ) {
        Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            ViewAllMealTopBar(
                scrollBehavior = scrollBehavior,
                onMenuClick = { drawerScope.launch { drawerState.open() } }
            )
        }, floatingActionButton = {
            ViewAllFab(onCreateMeal = onCreateMeal, onSearchRemoteMeal = onSearchRemoteMeal)
        }) { padding ->
            Surface(
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding)
                    .fillMaxSize()
            ) {
                MealsLazyColumn(
                    meals = meals,
                    onViewMeal = { onViewMeal(it.id) },
                    onDeleteMeal = onDeleteMeals,
                    onEditMeal = onEditMeal,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun ViewAllDrawerSheet(
    onCreateMeal: () -> Unit,
    onSearchRemoteMeal: () -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet {
                NavigationDrawerItem(
                    label = { Text(text = "Create custom meal") },
                    icon = { Icon(Icons.Default.Create, null) },
                    selected = false,
                    onClick = onCreateMeal,
                )
                NavigationDrawerItem(
                    label = { Text(text = "Search for food product") },
                    icon = { Icon(Icons.Default.Search, null) },
                    selected = false,
                    onClick = onSearchRemoteMeal,
                )
            }
        },
        content = content,
    )
}

@Composable
private fun ViewAllFab(
    onCreateMeal: () -> Unit,
    onSearchRemoteMeal: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SmallFloatingActionButton(onClick = onSearchRemoteMeal) {
            Icon(Icons.Default.Search, null)
        }
        FloatingActionButton(
            onClick = onCreateMeal,
        ) {
            Icon(Icons.Default.Create, stringResource(id = R.string.create_new_meal))
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ViewAllMealTopBar(
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, null)
            }
        },
        title = { Text(text = stringResource(id = R.string.all_meals_title)) },
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MealsLazyColumn(
    meals: List<Meal>,
    onViewMeal: (Meal) -> Unit,
    onDeleteMeal: (List<Long>) -> Unit,
    onEditMeal: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedMeals = remember { mutableStateListOf<Long>() }
    val sheetState = rememberModalBottomSheetState()
    val showOptions by remember(selectedMeals) {
        derivedStateOf { selectedMeals.isNotEmpty() }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        items(items = meals, key = { it.id }) { meal ->
            val haptic = LocalHapticFeedback.current
            val isSelected by remember(meal.id) {
                derivedStateOf {
                    selectedMeals.contains(meal.id)
                }
            }

            MealListItem(
                meal = meal,
                selected = isSelected,
                modifier = Modifier
                    .combinedClickable(
                        role = Role.Button,
                        onLongClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            selectedMeals.add(meal.id)
                        },
                        onClick = {
                            if (selectedMeals.isEmpty())
                                onViewMeal(meal)
                            else if (selectedMeals.contains(meal.id))
                                selectedMeals.remove(meal.id)
                            else
                                selectedMeals.add(meal.id)
                        },
                    )
            )
        }
    }

    if (showOptions) {
        ModalBottomSheet(
            onDismissRequest = { selectedMeals.clear() },
            sheetState = sheetState,
        ) {
            ListItem(
                leadingContent = { Icon(Icons.Default.Delete, null) },
                headlineContent = {
                    Text(
                        text = if (selectedMeals.size == 1)
                            stringResource(id = R.string.delete)
                        else stringResource(id = R.string.delete_all)
                    )
                },
                modifier = Modifier
                    .clickable(role = Role.Button) { onDeleteMeal(selectedMeals); selectedMeals.clear() },
            )
            if (selectedMeals.size == 1) {
                ListItem(
                    leadingContent = { Icon(Icons.Default.Edit, null) },
                    headlineContent = { Text(text = stringResource(id = R.string.edit)) },
                    modifier = Modifier
                        .clickable(role = Role.Button) { onEditMeal(selectedMeals.single()); selectedMeals.clear() },
                )
            }
        }
    }
}

@Composable
private fun MealListItem(
    meal: Meal,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            if (selected) {
                Icon(Icons.Default.CheckCircle, null)
            }
        },
        headlineContent = { Text(text = meal.name) },
        tonalElevation = LocalAbsoluteTonalElevation.current + if (selected) 1.dp else 0.dp
    )
}

@Preview
@Composable
private fun ViewAllMealPreview() {
    val previewMeals = remember {
        mutableStateListOf(Meal(name = "alpha", id = 1L))
    }
    ViewAllMealsLayout(
        meals = previewMeals,
        onCreateMeal = {},
        onViewMeal = {},
    )
}
