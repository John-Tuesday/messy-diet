package org.calamarfederal.messydiet

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats.EnterStatsScreen
import org.calamarfederal.messydiet.feature.meal.presentation.MealsGraph
import org.calamarfederal.messydiet.feature.meal.presentation.mealsGraph
import org.calamarfederal.messydiet.feature.meal.presentation.toMealsGraph
import org.calamarfederal.messydiet.feature.search.presentation.SearchFoodGraph
import org.calamarfederal.messydiet.feature.search.presentation.searchFoodGraph
import org.calamarfederal.messydiet.feature.search.presentation.toSearchFoodGraph
import org.calamarfederal.messydiet.ui.theme.MessyDietTheme

@Composable
fun MessyDietContent(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    MessyDietTheme {
        NavHost(
            navController = navController,
            startDestination = MealsGraph.route,
            modifier = modifier,
        ) {
            searchFoodGraph(
                navController = navController,
                navigateToViewAllMeals = {
                    navController.toMealsGraph()
                },
            )
            composable(
                route = "enter-stats",
            ) {
                EnterStatsScreen(
                    onNavigateUp = {
                        navController.toMealsGraph()
                    }
                )
            }
            mealsGraph(
                navController = navController,
                navigateToSearchRemoteMeal = {
                    navController.toSearchFoodGraph()
                }
            )
        }
    }

}
