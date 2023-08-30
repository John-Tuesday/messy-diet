package org.calamarfederal.messydiet

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import org.calamarfederal.messydiet.feature.bmi.presentation.enter_stats.EnterStatsScreen
import org.calamarfederal.messydiet.feature.meal.presentation.mealsGraph
import org.calamarfederal.messydiet.feature.meal.presentation.toMealsGraph
import org.calamarfederal.messydiet.feature.search.presentation.SearchFoodGraph
import org.calamarfederal.messydiet.feature.search.presentation.search.SearchFdcUi
import org.calamarfederal.messydiet.feature.search.presentation.searchFoodGraph
import org.calamarfederal.messydiet.ui.theme.MessyDietTheme

@HiltAndroidApp
class MessyDietApp : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MessyDietContent()
        }
    }
}
