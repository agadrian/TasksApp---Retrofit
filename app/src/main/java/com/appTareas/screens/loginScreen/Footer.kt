@file:Suppress("DEPRECATION")

package com.appTareas.screens.loginScreen

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appTareas.R
import com.appTareas.navegation.AppScreen
import com.appTareas.utils.customToast






@Composable
fun Footer(
    context: Context,
    navController: NavController
){

    var isPressedForgotPswd by remember { mutableStateOf(false) }
    var isPressedSingUp by remember { mutableStateOf(false) }


    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        ClickableTextUnderline(
            text = "Forgot your password?",
            onClick = { customToast(context, "Redirecting to forgot password") },
            onPressChangeColor = { isPressedForgotPswd = it},
            isPressed = isPressedForgotPswd,
            colorUnpressed = MaterialTheme.colorScheme.onPrimary,
            colorPressed = colorResource(R.color.green)
        )


        Spacer(Modifier.height(15.dp))

        Text(
            text = "Don't have an account?",
            fontSize = 14.sp,
            color = colorResource(R.color.gray2),
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(5.dp))


        Text(
            text = "Sing up",
            modifier = Modifier.clickable {
                navController.navigate(
                    AppScreen.RegisterScreen.route
                )
            }
        )

//        ClickableTextUnderline(
//            text = "Sing up for Spotify.",
//            onClick = { navController.navigate(
//                route = AppScreen.RegisterScreen
//            ) },
//            onPressChangeColor = { isPressedSingUp = it },
//            isPressed = isPressedSingUp,
//            colorUnpressed = MaterialTheme.colorScheme.onPrimary,
//            colorPressed = colorResource(R.color.green)
//        )

        Spacer(Modifier.height(30.dp))


        // Texto con palabras clicables para pasarselo a la funcion.
        val textTerms = buildAnnotatedString {
            append("This site is protected by reCAPTCHA and the Google ")

            // Privacy Policy clicable
            pushStringAnnotation(
                tag = "PrivacyPolicy",
                annotation = "Privacy Policy"
            )
            withStyle(
                SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    color = colorResource(R.color.gray2)
                )
            ){
                append("Privacy Policy")
            }
            pop()

            append(" and ")

            // Privacy Policy clicable
            pushStringAnnotation(
                tag = "TermsOfService",
                annotation = "Terms of Service"
            )
            withStyle(
                SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    color = colorResource(R.color.gray2)
                )
            ){
                append("Terms of Service")
            }
            pop()

            append(" apply.")

        }


        TextWithClicablesWords(context, textTerms)

    }
}


@Composable
fun ClickableTextUnderline(
    text: String,
    onClick: () -> Unit,
    onPressChangeColor: (Boolean) -> Unit,
    isPressed: Boolean,
    colorUnpressed: Color,
    colorPressed: Color
){

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }


    Text(
        text = text,
        textDecoration = TextDecoration.Underline,
        fontSize = if (isFocused) 15.sp else 14.sp,
        color = if (isPressed){
            colorPressed
        } else {
            colorUnpressed
        },
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clickable(
                onClick = onClick,
                onClickLabel = "Forgot Password"
            ).pointerInput(Unit){
                detectTapGestures (
                    onPress = {
                        onPressChangeColor(true)
                        tryAwaitRelease()

                        onPressChangeColor(false)
                    }
                )
            }
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
    )
}


@Composable
fun TextWithClicablesWords(context: Context, text: AnnotatedString){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ClickableText(
            text = text,
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                color = colorResource(R.color.gray2)
            ),

            // Para poder gestionar el click y color de un texto que este en medio de una frase
            onClick = { offset ->
                text.getStringAnnotations(
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    when(it.tag){
                        "PrivacyPolicy" -> {
                            customToast(context, "Redirecting to Privacy Policy")
                        }

                        "TermsOfService" -> {
                            customToast(context, "Redirecting to Terms of Service")
                        }
                    }
                }
            }
        )
    }
}
