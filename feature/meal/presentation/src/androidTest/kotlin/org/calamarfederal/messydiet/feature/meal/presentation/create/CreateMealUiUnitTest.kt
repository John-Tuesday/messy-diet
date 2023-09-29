package org.calamarfederal.messydiet.feature.meal.presentation.create

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.calamarfederal.messydiet.feature.meal.presentation.R
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.calamarfederal.messydiet.feature.measure.R as M

@RunWith(AndroidJUnit4::class)
internal class CreateMealUiUnitTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private var enableSaveState by mutableStateOf(false)

    @BeforeTest
    fun setUp() {
        composeRule.setContent {
            CreateMealLayout(
                state = CreateMealUiState(),
                enableSave = enableSaveState,
            )
        }
    }

    private val optionalFatStrings by lazy {
        composeRule.activity.resources.getStringArray(M.array.fat_names)
            .filterNot { it == composeRule.activity.getString(M.string.fat) }
    }
    private val fatStrings by lazy { optionalFatStrings + composeRule.activity.getString(R.string.meal_total_fats_label) }

    private val optionalCarbohydrateStrings by lazy {
        composeRule.activity.resources.getStringArray(M.array.carbohydrate_names)
            .filterNot { it == composeRule.activity.getString(M.string.carbohydrates) }
    }
    private val carbohydrateStrings by lazy { optionalCarbohydrateStrings + composeRule.activity.getString(R.string.meal_total_carbohydrates_label) }

    private val fatExpandButton
        get() = composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.expand_fat_group))
    private val carbohydrateExpandButton
        get() = composeRule.onNodeWithContentDescription(composeRule.activity.getString(R.string.expand_carbohydrate_group))

    private val totalCarbohydratesTextField
        get() = composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.meal_total_carbohydrates_label))

    private val totalFatTextField
        get() = composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.meal_total_fats_label))

    private val proteinTextField
        get() = composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.meal_protein_label))

    private val cholesterolTextField
        get() = composeRule
            .onNodeWithText(composeRule.activity.getString(M.string.cholesterol))

    private val showMoreButton
        get() = composeRule
            .onNode(
                hasAnyChild(hasText(composeRule.activity.getString(R.string.show_more))) and hasClickAction(),
                useUnmergedTree = true,
            )

    private fun hasTextIn(strings: Collection<String>): SemanticsMatcher =
        SemanticsMatcher("Matches any nodes whose text is contained in $strings") { node ->
            val text = node.config.getOrNull(SemanticsProperties.Text) ?: return@SemanticsMatcher false
            text.any { it.text in strings }
        }

    private val fatNodes
        get() = composeRule
            .onAllNodes(hasTextIn(fatStrings))
    private val optionalFatNodes
        get() = composeRule
            .onAllNodes(hasTextIn(optionalFatStrings))

    private val carbohydrateNodes
        get() = composeRule
            .onAllNodes(hasTextIn(carbohydrateStrings))
    private val optionalCarbohydrateNodes
        get() = composeRule
            .onAllNodes(hasTextIn(optionalCarbohydrateStrings))
    private val vitaminNodes
        get() = composeRule
            .onAllNodes(hasTextIn(composeRule.activity.resources.getStringArray(M.array.vitamin_names).toList()))

    private val mineralNodes
        get() = composeRule
            .onAllNodes(hasTextIn(composeRule.activity.resources.getStringArray(M.array.mineral_names).toList()))

    private val vitaminMineralNodes
        get() = composeRule
            .onAllNodes(
                hasTextIn(composeRule.activity.resources.getStringArray(M.array.mineral_names).toList()) or
                        hasTextIn(composeRule.activity.resources.getStringArray(M.array.vitamin_names).toList())
            )

    @Test
    fun `Save button matches enabled state`() {
        enableSaveState = false
        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.save))
            .assertIsDisplayed()
            .assertIsNotEnabled()

        enableSaveState = true
        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.save))
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
    }

    @Test
    fun `Name Input correctly reflects input`() {
        val testName = "new name test"

        composeRule
            .onNodeWithText(testName)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.meal_name_placeholder))
            .performTextReplacement(testName)

        composeRule
            .onNodeWithText(testName)
            .assertExists()
            .assertIsDisplayed()

        val testNameAlt = "abcd abcd 12"
        composeRule
            .onNodeWithText(testNameAlt)
            .assertDoesNotExist()
        composeRule
            .onNodeWithText(testName)
            .performTextReplacement(testNameAlt)
        composeRule
            .onNodeWithText(testNameAlt)
            .assertExists()
    }

    @Test
    fun `Portion correctly reflects input`() {
        val amountString = "1234"

        composeRule
            .onNodeWithText(amountString)
            .assertDoesNotExist()

        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.serving_size))
            .assertIsDisplayed()
            .performTextReplacement(amountString)

        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.serving_size))
            .assertTextContains(amountString)
            .assertIsDisplayed()
            .performTextClearance()

        composeRule
            .onNode(hasText(composeRule.activity.getString(R.string.serving_size)) and hasText(amountString))
            .assertDoesNotExist()
    }

    @Test
    fun `Food energy correctly reflects input`() {
        val amountString = "1234"

        composeRule
            .onNodeWithText(amountString)
            .assertDoesNotExist()

        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.food_energy_label))
            .assertIsDisplayed()
            .performTextReplacement(amountString)
        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.food_energy_label))
            .assertTextContains(amountString)
            .assertIsDisplayed()
            .performTextClearance()

        composeRule
            .onNodeWithText(amountString)
            .assertDoesNotExist()
    }

    @Test
    fun `Protein correctly reflects input`() {
        val amountString = "1234"

        composeRule
            .onNodeWithText(amountString)
            .assertDoesNotExist()

        proteinTextField
            .assertIsDisplayed()
            .performTextReplacement(amountString)
        proteinTextField
            .assertTextContains(amountString)
            .assertIsDisplayed()
            .performTextClearance()

        composeRule
            .onNodeWithText(amountString)
            .assertDoesNotExist()
    }


    @Test
    fun `Fat total correctly reflects input`() {
        val amountString = "1234"

        composeRule
            .onNodeWithText(amountString)
            .assertDoesNotExist()

        totalFatTextField
            .assertIsDisplayed()
            .performTextReplacement(amountString)
        totalFatTextField
            .assertTextContains(amountString)
            .assertIsDisplayed()
            .performTextClearance()

        composeRule
            .onNodeWithText(amountString)
            .assertDoesNotExist()
    }

    @Test
    fun `Fat group expands and hides on arrow button click`() {
        fatNodes
            .assertCountEquals(1)
            .filterToOne(hasText(composeRule.activity.getString(R.string.meal_total_fats_label)))
            .assertExists()

        fatExpandButton.performClick()
        fatNodes.assertCountEquals(fatStrings.size)

        fatExpandButton.performClick()
        fatNodes
            .assertCountEquals(1)
            .filterToOne(hasText(composeRule.activity.getString(R.string.meal_total_fats_label)))
            .assertExists()
    }

    @Test
    fun `Fat group always shows non empty optional fats`() {
        fatNodes
            .assertCountEquals(1)
            .filterToOne(hasText(composeRule.activity.getString(R.string.meal_total_fats_label)))
            .assertExists()

        for (fatString in optionalFatStrings) {
            val amountString = "123"

            composeRule
                .onNodeWithText(fatString)
                .assertDoesNotExist()

            fatExpandButton.performClick()

            composeRule
                .onNodeWithText(fatString)
                .performScrollTo()
                .performTextReplacement(amountString)

            fatExpandButton
                .performScrollTo()
                .performClick()

            composeRule
                .onNodeWithText(fatString)
                .assertTextContains(amountString)
                .performScrollTo()
                .assertIsDisplayed()

            composeRule
                .onNodeWithText(amountString)
                .performScrollTo()
                .performTextClearance()

            composeRule
                .onNodeWithText(fatString)
                .assertDoesNotExist()
        }
    }

    @Test
    fun `Carbohydrate total correctly reflects input`() {
        val amountString = "1234"

        composeRule
            .onNodeWithText(amountString)
            .assertDoesNotExist()

        totalCarbohydratesTextField
            .assertIsDisplayed()
            .performTextReplacement(amountString)
        totalCarbohydratesTextField
            .assertTextContains(amountString)
            .assertIsDisplayed()
            .performTextClearance()

        composeRule
            .onNodeWithText(amountString)
            .assertDoesNotExist()
    }

    @Test
    fun `Carbohydrate group expands and hides on arrow button click`() {
        optionalCarbohydrateNodes
            .assertCountEquals(0)

        carbohydrateExpandButton
            .performClick()

        optionalCarbohydrateNodes
            .assertCountEquals(optionalCarbohydrateStrings.size)

        carbohydrateExpandButton
            .performClick()

        optionalCarbohydrateNodes
            .assertCountEquals(0)
    }

    @Test
    fun `Carbohydrate group always shows non empty optional child`() {
        optionalCarbohydrateNodes
            .assertCountEquals(0)

        for (optionalCarbString in optionalCarbohydrateStrings) {
            val amountString = "1234"

            composeRule
                .onNodeWithText(optionalCarbString)
                .assertDoesNotExist()

            carbohydrateExpandButton
                .performClick()

            composeRule
                .onNodeWithText(optionalCarbString)
                .performScrollTo()
                .performTextReplacement(amountString)

            carbohydrateExpandButton
                .performClick()

            composeRule
                .onNodeWithText(optionalCarbString)
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .assertTextContains(amountString)

            composeRule
                .onNodeWithText(optionalCarbString)
                .performTextClearance()

            composeRule
                .onNodeWithText(optionalCarbString)
                .assertDoesNotExist()
        }
    }

    @Test
    fun `Vitamins and Minerals shows and hides with show more button`() {
        vitaminMineralNodes
            .assertCountEquals(0)

        showMoreButton
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        vitaminMineralNodes
            .assertCountEquals(
                composeRule.activity.resources.getStringArray(M.array.mineral_names).size +
                        composeRule.activity.resources.getStringArray(M.array.vitamin_names).size
            )

        showMoreButton
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        vitaminMineralNodes
            .assertCountEquals(0)
    }

    @Test
    fun `Vitamin is always shown when not null or empty`() {
        vitaminNodes
            .assertCountEquals(0)


        for (vitaminString in composeRule.activity.resources.getStringArray(M.array.vitamin_names)) {
            val amountString = "1234"

            composeRule
                .onNodeWithText(vitaminString)
                .assertDoesNotExist()

            showMoreButton
                .performClick()

            composeRule
                .onNodeWithText(vitaminString)
                .performScrollTo()
                .performTextReplacement(amountString)

            showMoreButton
                .performScrollTo()
                .performClick()

            composeRule
                .onNodeWithText(vitaminString)
                .assertTextContains(amountString)
                .performScrollTo()
                .assertIsDisplayed()

            composeRule
                .onNodeWithText(amountString)
                .performScrollTo()
                .performTextClearance()

            composeRule
                .onNodeWithText(vitaminString)
                .assertDoesNotExist()
        }
    }

    @Test
    fun `Mineral is always shown when not null or empty`() {
        mineralNodes
            .assertCountEquals(0)

        for (mineralString in composeRule.activity.resources.getStringArray(M.array.mineral_names)) {
            val amountString = "1234"

            composeRule
                .onNodeWithText(mineralString)
                .assertDoesNotExist()

            showMoreButton
                .performClick()

            composeRule
                .onNodeWithText(mineralString)
                .performScrollTo()
                .performTextReplacement(amountString)

            showMoreButton
                .performScrollTo()
                .performClick()

            composeRule
                .onNodeWithText(mineralString)
                .assertTextContains(amountString)
                .performScrollTo()
                .assertIsDisplayed()

            composeRule
                .onNodeWithText(amountString)
                .performScrollTo()
                .performTextClearance()

            composeRule
                .onNodeWithText(mineralString)
                .assertDoesNotExist()
        }
    }

    @Test
    fun `When focused text field is out of view the app does not crash`() {
        totalCarbohydratesTextField
            .performScrollTo()
            .performTextReplacement("1")

        fatExpandButton
            .performClick()

        carbohydrateExpandButton
            .performScrollTo()
            .performClick()

        cholesterolTextField
            .performScrollTo()
            .performSemanticsAction(SemanticsActions.RequestFocus)
            .assertIsFocused()

        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.meal_name_placeholder))
            .performScrollTo()

        cholesterolTextField
            .assertIsFocused()
            .assertIsNotDisplayed()
    }

    @Test
    fun `Ime action goes to the next field`() {
        val nameInput = "new name"

        fatExpandButton
            .performClick()
        carbohydrateExpandButton
            .performScrollTo()
            .performClick()
        showMoreButton
            .performScrollTo()
            .performClick()

        composeRule
            .onNodeWithText(composeRule.activity.getString(R.string.meal_name_placeholder))
            .performScrollTo()
            .performTextReplacement(nameInput)

        composeRule
            .onNodeWithText(nameInput)
            .performImeAction()

        val totalFields = composeRule.activity.resources.getStringArray(M.array.mineral_names).size +
                composeRule.activity.resources.getStringArray(M.array.vitamin_names).size +
                composeRule.activity.resources.getStringArray(M.array.carbohydrate_names).size +
                composeRule.activity.resources.getStringArray(M.array.fat_names).size +
                4 // Name, Portion, Protein, Calories

        for (index in 1..<totalFields) {
            val inputString = "1"
            composeRule
                .onNode(isFocused())
                .assert(
                    SemanticsMatcher("Focus has changed") { node ->
                        node.config.getOrNull(SemanticsProperties.Text)?.none {
                            it.text == inputString || it.text == nameInput
                        } ?: true
                    }
                )
                .assertIsDisplayed()
                .performTextReplacement(inputString)
            composeRule
                .onNode(isFocused())
                .assertTextContains(inputString)
                .performImeAction()
        }
    }
}
