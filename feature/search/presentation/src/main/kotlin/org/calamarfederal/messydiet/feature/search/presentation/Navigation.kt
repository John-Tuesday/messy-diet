package org.calamarfederal.messydiet.feature.search.presentation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import org.calamarfederal.messydiet.feature.search.data.model.FoodId
import org.calamarfederal.messydiet.feature.search.data.model.foodId
import org.calamarfederal.messydiet.feature.search.presentation.detail.FdcFoodItemDetailsUi
import org.calamarfederal.messydiet.feature.search.presentation.scanner.BarcodeScannerScreen
import org.calamarfederal.messydiet.feature.search.presentation.search.SearchFdcUi
import org.calamarfederal.messydiet.feature.search.presentation.search.SearchFdcViewModel

data object SearchFoodGraph {
    const val route: String = "search-food-graph"
}

fun NavController.toSearchFoodGraph() {
    navigate(SearchFoodGraph.route)
}

sealed class RemoteFoodScreen(staticRoute: String = "") {
    internal open val route: String = staticRoute
}

data object SearchFoodScreen : RemoteFoodScreen() {
    internal const val BARCODE_KEY = "barcode"
    private const val BASE_ROUTE = "search-food-screen"
    override val route: String = "$BASE_ROUTE?$BARCODE_KEY={$BARCODE_KEY}"

    val arguments: List<NamedNavArgument> = listOf(
        navArgument(name = BARCODE_KEY) { type = NavType.StringType; defaultValue = "" },
    )

    internal fun navRoute(barcode: String) = "$BASE_ROUTE?$BARCODE_KEY=$barcode"
}

internal fun NavController.toSearchFood(
    barcode: String = "",
    navOptions: NavOptions = navOptions {
        launchSingleTop = true
        popUpTo(route = SearchFoodScreen.route) { inclusive = false }
    },
) {
    navigate(SearchFoodScreen.navRoute(barcode), navOptions)
}

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

private fun NavController.toFoodDetails(foodIdNumber: Int, foodIdType: Int) {
    navigate(FoodDetailsScreen.navRoute(foodIdNumber, foodIdType))
}

internal fun NavController.toFoodDetails(foodId: FoodId) {
    navigate(FoodDetailsScreen.navRoute(foodId.id, foodId.type))
}

internal data object BarcodeScannerScreenDetails : RemoteFoodScreen("barcode-scanner")

internal fun NavController.toBarcodeScanner() = navigate(
    BarcodeScannerScreenDetails.route,
    navOptions {
        launchSingleTop = true
//        popUpTo(SearchFoodScreen.route)
    },
)

fun NavGraphBuilder.searchFoodGraph(
    navController: NavController,
    navigateToViewAllMeals: () -> Unit,
) {
    navigation(
        route = SearchFoodGraph.route,
        startDestination = SearchFoodScreen.navRoute(""),
    ) {
        composable(
            route = SearchFoodScreen.route,
            arguments = SearchFoodScreen.arguments,
        ) { backStackEntry ->
            val viewModel: SearchFdcViewModel = hiltViewModel(backStackEntry)

            val inBarcode = remember(backStackEntry) {
                backStackEntry.arguments?.getString(SearchFoodScreen.BARCODE_KEY) ?: ""
            }

            LaunchedEffect(inBarcode) {
                if (inBarcode.isNotBlank()) {
                    viewModel.query = inBarcode
                    viewModel.submitSearchQuery()
                }
            }

            SearchFdcUi(
                toFoodDetails = {
                    navController.toFoodDetails(it)
                },
                toAllMeals = {
                    navigateToViewAllMeals()
                },
                toBarcodeScanner = {
                    navController.toBarcodeScanner()
                },
                viewModel = viewModel,
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
        composable(
            route = BarcodeScannerScreenDetails.route,
        ) {
            BarcodeScannerScreen(
                onBack = {
                    navController.navigateUp()
                },
                onBarcodeFound = { barcode ->
                    navController.toSearchFood(barcode = barcode)
                }
            )
        }
    }
}
