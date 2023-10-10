package org.calamarfederal.messydiet.feature.meal.presentation.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.calamarfederal.messydiet.feature.meal.data.MealRepository
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ViewAllMealViewModel @Inject constructor(
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
}
