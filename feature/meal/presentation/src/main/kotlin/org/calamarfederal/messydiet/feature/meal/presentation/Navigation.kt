package org.calamarfederal.messydiet.feature.meal.presentation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.NavType.Companion
import androidx.navigation.compose.composable
import org.calamarfederal.messydiet.feature.meal.presentation.create.CreateMealUi
import org.calamarfederal.messydiet.feature.meal.presentation.create.CreateMealViewModel
import org.calamarfederal.messydiet.feature.meal.presentation.view.ViewAllMealsUi
import org.calamarfederal.messydiet.feature.meal.presentation.view.ViewMealUi

private const val MEALS_GRAPH_ROUTE = "meals-graph"

data object MealsGraph {
    const val route: String = MEALS_GRAPH_ROUTE
}

sealed class NutritionScreen(staticRoute: String = "") {
    internal open val route: String = staticRoute
}

data object ViewAllMealsScreen : NutritionScreen("view-all-meals")
data object CreateMealScreen : NutritionScreen() {
    internal const val MealIdKey = "meal-id-key"
    private const val BaseRoute = "create-meal"
    override val route: String = "$BaseRoute/{$MealIdKey}"
    internal val arguments: List<NamedNavArgument> = listOf(
        navArgument(MealIdKey) { type = NavType.LongType }
    )

    internal fun navRoute(mealId: Long) = "$BaseRoute/$mealId"
}

data object ViewMealScreen : NutritionScreen() {
    internal const val MealIdKey = "meal-id-key"
    private const val BaseRoute = "view-meal"
    override val route: String = "$BaseRoute/{$MealIdKey}"
    internal val arguments: List<NamedNavArgument> = listOf(
        navArgument(MealIdKey) { type = NavType.LongType }
    )

    internal fun navRoute(mealId: Long) = "$BaseRoute/$mealId"
}

fun NavController.toViewMeal(mealId: Long) {
    navigate(ViewMealScreen.navRoute(mealId))
}

fun NavController.toMealsGraph() {
    navigate(MEALS_GRAPH_ROUTE)
}

fun NavController.toCreateMealScreen() {
    navigate(CreateMealScreen.navRoute(mealId = 0L))
}

fun NavController.toEditMealScreen(mealId: Long) {
    navigate(CreateMealScreen.navRoute(mealId = mealId))
}

fun NavController.toViewAllMealsScreen() {
    navigate(ViewAllMealsScreen.route)
}

fun NavGraphBuilder.mealsGraph(
    navController: NavController,
    navigateToSearchRemoteMeal: () -> Unit,
) {
    navigation(
        startDestination = ViewAllMealsScreen.route,
        route = MEALS_GRAPH_ROUTE,
    ) {
        composable(
            route = ViewAllMealsScreen.route
        ) {
            ViewAllMealsUi(
                onCreateMeal = {
                    navController.toCreateMealScreen()
                },
                onViewMeal = {
                    navController.toViewMeal(it)
                },
                onEditMeal = {
                    navController.toEditMealScreen(it)
                },
                onSearchRemoteMeal = {
                    navigateToSearchRemoteMeal()
                },
            )
        }
        composable(
            route = CreateMealScreen.route,
            arguments = CreateMealScreen.arguments,
        ) { backStackEntry ->
            val viewModel: CreateMealViewModel = hiltViewModel()

            val mealId = remember(backStackEntry.arguments) {
                backStackEntry.arguments?.getLong(CreateMealScreen.MealIdKey)
            }
            val locale = LocalConfiguration.current.locales[0]
            LaunchedEffect(mealId) {
                if (mealId != null && mealId != 0L)
                    viewModel.setMealId(mealId, locale)
            }
            CreateMealUi(
                onNavigateUp = {
                    navController.navigateUp()
                },
                viewModel = viewModel,
            )
        }
        composable(
            route = ViewMealScreen.route,
            arguments = ViewMealScreen.arguments,
        ) {
            ViewMealUi(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onEditMeal = {
                    navController.toEditMealScreen(it)
                },
                viewModel = hiltViewModel(),
            )
        }

    }
}
