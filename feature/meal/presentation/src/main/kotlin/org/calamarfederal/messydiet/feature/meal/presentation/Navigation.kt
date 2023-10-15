package org.calamarfederal.messydiet.feature.meal.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import org.calamarfederal.messydiet.feature.meal.data.di.FeatureMealDataModule
import org.calamarfederal.messydiet.feature.meal.presentation.create.CreateMealUi
import org.calamarfederal.messydiet.feature.meal.presentation.create.CreateMealViewModel
import org.calamarfederal.messydiet.feature.meal.presentation.view.ViewAllMealViewModel
import org.calamarfederal.messydiet.feature.meal.presentation.view.ViewAllMealsUi
import org.calamarfederal.messydiet.feature.meal.presentation.view.ViewMealUi
import org.calamarfederal.messydiet.feature.meal.presentation.view.ViewMealViewModel

private const val MEALS_GRAPH_ROUTE = "meals-graph"

internal interface FeatureMealsPresentationModule : FeatureMealDataModule {
    companion object
}

internal class FeatureMealsPresentationModuleImplementation(
    private val dataModule: FeatureMealDataModule,
) : FeatureMealsPresentationModule, FeatureMealDataModule by dataModule

internal fun FeatureMealsPresentationModule.Companion.implementation(
    context: Context,
): FeatureMealsPresentationModule {
    return FeatureMealsPresentationModuleImplementation(
        dataModule = FeatureMealDataModule.implementation(context)
    )
}

@Composable
internal fun rememberFeatureMealsPresentationModule(
    context: Context = LocalContext.current,
): FeatureMealsPresentationModule = remember {
    FeatureMealsPresentationModule.implementation(context = context)
}

data object MealsGraph {
    const val route: String = MEALS_GRAPH_ROUTE
}

data class MealsGraphModule(
    val context: Context,
) {
    internal val featureMealsPresentationModule: FeatureMealsPresentationModule by lazy {
        FeatureMealsPresentationModule.implementation(context)
    }

    fun NavGraphBuilder.mealsGraph(
        navController: NavController,
        navigateToSearchRemoteMeal: () -> Unit,
    ) {
        mealsGraph(
            navController = navController,
            navigateToSearchRemoteMeal = navigateToSearchRemoteMeal,
            featureMealsPresentationModule = featureMealsPresentationModule,
        )
    }
}

sealed class NutritionScreen<VM : ViewModel>(staticRoute: String = "") {
    internal open val route: String = staticRoute

    @Composable
    internal abstract fun viewModel(module: FeatureMealsPresentationModule): VM
}

data object ViewAllMealsScreen : NutritionScreen<ViewAllMealViewModel>("view-all-meals") {
    @Composable
    override fun viewModel(module: FeatureMealsPresentationModule): ViewAllMealViewModel {
        return viewModel(factory = ViewAllMealViewModel.factory(module))
    }
}

data object CreateMealScreen : NutritionScreen<CreateMealViewModel>() {
    internal const val MealIdKey = "meal-id-key"
    private const val BaseRoute = "create-meal"
    override val route: String = "$BaseRoute/{$MealIdKey}"
    internal val arguments: List<NamedNavArgument> = listOf(
        navArgument(MealIdKey) { type = NavType.LongType }
    )

    internal fun navRoute(mealId: Long) = "$BaseRoute/$mealId"

    @Composable
    override fun viewModel(module: FeatureMealsPresentationModule): CreateMealViewModel {
        return viewModel(factory = CreateMealViewModel.factory(module))
    }
}

data object ViewMealScreen : NutritionScreen<ViewMealViewModel>() {
    internal const val MealIdKey = "meal-id-key"
    private const val BaseRoute = "view-meal"
    override val route: String = "$BaseRoute/{$MealIdKey}"
    internal val arguments: List<NamedNavArgument> = listOf(
        navArgument(MealIdKey) { type = NavType.LongType }
    )

    internal fun navRoute(mealId: Long) = "$BaseRoute/$mealId"

    @Composable
    override fun viewModel(module: FeatureMealsPresentationModule): ViewMealViewModel {
        return viewModel(factory = ViewMealViewModel.factory(module))
    }
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

private fun NavGraphBuilder.mealsGraph(
    navController: NavController,
    navigateToSearchRemoteMeal: () -> Unit,
    featureMealsPresentationModule: FeatureMealsPresentationModule,
) {
    navigation(
        startDestination = ViewAllMealsScreen.route,
        route = MEALS_GRAPH_ROUTE,
    ) {
        composable(
            route = ViewAllMealsScreen.route
        ) {
            ViewAllMealsUi(
                viewModel = ViewAllMealsScreen.viewModel(module = featureMealsPresentationModule),
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
            val viewModel: CreateMealViewModel = CreateMealScreen.viewModel(module = featureMealsPresentationModule)

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
                viewModel = ViewMealScreen.viewModel(module = featureMealsPresentationModule),
            )
        }

    }
}
