package org.calamarfederal.messydiet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.calamarfederal.messydiet.feature.bmi.presentation.BmiGraph
import org.calamarfederal.messydiet.feature.meal.presentation.MealsGraph
import org.calamarfederal.messydiet.feature.meal.presentation.MealsGraphModule
import org.calamarfederal.messydiet.feature.meal.presentation.toMealsGraph
import org.calamarfederal.messydiet.feature.search.presentation.SearchFoodGraphModule
import org.calamarfederal.messydiet.feature.search.presentation.toSearchFoodGraph
import org.calamarfederal.messydiet.ui.theme.MessyDietTheme

@Composable
fun MessyDietContent(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    MessyDietTheme {
        val context = LocalContext.current
        val mealsGraph = remember { MealsGraphModule.implementation(context) }
        val bmiGraph = remember { BmiGraph(context) }
        val searchGraph = remember { SearchFoodGraphModule(context) }
        NavHost(
            navController = navController,
            startDestination = MealsGraph.route,
            modifier = modifier,
        ) {
            with(searchGraph) {
                searchFoodGraph(
                    navController = navController,
                    navigateToViewAllMeals = {
                        navController.toMealsGraph()
                    },
                )
            }
            with(bmiGraph) {
                bmiGraph(
                    navController = navController,
                )
            }
            with(mealsGraph) {
                mealsGraph(
                    navController = navController,
                    navigateToSearchRemoteMeal = {
                        navController.toSearchFoodGraph()
                    }
                )
            }
        }
    }

}
