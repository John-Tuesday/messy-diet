package org.calamarfederal.messydiet.feature.measure

import android.icu.number.LocalizedNumberFormatter
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.john.tuesday.nutrition.*
import org.calamarfederal.physical.measurement.*
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.BeforeTest
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
internal class NutrientComposablesUnitTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val allFilledNutrition = NutritionSamples.filledNutritionA

    private var testNutrition by mutableStateOf(allFilledNutrition)
    private lateinit var weightFormatter: LocalizedNumberFormatter

    private val scrollContainer
        get() = composeRule
            .onRoot()
            .onChildren()
            .filterToOne(hasScrollToKeyAction())

    @BeforeTest
    fun setUp() {
        testNutrition = allFilledNutrition.mutate(portion = Portion(0.grams))

        composeRule.setContent {
            NutritionInfoColumn(nutrition = testNutrition)
            weightFormatter = simpleFormatter
        }
    }

    @Test
    fun `Portion as weight is shown`() {
        testNutrition = allFilledNutrition.mutate(portion = Portion(134.grams))
        composeRule
            .onNode(
                hasTextExactly(
                    composeRule.activity.getString(R.string.serving_size),
                    weightFormatter.format(testNutrition.portion.mass!!.inGrams()).toString(),
                    composeRule.activity.getString(R.string.gram_label)
                )
            )
            .assertExists()
    }

    @Test
    fun `Portion as volume is shown`() {
        testNutrition = allFilledNutrition.mutate(portion = Portion(134.milliliters))
        composeRule
            .onNode(
                hasTextExactly(
                    composeRule.activity.getString(R.string.serving_size),
                    weightFormatter.format(testNutrition.portion.volume!!.inMilliliters()).toString(),
                    composeRule.activity.getString(R.string.milliliter_label)
                )
            )
            .assertExists()
    }

    @Test
    fun `Food energy is shown`() {
        composeRule
            .onNode(
                hasTextExactly(
                    composeRule.activity.getString(R.string.calories),
                    weightFormatter.format(testNutrition.foodEnergy.inKilocalories()).toString(),
                    "",
                )
            )
            .assertExists()

        testNutrition = allFilledNutrition
        composeRule
            .onNode(
                hasTextExactly(
                    composeRule.activity.getString(R.string.calories),
                    weightFormatter.format(testNutrition.foodEnergy.inKilocalories()).toString(),
                    "",
                )
            )
            .assertExists()
    }

    private fun assertNutrient(label: String, mass: Mass) {
        scrollContainer
            .performScrollToNode(hasText(label))
        composeRule
            .onNodeWithText(label)
            .assertExists()
            .performScrollTo()
            .assertIsDisplayed()
            .onSiblings()
            .filterToOne(hasText(weightFormatter.format(mass.inGrams()).toString()))
    }

    @Test
    fun `Fat nutrients are shown when present`() {
        testNutrition = allFilledNutrition

        val resources = composeRule.activity.resources
        val fatStrings = resources.getStringArray(R.array.fat_names)
        val fatNutrients =
            NutrientType.entries.filter { nutrientFullName(nutrient = it, resources = resources) in fatStrings }

        for (fatNutrient in fatNutrients) {
            assertNutrient(
                label = composeRule.activity.getString(fatNutrient.stringResId),
                mass = testNutrition[fatNutrient]!!,
            )
        }
    }

    @Test
    fun `Optional Fat nutrients do not exist when null`() {
        testNutrition = testNutrition.mutate(nutritionMap = mapOf())
        composeRule.waitForIdle()

        for (fatString in composeRule.activity.resources.getStringArray(R.array.fat_names)
            .filterNot { it == composeRule.activity.getString(R.string.fat) }
        ) {
            composeRule.onRoot().printToLog("*******")
            composeRule
                .onNodeWithText(fatString)
//                .assertDoesNotExist()
                .assertIsNotDisplayed()
        }
    }

    @Test
    fun `Carbohydrate nutrients are shown when present`() {
        testNutrition = allFilledNutrition

        val resources = composeRule.activity.resources
        val carbStrings = resources.getStringArray(R.array.carbohydrate_names)
        val carbNutrients = NutrientType.entries.filter {
            nutrientFullName(nutrient = it, resources = resources) in carbStrings
        }

        for (carbohydrateNutrient in carbNutrients) {
            assertNutrient(
                label = composeRule.activity.getString(carbohydrateNutrient.stringResId),
                mass = testNutrition[carbohydrateNutrient]!!,
            )
        }
    }

    @Test
    fun `Optional Carbohydrate nutrients do not exist when null`() {
        testNutrition = testNutrition.mutate(nutritionMap = mapOf())

        for (carbohydrateString in composeRule.activity.resources.getStringArray(R.array.carbohydrate_names)
            .filterNot { it == composeRule.activity.getString(R.string.carbohydrates) }) {
            composeRule
                .onNodeWithText(carbohydrateString)
                .assertDoesNotExist()
        }
    }

    @Test
    fun `Mineral nutrients are shown when present`() {
        testNutrition = allFilledNutrition

        val resources = composeRule.activity.resources
        val mineralStrings = resources.getStringArray(R.array.mineral_names)
        val mineralNutrients = NutrientType.entries.filter {
            nutrientFullName(nutrient = it, resources = resources) in mineralStrings
        }

        for (mineralNutrient in mineralNutrients) {
            assertNutrient(
                label = composeRule.activity.getString(mineralNutrient.stringResId),
                mass = testNutrition[mineralNutrient]!!,
            )
        }
    }

    @Test
    fun `Mineral nutrients do not exist when null`() {
        testNutrition = testNutrition.mutate(nutritionMap = mapOf())

        for (mineralString in composeRule.activity.resources.getStringArray(R.array.mineral_names)) {
            composeRule
                .onNodeWithText(mineralString)
                .assertDoesNotExist()
        }
    }

    @Test
    fun `Vitamin nutrients are shown when present`() {
        testNutrition = allFilledNutrition

        val resources = composeRule.activity.resources
        val vitaminStrings = resources.getStringArray(R.array.vitamin_names)
        val vitaminNutrients = NutrientType.entries.filter {
            nutrientFullName(nutrient = it, resources = resources) in vitaminStrings
        }

        for (vitaminNutrient in vitaminNutrients) {
            assertNutrient(
                label = composeRule.activity.getString(vitaminNutrient.stringResId),
                mass = testNutrition[vitaminNutrient]!!,
            )
        }
    }

    @Test
    fun `Vitamin nutrients do not exist when null`() {
        testNutrition = testNutrition.mutate(nutritionMap = mapOf())

        for (mineralString in composeRule.activity.resources.getStringArray(R.array.vitamin_names)) {
            composeRule
                .onNodeWithText(mineralString)
                .assertDoesNotExist()
        }
    }
}
