package com.appTareas.screens.loginScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appTareas.R
import com.appTareas.model.SocialButton


@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    loginScreenViewModel: LoginScreenViewModel,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
){


    val context = LocalContext.current
    //val tokenManager = remember { TokenManager(context) } // Inicializar TokenManager
    //val loginViewModel = remember { MainScreenViewModel(tokenManager) } // Pasar TokenManager


    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    val socialButtons = listOf(
        SocialButton(R.drawable.google, "Continue with Google"),
        SocialButton(R.drawable.facebook, "Continue with Facebook"),
        SocialButton(
            if (isDarkMode) R.drawable.apple_dark else R.drawable.apple_claro,
            "Continue with Apple"),
    )

    val email by loginScreenViewModel.email.collectAsState()
    val password by loginScreenViewModel.password.collectAsState()
    val isVisiblePassword by loginScreenViewModel.isVisiblePassword.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .clickable { focusManager.clearFocus() } // Para quitar el foco de donde este, al clickar en el background
            .padding(bottom = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            socialButtons = socialButtons,
            context = context,
            isDarkMode = isDarkMode,
            onDarkModeSwitchChange = onDarkModeChange
        )

        Spacer(Modifier.height(25.dp))

        HorizontalDivider(
            Modifier
                .padding(horizontal = 40.dp),
            color = colorResource(R.color.gray),
            thickness = 2.dp
        )

        Spacer(Modifier.height(15.dp))

        Body(
            email = email,
            password = password,
            loginScreenViewModel = loginScreenViewModel,
            isVisiblePassword = isVisiblePassword,
            navController = navController,
            context = context
        )

        Spacer(Modifier.height(25.dp))

        Footer(context, navController)
    }
}

//@Preview
//@Composable
//fun PreviewSpotify(){
//    MainScreen(navController = rememberNavController(), Modifier, MainScreenViewModel(TokenManager(
//        LocalContext.current)), true, {})
//}