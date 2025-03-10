package com.appTareas.navegation

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.appTareas.model.NavItem
import com.appTareas.screens.adminScreen.AdminScreen
import com.appTareas.screens.adminScreen.AdminScreenViewModel
import com.appTareas.screens.loginScreen.LoginScreen
import com.appTareas.screens.loginScreen.LoginScreenViewModel
import com.appTareas.screens.registerScreen.RegisterScreen
import com.appTareas.screens.registerScreen.RegisterViewModel
import com.appTareas.screens.tareasScreen.TareasScreen
import com.appTareas.screens.tareasScreen.TareasScreenViewModel
import com.appTareas.screens.welcomeScreen.WelcomeScreen
import com.appTareas.screens.welcomeScreen.WelcomeScreenViewModel
import kotlinx.coroutines.selects.select
import kotlin.math.log




@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    loginScreenViewModel: LoginScreenViewModel,
    registerViewModel: RegisterViewModel,
    welcomeScreenViewModel: WelcomeScreenViewModel,
    adminViewModel: AdminScreenViewModel,
    tareasScreenViewModel: TareasScreenViewModel,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,

    ){

    NavHost(
        navController = navController,
        startDestination = AppScreen.LoginScreen.route
    ) {

        // Ruta especifica de MainScreen
        composable(
            route = AppScreen.LoginScreen.route
        ){
            LoginScreen(
                navController = navController,
                loginScreenViewModel = loginScreenViewModel,
                isDarkMode = isDarkMode,
                onDarkModeChange = onDarkModeChange
            )
        }


        // Ruta especifica de RegisterScreen
        composable(
            route = AppScreen.RegisterScreen.route
        ){
            RegisterScreen(
                navController = navController,
                registerViewModel = registerViewModel
            )
        }


        // Ruta especifica de AdminScreen
        composable(
            route = AppScreen.AdminScreen.route
        ){
            AdminScreen(
                navController = navController,
                adminScreenViewModel = adminViewModel
            )
        }


        // Ruta especifica de TareasScreen
        composable(
            route = AppScreen.TareasScreen.route
        ){
            TareasScreen(
                tareasViewModel = tareasScreenViewModel,
                navController = navController,
            )
        }


        composable(
            route = AppScreen.WelcomeScreen.route
        ){
            WelcomeScreen(
                navController = navController,
                welcomeScreenViewModel = welcomeScreenViewModel,
                modifier = modifier
            )
        }
    }

}





@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val listItems = listOf(
        NavItem(
            label = "Tasks",
            icon = Icons.Default.Task,
            route = AppScreen.TareasScreen.route
        ),
        NavItem(
            label = "Admin Zone",
            icon = Icons.Default.PersonAddAlt1,
            route = AppScreen.AdminScreen.route
        ),
    )



    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route


    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.height(80.dp)
    ) {

        listItems.forEach { navItem ->
            val isSelected = currentRoute == navItem.route
            val iconColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Gray

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != navItem.route) {
                        navController.navigate(navItem.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                        }
                    }
                },
                label = { Text(
                    text = navItem.label,
                    color = iconColor
                ) },

                icon = { Icon(
                    imageVector = navItem.icon,
                    contentDescription = "Icon",
                    tint = iconColor
                ) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent // ovalo de fondo

                ),

                modifier = Modifier
                    .background(Color.Transparent)
                    .indication(
                        interactionSource = remember { MutableInteractionSource() },
                        rememberRipple(color = Color.White)
                    )

            )
        }
    }
}



//        // Ruta especifica de WelcomeScreen
//        composable (
//            route = AppScreen.WelcomeScreen.route,
//            // Configurar el argumento que recibe
//            arguments = listOf(
//                navArgument(name = "token") {type = NavType.StringType},
//                navArgument(name = "username") {type = NavType.StringType},
//                navArgument(name = "password") {type = NavType.StringType},
//            )
//        ) {
//            // Recuperamos los argumentos para pasarselo y darle uso en la función WelcomeScreen
//
//            val token = it.arguments?.getString("token") ?: "Sin token"
//            val username = it.arguments?.getString("username") ?: "Sin username"
//            val password = it.arguments?.getString("password") ?: "Sin password"
//
//            WelcomeScreen(
//                navController = navControlador,
//                welcomeScreenViewModel = welcomeScreenViewModel,
//                modifier = modifier
//            )
//        }

