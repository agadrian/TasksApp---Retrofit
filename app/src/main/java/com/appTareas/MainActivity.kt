package com.appTareas

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.appTareas.navegation.AppNavigation
import com.appTareas.navegation.AppScreen
import com.appTareas.navegation.BottomNavigationBar
import com.appTareas.navegation.TokenManager
import com.appTareas.screens.adminScreen.AdminScreenViewModel
import com.appTareas.screens.adminScreen.AdminScreenViewModelFactory
import com.appTareas.screens.loginScreen.LoginScreenViewModel
import com.appTareas.screens.loginScreen.LoginScreenViewModelFactory
import com.appTareas.screens.registerScreen.RegisterViewModel
import com.appTareas.screens.tareasScreen.TareasScreenViewModel
import com.appTareas.screens.tareasScreen.TareasViewModelFactory

import com.appTareas.ui.theme.Ej2_LoginSpotifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var isDarkMode by rememberSaveable { mutableStateOf(true) }

            // Compobar si esta logeado o no para mostrar o no la bottom bar
            var isLogged by rememberSaveable { mutableStateOf(false) }
            val tokenManager = TokenManager(LocalContext.current.applicationContext as Application)
            isLogged = tokenManager.getToken() != null



            Ej2_LoginSpotifyTheme(darkTheme = isDarkMode) {

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (isLogged){
                            val currentDestination = navBackStackEntry?.destination?.route
                            val shouldDisplayBottomBar = when (currentDestination) {
                                AppScreen.TareasScreen.route,
                                AppScreen.WelcomeScreen.route,
                                AppScreen.AdminScreen.route -> true
                                else -> false
                            }
                            if (shouldDisplayBottomBar) {
                                BottomNavigationBar(navController = navController)
                            }
                        }
                    }

                ) { innerPadding ->

                    val application = LocalContext.current.applicationContext as Application

                    // Viewmodels
                    val loginScreenViewModel: LoginScreenViewModel = viewModel(factory = LoginScreenViewModelFactory(application))
                    val registerViewModel: RegisterViewModel = viewModel()
                    val adminScreenViewModel: AdminScreenViewModel = viewModel(
                        factory = AdminScreenViewModelFactory(application)
                    )
                    val tareasScreenViewModel: TareasScreenViewModel = viewModel(
                        factory = TareasViewModelFactory(application)
                    )

                    AppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        loginScreenViewModel = loginScreenViewModel,
                        registerViewModel = registerViewModel,
                        adminViewModel = adminScreenViewModel,
                        tareasScreenViewModel = tareasScreenViewModel,
                        isDarkMode = isDarkMode,
                        onDarkModeChange = {
                            isDarkMode = it
                        }
                    )
                }
            }
        }
    }
}








