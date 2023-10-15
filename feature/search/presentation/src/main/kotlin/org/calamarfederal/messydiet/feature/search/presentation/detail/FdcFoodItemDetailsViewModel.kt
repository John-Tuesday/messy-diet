package org.calamarfederal.messydiet.feature.search.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import org.calamarfederal.messydiet.feature.search.data.di.FeatureSearchDataModule
import org.calamarfederal.messydiet.feature.search.data.model.FoodDetailsStatus
import org.calamarfederal.messydiet.feature.search.data.model.FoodId
import org.calamarfederal.messydiet.feature.search.data.repository.FoodDetailsRepository
import javax.inject.Inject

class FdcFoodItemDetailsViewModel @Inject constructor(
    private val foodDetailRepo: FoodDetailsRepository,
) : ViewModel() {
    private val _foodIdState = MutableStateFlow<FoodId?>(null)
    val foodIdState = _foodIdState.asStateFlow()

    private val _detailsStatusState = MutableStateFlow<FoodDetailsStatus?>(null)
    val detailsStatusState = _detailsStatusState.asStateFlow()

    fun setFoodId(foodId: FoodId) {
        val oldId = _foodIdState.getAndUpdate { foodId }
        if (oldId == foodId)
            return
        foodDetailRepo
            .foodDetails(foodId)
            .onEach { status -> _detailsStatusState.update { status } }
            .launchIn(viewModelScope + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
                throw (throwable)
//                _detailsStatusState.update {
//                    FoodDetailsStatus.Failure("Unknown network error\nfoodId: id=${foodId.id} type=${foodId.type}")
//                }
            })
    }

    companion object {
        internal fun factor(module: FeatureSearchDataModule): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                FdcFoodItemDetailsViewModel(
                    foodDetailRepo = module.provideFoodDetailsRepository(),
                )
            }
        }
    }
}
