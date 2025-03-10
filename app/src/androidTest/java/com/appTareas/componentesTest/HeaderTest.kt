package com.appTareas.componentesTest

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.appTareas.screens.loginScreen.DarkModeSwitch
import org.junit.Rule
import org.junit.Test


class HeaderTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myTest(){
        var isDarkMode = false

        composeTestRule.setContent {
            DarkModeSwitch(
                isDarkMode = isDarkMode,
                onDarkModeSwitchChange = { isDarkMode = it },
                modifier = Modifier
            )
        }

        // Comprobar el texto inicial
        composeTestRule.onNodeWithText("modo claro", ignoreCase = true).assertIsDisplayed()

        // Obtener el switch y copmprobar si existe
        val switch = composeTestRule.onNodeWithTag("darkModeSwitch")
        switch.assertExists()

        // Activar el switch
        switch.performClick()

        // Comprobar que ha cambiado el texto
        composeTestRule.onNodeWithText("modo oscuro", ignoreCase = true).assertIsDisplayed()


        switch.performClick()

        composeTestRule.onNodeWithText("modo claro", ignoreCase = true).assertIsDisplayed()

    }
}