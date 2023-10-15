package org.calamarfederal.messydiet.feature.meal.presentation.create

import android.icu.number.LocalizedNumberFormatter
import android.icu.number.NumberFormatter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.john.tuesday.nutrition.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.calamarfederal.messydiet.feature.meal.data.MealRepository
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import org.calamarfederal.messydiet.feature.meal.presentation.FeatureMealsPresentationModule
import org.calamarfederal.messydiet.feature.measure.PortionInputState
import org.calamarfederal.messydiet.feature.measure.WeightInputState
import org.calamarfederal.physical.measurement.Mass
import org.calamarfederal.physical.measurement.MassUnit
import org.calamarfederal.physical.measurement.inKilocalories
import org.calamarfederal.physical.measurement.kilocalories
import java.util.Locale

class CreateMealUiState(
    val portionInput: PortionInputState = PortionInputState(initialWeightUnit = MassUnit.Gram),

    val proteinInput: WeightInputState = WeightInputState(),

    val fiberInput: WeightInputState = WeightInputState(),
    val sugarInput: WeightInputState = WeightInputState(),
    val sugarAlcoholInput: WeightInputState = WeightInputState(),
    val starchInput: WeightInputState = WeightInputState(),
    val carbohydrateTotalInput: WeightInputState = WeightInputState(),

    val monounsaturatedFatInput: WeightInputState = WeightInputState(),
    val polyunsaturatedFatInput: WeightInputState = WeightInputState(),
    val omega3Input: WeightInputState = WeightInputState(),
    val omega6Input: WeightInputState = WeightInputState(),
    val saturatedFatInput: WeightInputState = WeightInputState(),
    val transFatInput: WeightInputState = WeightInputState(),
    val cholesterolFatInput: WeightInputState = WeightInputState(),
    val fatTotalInput: WeightInputState = WeightInputState(),

    val calciumInput: WeightInputState = WeightInputState(),
    val chlorideInput: WeightInputState = WeightInputState(),
    val ironInput: WeightInputState = WeightInputState(),
    val magnesiumInput: WeightInputState = WeightInputState(),
    val phosphorousInput: WeightInputState = WeightInputState(),
    val potassiumInput: WeightInputState = WeightInputState(),
    val sodiumInput: WeightInputState = WeightInputState(),

    val vitaminA: WeightInputState = WeightInputState(),
    val vitaminC: WeightInputState = WeightInputState(),

    initialName: String = "",
    initialFoodEnergy: String = "",
) {
    var nameInput by mutableStateOf(initialName)

    var foodEnergyInput by mutableStateOf(initialFoodEnergy)

    private val carbohydrateFlow = combine(
        fiberInput.weightFlow,
        sugarInput.weightFlow,
        sugarAlcoholInput.weightFlow,
        starchInput.weightFlow,
        carbohydrateTotalInput.weightFlow,
    ) { fiber, sugar, sugarAlcohol, starch, totalCarbohydrates ->
        mapOf(
            NutrientType.Fiber to fiber,
            NutrientType.Sugar to sugar,
            NutrientType.SugarAlcohol to sugarAlcohol,
            NutrientType.Starch to starch,
            NutrientType.TotalCarbohydrate to totalCarbohydrates,
        )
    }

    private val fatFlow = combine(
        monounsaturatedFatInput.weightFlow,
        polyunsaturatedFatInput.weightFlow,
        omega3Input.weightFlow,
        omega6Input.weightFlow,
        saturatedFatInput.weightFlow,
        transFatInput.weightFlow,
        cholesterolFatInput.weightFlow,
        fatTotalInput.weightFlow,
    ) {
        mapOf(
            NutrientType.MonounsaturatedFat to it[0],
            NutrientType.PolyunsaturatedFat to it[1],
            NutrientType.Omega3 to it[2],
            NutrientType.Omega6 to it[3],
            NutrientType.SaturatedFat to it[4],
            NutrientType.TransFat to it[5],
            NutrientType.Cholesterol to it[6],
            NutrientType.TotalFat to it[7],
        )
    }

    private val mineralFlow = combine(
        calciumInput.weightFlow,
        chlorideInput.weightFlow,
        ironInput.weightFlow,
        magnesiumInput.weightFlow,
        phosphorousInput.weightFlow,
        potassiumInput.weightFlow,
        sodiumInput.weightFlow,
    ) {
        mapOf(
            NutrientType.Calcium to it[0],
            NutrientType.Chloride to it[1],
            NutrientType.Iron to it[2],
            NutrientType.Magnesium to it[3],
            NutrientType.Phosphorous to it[4],
            NutrientType.Potassium to it[5],
            NutrientType.Sodium to it[6],
        )
    }

    private val vitaminFlow = combine(
        vitaminA.weightFlow,
        vitaminC.weightFlow,
    ) { vitA, vitC ->
        mapOf(
            NutrientType.VitaminA to vitA,
            NutrientType.VitaminC to vitC,
        )
    }

    private val nutrientFlow = combine(
        carbohydrateFlow,
        fatFlow,
        mineralFlow,
        vitaminFlow,
    ) { carb, fat, min, vit ->
        carb + fat + min + vit
    }.combine(proteinInput.weightFlow) { baseNutrition, protein ->
        baseNutrition + (NutrientType.Protein to protein)
    }

    val mealFlow: Flow<Meal?> = combine(
        snapshotFlow { nameInput },
        snapshotFlow { foodEnergyInput },
        nutrientFlow,
        portionInput.portionFlow,
    ) { name, foodEnergy, baseNutrition, portion ->

        val mealNutrition = FoodNutrition(
            foodEnergy = (foodEnergy.toDoubleOrNull() ?: 0.00).kilocalories,
            portion = portion ?: return@combine null,
            nutritionMap = baseNutrition.mapNotNull { (type, mass) -> mass?.let { type to mass } }.toMap(),
        )
        Meal(
            name = name,
            foodNutrition = mealNutrition,
        )
    }
}

private fun WeightInputState.setInputFromWeightOrNull(weight: Mass?, formatter: LocalizedNumberFormatter) {
    if (weight != null) setInputFromWeight(weight, formatter = formatter)
    else input = ""
}


internal fun CreateMealUiState.matchMeal(meal: Meal, locale: Locale) {
    nameInput = meal.name

    val simpleFormatter = NumberFormatter.withLocale(locale)

    val foodNutrition = meal.foodNutrition
    portionInput.setInputFromPortion(foodNutrition.portion, simpleFormatter)

    foodEnergyInput = simpleFormatter.format(foodNutrition.foodEnergy.inKilocalories()).toString()

    proteinInput.setInputFromWeightOrNull(foodNutrition.protein, simpleFormatter)

    fiberInput.setInputFromWeightOrNull(foodNutrition.fiber, simpleFormatter)
    sugarInput.setInputFromWeightOrNull(foodNutrition.sugar, simpleFormatter)
    sugarAlcoholInput.setInputFromWeightOrNull(foodNutrition.sugarAlcohol, simpleFormatter)
    starchInput.setInputFromWeightOrNull(foodNutrition.starch, simpleFormatter)
    carbohydrateTotalInput.setInputFromWeightOrNull(foodNutrition.totalCarbohydrate, simpleFormatter)

    monounsaturatedFatInput.setInputFromWeightOrNull(foodNutrition.monounsaturatedFat, simpleFormatter)
    polyunsaturatedFatInput.setInputFromWeightOrNull(foodNutrition.polyunsaturatedFat, simpleFormatter)
    omega3Input.setInputFromWeightOrNull(foodNutrition.omega3, simpleFormatter)
    omega6Input.setInputFromWeightOrNull(foodNutrition.omega6, simpleFormatter)
    saturatedFatInput.setInputFromWeightOrNull(foodNutrition.saturatedFat, simpleFormatter)
    transFatInput.setInputFromWeightOrNull(foodNutrition.transFat, simpleFormatter)
    cholesterolFatInput.setInputFromWeightOrNull(foodNutrition.cholesterol, simpleFormatter)
    fatTotalInput.setInputFromWeightOrNull(foodNutrition.totalFat, simpleFormatter)
}

class CreateMealViewModel(
    private val mealRepo: MealRepository,
) : ViewModel() {
    val uiState = CreateMealUiState()
    private val mealIdState = MutableStateFlow<Long?>(null)

    fun setMealId(id: Long, locale: Locale) {
        mealIdState.update { id }
        viewModelScope.launch {
            val meal = mealRepo.getMeal(id).first()
            if (meal != null)
                uiState.matchMeal(meal, locale)
        }
    }

    private val mealState: StateFlow<Meal?> = uiState.mealFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val enableSaveState: StateFlow<Boolean> = mealState
        .mapLatest { it != null && mealRepo.isMealValidUpsert(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    fun saveMeal() {
        viewModelScope.launch {
            mealState.value?.let {
                val id = mealIdState.value
                val meal = it.copy(id = id ?: 0L)
                mealRepo.insertMeal(meal = meal, generateId = id == null)
            }
        }
    }

    companion object {
        internal fun factory(module: FeatureMealsPresentationModule): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    CreateMealViewModel(module.provideMealRepository())
                }
            }
        }
    }
}
