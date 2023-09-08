package org.calamarfederal.messydiet.feature.measure

import android.icu.number.LocalizedNumberFormatter
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.calamarfederal.messydiet.diet_data.model.Nutrition
import org.calamarfederal.messydiet.diet_data.model.Portion
import org.calamarfederal.messydiet.diet_data.model.kcal
import org.calamarfederal.messydiet.measure.R
import org.calamarfederal.messydiet.measure.Weight
import org.calamarfederal.messydiet.measure.grams
import org.calamarfederal.messydiet.test.UnitTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith

@Category(UnitTest::class)
@RunWith(AndroidJUnit4::class)
internal class NutrientComposablesUnitTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val allFilledNutrition = Nutrition(
        foodEnergy = 15.kcal,
        portion = Portion(16.grams),
        totalFat = 1.grams,
        monounsaturatedFat = 2.grams,
        polyunsaturatedFat = 3.grams,
        transFat = 4.grams,
        saturatedFat = 5.grams,
        omega3 = 6.grams,
        omega6 = 7.grams,
        totalCarbohydrates = 8.grams,
        sugar = 9.grams,
        fiber = 10.grams,
        sugarAlcohol = 11.grams,
        starch = 12.grams,
        totalProtein = 13.grams,
        cholesterol = 14.grams,
        vitaminC = 17.grams,
        magnesium = 18.grams,
        iron = 19.grams,
        calcium = 20.grams,
        chloride = 21.grams,
        phosphorous = 22.grams,
        potassium = 23.grams,
        sodium = 24.grams,
    )

    private var testNutrition by mutableStateOf(allFilledNutrition)
    private lateinit var weightFormatter: LocalizedNumberFormatter

    private val monounsaturatedFatLabelString get() = composeRule.activity.getString(R.string.monounsaturated_fat)
    private val polyunsaturatedFatLabelString get() = composeRule.activity.getString(R.string.polyunsaturated_fat)
    private val omega3FatLabelString get() = composeRule.activity.getString(R.string.omega3_fat)
    private val omega6FatLabelString get() = composeRule.activity.getString(R.string.omega6_fat)
    private val saturatedFatLabelString get() = composeRule.activity.getString(R.string.saturated_fat)
    private val transFatLabelString get() = composeRule.activity.getString(R.string.trans_fat)
    private val cholesterolFatLabelString get() = composeRule.activity.getString(R.string.cholesterol)
    private val fatLabelString get() = composeRule.activity.getString(R.string.fat)
    private val proteinLabelString get() = composeRule.activity.getString(R.string.protein)
    private val fiberLabelString get() = composeRule.activity.getString(R.string.fiber)
    private val sugarLabelString get() = composeRule.activity.getString(R.string.sugar)
    private val sugarAlcoholLabelString get() = composeRule.activity.getString(R.string.sugar_alcohol)
    private val starchLabelString get() = composeRule.activity.getString(R.string.starch)
    private val carbohydrateLabelString get() = composeRule.activity.getString(R.string.carbohydrates)

    @Before
    fun setUp() {
        testNutrition = Nutrition(portion = Portion(0.grams))

        composeRule.setContent {
            NutritionInfoColumn(nutrition = testNutrition)
            weightFormatter = simpleFormatter
        }
    }

    private fun assertNutrient(label: String, weight: Weight) {
        composeRule
            .onNodeWithText(label)
            .assertExists()
            .performScrollTo()
            .assertIsDisplayed()
            .onSiblings()
            .filterToOne(hasText(weightFormatter.format(weight.inGrams()).toString()))
    }

    @Test
    fun `Macro nutrients are shown and accurate`() {
        testNutrition = allFilledNutrition

        assertNutrient(fatLabelString, testNutrition.totalFat)
        assertNutrient(proteinLabelString, testNutrition.totalProtein)
        assertNutrient(carbohydrateLabelString, testNutrition.totalCarbohydrates)
    }

    @Test
    fun `Optional Fat nutrients are shown when present`() {
        testNutrition =  allFilledNutrition

        assertNutrient(monounsaturatedFatLabelString, testNutrition.monounsaturatedFat!!)
        assertNutrient(polyunsaturatedFatLabelString, testNutrition.polyunsaturatedFat!!)
        assertNutrient(omega3FatLabelString, testNutrition.omega3!!)
        assertNutrient(omega6FatLabelString, testNutrition.omega6!!)
        assertNutrient(saturatedFatLabelString, testNutrition.saturatedFat!!)
        assertNutrient(transFatLabelString, testNutrition.transFat!!)
        assertNutrient(cholesterolFatLabelString, testNutrition.cholesterol!!)
    }

    @Test
    fun `Optional Fat nutrients do not exist when null`() {
        composeRule
            .onNodeWithText(monounsaturatedFatLabelString)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText(polyunsaturatedFatLabelString)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText(omega3FatLabelString)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText(omega6FatLabelString)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText(saturatedFatLabelString)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText(transFatLabelString)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText(cholesterolFatLabelString)
            .assertDoesNotExist()
    }

    @Test
    fun `Optional Carbohydrate nutrients are shown when present`() {
        testNutrition = allFilledNutrition

        assertNutrient(fiberLabelString, testNutrition.fiber!!)
        assertNutrient(sugarLabelString, testNutrition.sugar!!)
        assertNutrient(sugarAlcoholLabelString, testNutrition.sugarAlcohol!!)
        assertNutrient(starchLabelString, testNutrition.starch!!)
    }

    @Test
    fun `Optional Carbohydrate nutrients are hidden when null`() {
        composeRule
            .onNodeWithText(fiberLabelString)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText(sugarLabelString)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText(sugarAlcoholLabelString)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText(starchLabelString)
            .assertDoesNotExist()
    }
}
