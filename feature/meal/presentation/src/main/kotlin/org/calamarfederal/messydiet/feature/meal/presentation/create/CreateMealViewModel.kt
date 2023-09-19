package org.calamarfederal.messydiet.feature.meal.presentation.create

import android.icu.number.LocalizedNumberFormatter
import android.icu.number.NumberFormatter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.diet_data.model.inKilocalories
import org.calamarfederal.messydiet.diet_data.model.kcal
import org.calamarfederal.messydiet.feature.meal.data.MealRepository
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import org.calamarfederal.messydiet.feature.measure.PortionInputState
import org.calamarfederal.messydiet.feature.measure.WeightInputState
import org.calamarfederal.messydiet.measure.Weight
import org.calamarfederal.messydiet.measure.WeightUnit
import org.calamarfederal.messydiet.measure.grams
import java.util.Locale
import javax.inject.Inject

class CreateMealUiState(
    val portionInput: PortionInputState = PortionInputState(initialWeightUnit = WeightUnit.Gram),

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
        Nutrition(
            fiber = fiber,
            sugar = sugar,
            sugarAlcohol = sugarAlcohol,
            starch = starch,
            totalCarbohydrates = totalCarbohydrates ?: 0.grams,
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
        Nutrition(
            monounsaturatedFat = it[0],
            polyunsaturatedFat = it[1],
            omega3 = it[2],
            omega6 = it[3],
            saturatedFat = it[4],
            transFat = it[5],
            cholesterol = it[6],
            totalFat = it[7] ?: 0.grams,
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
        Nutrition(
            calcium = it[0],
            chloride = it[1],
            iron = it[2],
            magnesium = it[3],
            phosphorous = it[4],
            potassium = it[5],
            sodium = it[6],
        )
    }

    private val vitaminFlow = combine(
        vitaminA.weightFlow,
        vitaminC.weightFlow,
    ) { vitA, vitC ->
        Nutrition(
            vitaminA = vitA,
            vitaminC = vitC,
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
        baseNutrition.copy(totalProtein = protein ?: 0.grams)
    }

    val mealFlow: Flow<Meal> = combine(
        snapshotFlow { nameInput },
        snapshotFlow { foodEnergyInput },
        nutrientFlow,
//        proteinInput.weightFlow,
//        carbohydrateFlow,
//        fatFlow,
    ) { name, foodEnergy, baseNutrition ->

        val mealNutrition = baseNutrition + Nutrition(
            foodEnergy = (foodEnergy.toDoubleOrNull() ?: 0.00).kcal,
        )
        Meal(
            name = name,
            nutritionInfo = mealNutrition,
        )
    }
}

private fun WeightInputState.setInputFromWeightOrNull(weight: Weight?, formatter: LocalizedNumberFormatter) {
    if (weight != null) setInputFromWeight(weight, formatter = formatter)
    else input = ""
}


internal fun CreateMealUiState.matchMeal(meal: Meal, locale: Locale) {
    nameInput = meal.name

    val simpleFormatter = NumberFormatter.withLocale(locale)

    portionInput.setInputFromPortion(meal.portion, simpleFormatter)

    foodEnergyInput = simpleFormatter.format(meal.foodEnergy.inKilocalories()).toString()

    proteinInput.setInputFromWeight(meal.totalProtein, simpleFormatter)

    fiberInput.setInputFromWeightOrNull(meal.fiber, simpleFormatter)
    sugarInput.setInputFromWeightOrNull(meal.sugar, simpleFormatter)
    sugarAlcoholInput.setInputFromWeightOrNull(meal.sugarAlcohol, simpleFormatter)
    starchInput.setInputFromWeightOrNull(meal.starch, simpleFormatter)
    carbohydrateTotalInput.setInputFromWeight(meal.totalCarbohydrates, simpleFormatter)

    monounsaturatedFatInput.setInputFromWeightOrNull(meal.monounsaturatedFat, simpleFormatter)
    polyunsaturatedFatInput.setInputFromWeightOrNull(meal.polyunsaturatedFat, simpleFormatter)
    omega3Input.setInputFromWeightOrNull(meal.omega3, simpleFormatter)
    omega6Input.setInputFromWeightOrNull(meal.omega6, simpleFormatter)
    saturatedFatInput.setInputFromWeightOrNull(meal.saturatedFat, simpleFormatter)
    transFatInput.setInputFromWeightOrNull(meal.transFat, simpleFormatter)
    cholesterolFatInput.setInputFromWeightOrNull(meal.cholesterol, simpleFormatter)
    fatTotalInput.setInputFromWeight(meal.totalFat, simpleFormatter)
}

@HiltViewModel
class CreateMealViewModel @Inject constructor(
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
        .mapLatest { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false,
        )

    fun saveMeal() {
        viewModelScope.launch {
            mealState.value?.let {
                val id = mealIdState.value
                mealRepo.insertMeal(meal = it.copy(id = id ?: 0L), generateId = id == null)
            }
        }
    }
}
