package org.calamarfederal.messydiet.feature.meal.presentation.view

import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.calamarfederal.messydiet.feature.meal.data.model.Meal
import org.calamarfederal.messydiet.screenshot.saveScreenshot
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test


object ScreenshotContent {
    val meals = listOf(
        Meal(id = 1, name = "alpha"),
        Meal(id = 2, name = "BETA"),
        Meal(id = 3, name = "chArlie"),
    )
}

@RunWith(AndroidJUnit4::class)
@Ignore("Should move modules")
class ViewAllMealUiScreenshot {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @BeforeTest
    fun setUp() {
        composeRule.setContent {
            ViewAllMealsLayout(
                meals = ScreenshotContent.meals,
            )
        }
    }

    @Test
    fun `default screenshot`() {
        val bmp = composeRule
            .onRoot()
            .captureToImage()

        saveScreenshot(
            "ViewAllMealUiScreenshot.png",
            bmp.asAndroidBitmap(),
        )
    }
}
