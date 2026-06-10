package com.tuapp.cineapp.ui.home

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.tuapp.cineapp.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeScreen_muestraListaDePeliculas() {
        // Since we are running in debug mode, the repository will return 6 fake movies
        composeTestRule.onAllNodesWithTag("MovieCard")
            .assertCountEquals(6)
    }

    @Test
    fun homeScreen_navega_a_detalle_al_hacer_click() {
        composeTestRule.onAllNodesWithTag("MovieCard")[0].performClick()
        composeTestRule.onNodeWithTag("DetailScreen").assertIsDisplayed()
    }
}
