package org.calamarfederal.messydiet.feature.meal.presentation.view

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.calamarfederal.messydiet.feature.meal.data.MealRepository
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import org.calamarfederal.messydiet.feature.meal.presentation.ViewMealScreen
import org.calamarfederal.messydiet.feature.meal.presentation.di.FeatureMealsPresentationModule
import kotlin.time.Duration.Companion.seconds

private const val MEAL_ID_KEY = ViewMealScreen.MealIdKey

class ViewMealViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val mealRepo: MealRepository,
) : ViewModel() {
    private val mealIdState = savedStateHandle
        .getStateFlow(MEAL_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val mealState: StateFlow<Meal?> = mealIdState
        .flatMapLatest {
            mealRepo.getMeal(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = null,
        )

    companion object {
        internal fun factory(module: FeatureMealsPresentationModule): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    ViewMealViewModel(
                        savedStateHandle = createSavedStateHandle(),
                        mealRepo = module.provideMealRepository(),
                    )
                }
            }
        }
    }
}
