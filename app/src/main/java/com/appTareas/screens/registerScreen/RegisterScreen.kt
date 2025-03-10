package com.appTareas.screens.registerScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appTareas.utils.CustomOutlinedTextField
import com.appTareas.utils.ErrorDialog


@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel
) {

    val username by registerViewModel.username.collectAsState()
    val email by registerViewModel.email.collectAsState()
    val password by registerViewModel.password.collectAsState()
    val passwordRepeat by registerViewModel.passwordRepeat.collectAsState()
    val direccion by registerViewModel.direccion.collectAsState()

    // Los valores de cada campo de dirección
    val calle = direccion.calle
    val num = direccion.num
    val municipio = direccion.municipio
    val provincia = direccion.provincia
    val cp = direccion.cp

    // Observamos el evento de navegación
    val loginResult by registerViewModel.loginResult.observeAsState()
    val showErrorDialog by registerViewModel.showErrorDialog.collectAsState()




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        // Si el evento de navegación es "success", navegamos a la pantalla de bienvenida
//        LaunchedEffect(navigationEvent) {
//            navigationEvent?.let {
//                if (it == "success") {
//                    // Navegar a WelcomeScreen, pasando el token o el resultado que necesites
//                    navController.navigate(AppScreen.WelcomeScreen.createRoute(loginResult))
//                    registerViewModel.resetNavigationEvent() // Reiniciar el evento después de consumirlo
//                }
//            }
//        }

        Text(
            text = "Sing Up",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        CustomOutlinedTextField(
            value = username,
            onValueChange = { registerViewModel.onUsernameChange(it) },
            placeholder = "Username"
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = email,
            onValueChange = { registerViewModel.onEmailChange(it) },
            placeholder = "Email"
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = password,
            onValueChange = { registerViewModel.onPasswordChange(it) },
            placeholder = "Password",
            singleLine = false,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = passwordRepeat,
            onValueChange = { registerViewModel.onPasswordRepeatChange(it) },
            placeholder = "Repeat Password",
            singleLine = false,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dirección
        CustomOutlinedTextField(
            value = calle,
            onValueChange = { registerViewModel.onCalleChange(it) },
            placeholder = "Street",
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = num,
            onValueChange = { registerViewModel.onNumChange(it) },
            placeholder = "Number",
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = municipio,
            onValueChange = { registerViewModel.onMunicipioChange(it) },
            placeholder = "Municipio",
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = provincia,
            onValueChange = { registerViewModel.onProvinciaChange(it) },
            placeholder = "Province",
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = cp,
            onValueChange = { registerViewModel.onCpChange(it) },
            placeholder = "Postal Code",
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = {
                registerViewModel.register(navController)
            }
        ) {
            Text("Register")
        }

        // Mostrar el error en un AlertDialog si hay un error
        if (showErrorDialog && !loginResult.isNullOrBlank()) {
            ErrorDialog(errorMessage = loginResult!!) {
                registerViewModel.dismissErrorDialog()
            }
        }
    }
}

