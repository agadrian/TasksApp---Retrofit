package com.appTareas.navegation

sealed class AppScreen(
    val route: String
) {
    object LoginScreen: AppScreen("LoginScreen")
    object RegisterScreen: AppScreen("RegisterScreen")
    object AdminScreen: AppScreen("AdminScreen")
    object TareasScreen: AppScreen("TareasScreen")
    object WelcomeScreen : AppScreen("WelcomeScreen")
}