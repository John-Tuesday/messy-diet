package org.calamarfederal.messydiet.feature.meal.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.calamarfederal.messydiet.feature.meal.data.MealRepository
import org.calamarfederal.messydiet.feature.meal.presentation.di.FeatureMealsPresentationModule
import kotlin.time.Duration.Companion.seconds

class ViewAllMealViewModel(
    private val mealRepo: MealRepository,
) : ViewModel() {
    val allMealsState = mealRepo
        .getAllMealInfo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = listOf()
        )

    fun deleteMeals(mealIds: List<Long>) {
        viewModelScope.launch {
            for (id in mealIds) {
                mealRepo.deleteMeal(id)
            }
        }
    }

    companion object {
        internal fun factory(
            module: FeatureMealsPresentationModule,
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    ViewAllMealViewModel(
                        mealRepo = module.provideMealRepository()
                    )
                }
            }
        }
    }
}
