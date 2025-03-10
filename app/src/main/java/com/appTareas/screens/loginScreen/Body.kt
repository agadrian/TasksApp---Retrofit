package com.appTareas.screens.loginScreen

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appTareas.R
import com.appTareas.utils.ErrorDialog


@Composable
fun Body(
    email: String,
    password: String,
    loginScreenViewModel: LoginScreenViewModel,
    isVisiblePassword: Boolean,
    navController: NavController,
    context: Context



){

    val loginResult by loginScreenViewModel.loginResult.observeAsState()
    val showErrorDialog by loginScreenViewModel.showErrorDialog.collectAsState()



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

    ButtonLogin(
        text = "Log In",
        onClick = { loginScreenViewModel.login(navController) }
//        onClick = {
//            if (loginScreenViewModel.validateLogin(correctEmail, correctPassword)){
//                navController.navigate(
//                    route = AppScreen.WelcomeScreen.createRoute(
//                        email = email
//                    )
//                )
//                loginScreenViewModel.clearInputs() // Limpiar inputs una vez acceda con credenciales correctas
//            }else{
//                customToast(context, "Wrong email or password")
//            }
//        }
    )

    // Mostrar el error en un AlertDialog si hay un error
    if (showErrorDialog && !loginResult.isNullOrBlank()) {
        ErrorDialog(errorMessage = loginResult!!) {
            loginScreenViewModel.dismissErrorDialog()
        }
    }
}



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
 * Textfield parala contraseña
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


/**
 * Boton para hacer login. Si esta bien ambos campos, navega a otra pantalla y borra los datos de los textfields.
 */
@Composable
fun ButtonLogin(
    text: String,
    onClick: () -> Unit
){

    // He tenido que hacert un box rodeando al boton para poder hacer el borde blanco cuando tiene el foco, porque no he conseguido que la forma del borde con el foco coincida con la forma del boton. En los botones de arriba lo he podido hacer porque el borde del foco se hace respecto a un borde que tambien he creado yo, ya que el boton en sí esta en transparente.

    // He tenido que hacer focusable el box, y cuiando detecte enter, que llame al onclick para que actue el boton. No es la mejor manera de hacerlo, pero la unica que he conseguido de momento.


    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val sizeScale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "Size effect")

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 34.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { isFocused = it.isFocused }
                .focusable()

                // Para gestionar el borde del foco y el onclick al mismo tiempo.
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Enter) {
                        onClick() // Delegar acción al botón
                        true
                    } else {
                        false
                    }
                }
                .then(
                    if (isFocused){
                        Modifier
                            .border(2.dp, Color.White, RoundedCornerShape(32.dp))
                    }else{
                        Modifier

                    }
                )
        ){
            Button(
                onClick = onClick,
                //enabled = isValidEmail && isValidPassword,  --> Lo dejo siempre habilitado ya que muestro mensajes constantes de si es valido o no, ademas de no navegar a la siguiente pantalla yy mostrar un toast cuando las credenciales no son validas
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPressed){
                        colorResource(R.color.greenButtonPressed)
                    }else{
                        colorResource(R.color.green)
                    },
                    contentColor = colorResource(R.color.black)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 1.dp)
                    .graphicsLayer(
                        scaleX = sizeScale,
                        scaleY = sizeScale
                    ),
                interactionSource = interactionSource
            ) {
                Text(
                    text = text,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}