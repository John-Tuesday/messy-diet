package org.calamarfederal.messydiet.feature.search.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.calamarfederal.messydiet.feature.search.data.FoodDetailsRepository
import org.calamarfederal.messydiet.feature.search.data.FoodSearchRepository
import org.calamarfederal.messydiet.feature.search.data.SaveFoodDetailsRepository
import org.calamarfederal.messydiet.feature.search.data.di.FeatureSearchDataModule
import org.calamarfederal.messydiet.feature.search.data.model.FoodDetailsStatus
import org.calamarfederal.messydiet.feature.search.data.model.FoodId
import org.calamarfederal.messydiet.feature.search.data.model.SearchStatus
import kotlin.coroutines.cancellation.CancellationException

class SearchFdcViewModel(
    private val searchRepository: FoodSearchRepository,
    private val foodDetailsRepository: FoodDetailsRepository,
    private val saveFoodDetailsRepository: SaveFoodDetailsRepository,
) : ViewModel() {
    var query by mutableStateOf("")

    private val _searchStatusState = MutableStateFlow<SearchStatus?>(null)
    val searchStatusState = _searchStatusState.asStateFlow()

    private val _detailsStatusState = MutableStateFlow<FoodDetailsStatus?>(null)
    val detailsStatusState = _detailsStatusState.asStateFlow()

    fun saveFoodDetails() {
        viewModelScope.launch {
            val status = detailsStatusState.value
            if (status is FoodDetailsStatus.Success) {
                saveFoodDetailsRepository.saveFoodDetails(
                    name = status.results.name,
                    nutritionInfo = status.results.nutritionInfo,
                )
            }
        }
    }

    fun getFoodDetails(foodId: FoodId) {
        foodDetailsRepository
            .foodDetails(foodId)
            .onEach { status -> _detailsStatusState.update { status } }
            .launchIn(
                scope = viewModelScope + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
                    throwable.printStackTrace()
                    if (throwable is CancellationException) {
                        println("********** get food details cancelled")
                    } else {
                        throw (throwable)
//                        _detailsStatusState.update {
//                            FoodDetailsStatus.Failure()
//                        }
                    }
                }
            )
    }

    fun submitSearchQuery() {
        searchRepository
            .searchWithUpcGtin(query)
            .onEach { status -> _searchStatusState.update { status } }
            .launchIn(viewModelScope + SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
                throw (throwable)
//                _searchStatusState.update { SearchStatus.Failure("Unknown external error") }
            })
    }

    companion object {
        internal fun factor(module: FeatureSearchDataModule): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchFdcViewModel(
                    foodDetailsRepository = module.provideFoodDetailsRepository(),
                    searchRepository = module.provideFoodSearchRepository(),
                    saveFoodDetailsRepository = module.provideSaveFoodDetailsRepository(),
                )
            }
        }
    }
}
