package com.appTareas.screens.loginScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appTareas.R
import com.appTareas.utils.ButtonWithEffect
import com.appTareas.utils.ErrorDialog


@Composable
fun Body(
    email: String,
    password: String,
    loginScreenViewModel: LoginScreenViewModel,
    isVisiblePassword: Boolean,
    navController: NavController,
    loginResult: String?,
    showErrorDialog: Boolean
){

    Email(
        email = email,
        emailChange = {
            loginScreenViewModel.onEmailChange(it)
        },
    )

    Spacer(Modifier.height(15.dp))

    Password(
        password = password,
        passwordChange = {
            loginScreenViewModel.onPasswordChange(it)
        },
        passwordVisible = isVisiblePassword,
        passwordVisibleChange = { loginScreenViewModel.onPasswordVisibleChange() },

    )

    Spacer(Modifier.height(15.dp))

    ButtonWithEffect(
        text = "Log In",
        onClick = { loginScreenViewModel.login(navController) }
    )


    // Mostrar el error en un AlertDialog si hay un error
    if (showErrorDialog && !loginResult.isNullOrBlank()) {
        ErrorDialog(errorMessage = loginResult!!) {
            loginScreenViewModel.dismissErrorDialog()
        }
    }
}


/**
 * Funcion para el campo email
 */
@Composable
fun Email(
    email: String,
    emailChange: (String) -> Unit,
){
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = "Username",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(5.dp))

        OutlinedTextField(
            value = email,
            onValueChange = emailChange,
            placeholder = { Text("Username") },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onPrimary
            ),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = colorResource(R.color.gray)
                ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused = it.isFocused }
        )

        // Texto para indicar que el email no es valido
        if (isFocused && email.isEmpty()){
            Text(
                text = "Please enter a valid username",
                fontSize = 13.sp,
                color = colorResource(R.color.red)
            )
        }

    }

}


/**
 * Textfield para la contraseña
 */
@Composable
fun Password(
    password: String,
    passwordChange: (String) -> Unit,
    passwordVisible: Boolean,
    passwordVisibleChange: () -> Unit
){

    val focusRequester = remember { FocusRequester() }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    var isEyeFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Password",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(5.dp))

        OutlinedTextField(
            value = password,
            onValueChange = passwordChange,
            placeholder = { Text("Password") },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onPrimary
            ),
            trailingIcon = {
                val image = if(passwordVisible){
                    Icons.Filled.VisibilityOff
                }else{
                    Icons.Filled.Visibility
                }


                IconButton(
                    onClick = passwordVisibleChange,
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged { isEyeFocused = it.isFocused }
                ) {
                    Icon(
                        imageVector = image,
                        contentDescription = "See password",
                        tint = if (isEyeFocused) colorResource(R.color.white) else colorResource(R.color.gray)
                    )
                }
            },

            // Cambio del ojito para ver/ocultar la contraseña
            visualTransformation = if(passwordVisible){
                VisualTransformation.None
            }else{
                PasswordVisualTransformation()
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { isTextFieldFocused = it.isFocused },

            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = colorResource(R.color.gray)
                )
        )

        // Texto para indicar que la contraseña no es valida
        if (isTextFieldFocused && password.isEmpty()){
            Text(
                text = "Please enter a valid password (8+ characters)",
                fontSize = 13.sp,
                color = colorResource(R.color.red)
            )
        }
    }
}
