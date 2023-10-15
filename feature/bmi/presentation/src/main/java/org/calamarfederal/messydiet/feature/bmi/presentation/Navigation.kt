package org.calamarfederal.messydiet.feature.bmi.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.calamarfederal.feature.bmi.data.di.FeatureBmiDataModule
import org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats.EnterStatsScreen
import org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats.EnterStatsViewModel

class BmiGraph(
    private val context: Context,
) {
    companion object Details {
        internal val startDestination: String = BmiScreen.EnterStatsScreen.route
        internal val route: String = "bmi"
    }

    internal val presentationModule by lazy {
        FeatureBmiDataModule.implementation(context = context)
    }

    fun NavGraphBuilder.bmiGraph(
        navController: NavController,
    ) {
        navigation(
            route = Details.route,
            startDestination = Details.startDestination,
        ) {
            composable(
                route = BmiScreen.EnterStatsScreen.route,
            ) {
                EnterStatsScreen(
                    onNavigateUp = { navController.navigateUp() },
                    viewModel = BmiScreen.EnterStatsScreen.viewModel(module = presentationModule)
                )
            }
        }
    }
}

sealed class BmiScreen<VM : ViewModel>(internal open val route: String = "") {
    @Composable
    internal abstract fun viewModel(module: FeatureBmiDataModule): VM

    data object EnterStatsScreen : BmiScreen<EnterStatsViewModel>("enter-stats") {
        @Composable
        override fun viewModel(module: FeatureBmiDataModule): EnterStatsViewModel {
            return viewModel(factory = EnterStatsViewModel.factory(module = module))
        }
    }
}
