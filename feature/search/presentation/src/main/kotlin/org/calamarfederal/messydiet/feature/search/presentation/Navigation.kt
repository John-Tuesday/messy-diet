package org.calamarfederal.messydiet.feature.search.presentation

import androidx.compose.runtime.remember
import androidx.navigation.*
import androidx.navigation.compose.composable
import org.calamarfederal.messydiet.feature.search.data.model.FoodId
import org.calamarfederal.messydiet.feature.search.data.model.foodId
import org.calamarfederal.messydiet.feature.search.presentation.detail.FdcFoodItemDetailsUi
import org.calamarfederal.messydiet.feature.search.presentation.search.SearchFdcUi

private const val SEARCH_FOOD_GRAPH_ROUTE = "search-food-graph"

data object SearchFoodGraph {
    const val route: String = SEARCH_FOOD_GRAPH_ROUTE
}

sealed class RemoteFoodScreen(staticRoute: String = "") {
    internal open val route: String = staticRoute
}

data object SearchFoodScreen : RemoteFoodScreen("search-food-screen")

data object FoodDetailsScreen : RemoteFoodScreen() {
    internal const val FOOD_ID_KEY = "food-id"
    internal const val FOOD_ID_TYPE_KEY = "food-id-type"
    private const val BASE_ROUTE = "food-detail"
    override val route: String = "$BASE_ROUTE/{$FOOD_ID_KEY}/{$FOOD_ID_TYPE_KEY}"

    val arguments: List<NamedNavArgument> = listOf(
        navArgument(name = FOOD_ID_KEY) { type = NavType.IntType },
        navArgument(name = FOOD_ID_TYPE_KEY) { type = NavType.IntType },
    )

    internal fun navRoute(foodIdNumber: Int, foodIdType: Int) = "$BASE_ROUTE/$foodIdNumber/$foodIdType"
}

internal fun NavController.toSearchFood() {
    navigate(SearchFoodScreen.route)
}

private fun NavController.toFoodDetails(foodIdNumber: Int, foodIdType: Int) {
    navigate(FoodDetailsScreen.navRoute(foodIdNumber, foodIdType))
}

internal fun NavController.toFoodDetails(foodId: FoodId) {
    navigate(FoodDetailsScreen.navRoute(foodId.id, foodId.type))
}

fun NavController.toSearchFoodGraph() {
    navigate(SearchFoodGraph.route)
}

fun NavGraphBuilder.searchFoodGraph(
    navController: NavController,
    navigateToViewAllMeals: () -> Unit,
) {
    navigation(
        route = SearchFoodGraph.route,
        startDestination = SearchFoodScreen.route,
    ) {
        composable(
            route = SearchFoodScreen.route,
        ) {
            SearchFdcUi(
                toFoodDetails = {
                    navController.toFoodDetails(it)
                },
                toAllMeals = {
                    navigateToViewAllMeals()
                }
            )
        }
        composable(
            route = FoodDetailsScreen.route,
            arguments = FoodDetailsScreen.arguments,
        ) {

            val foodId = remember(it) {
                val id = it.arguments?.getInt(FoodDetailsScreen.FOOD_ID_KEY)!!
                val type = it.arguments?.getInt(FoodDetailsScreen.FOOD_ID_TYPE_KEY)!!
                foodId(id = id, type = type)
            }

            FdcFoodItemDetailsUi(
                foodId = foodId,
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}
