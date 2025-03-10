package com.appTareas.screens.loginScreen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appTareas.R
import com.appTareas.model.SocialButton
import com.appTareas.utils.customToast

@Composable
fun Header(
    socialButtons: List<SocialButton>,
    context: Context,
    isDarkMode: Boolean,
    onDarkModeSwitchChange: (Boolean) -> Unit,
){


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 10.dp, start = 40.dp, end = 40.dp)
    ){

//        Image(
//            painter = if (isDarkMode){
//                painterResource(R.drawable.logo_dark)
//            }else{
//                painterResource(R.drawable.logo_claro)
//            }
//            ,
//            contentDescription = "Logo spotify",
//            modifier = Modifier
//                .align(Alignment.Center)
//                .size(32.dp)
//        )

        DarkModeSwitch(
            isDarkMode = isDarkMode,
            onDarkModeSwitchChange = onDarkModeSwitchChange,
            modifier = Modifier.align(Alignment.CenterEnd)
        )

    }


    Text(
        text = "Log in",
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 30.sp
    )

    Spacer(Modifier.height(30.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
        ,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        socialButtons.forEach { button ->
            ButtonContinueWith(
                image = button.img,
                text = button.text,
                onClick = {
                    customToast(context, "Redirecting to ${button.text.split(" ").last()}...") // Tomar la ultima palabra del texto
                }
            )
        }
    }
}


/**
 * Extra añadido. Switch para cambiar entre modo oscuro y claro
 */

@Composable
fun DarkModeSwitch(
    isDarkMode: Boolean,
    onDarkModeSwitchChange: (Boolean) -> Unit,
    modifier: Modifier
){
    Column(
        modifier = modifier
    ){

        Text(
            text = if (isDarkMode) "Modo claro" else "Modo oscuro",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 12.sp
        )

        Switch(
            checked = isDarkMode,
            onCheckedChange = { onDarkModeSwitchChange(it) },
            Modifier
                .align(Alignment.CenterHorizontally)
                .scale(0.8f)
                .testTag("darkModeSwitch"),
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorResource(R.color.white),
                uncheckedThumbColor = colorResource(R.color.gray),
                checkedTrackColor = colorResource(R.color.green),
                uncheckedTrackColor = colorResource(R.color.gray2),
            )
        )
    }
}


/**
 * Boton continuar con.. redes sociales
 */
@Composable
fun ButtonContinueWith(
    image: Int,
    text: String,
    onClick: () -> Unit
){

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    val borderColor = MaterialTheme.colorScheme.onPrimary

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .border(0.5.dp, borderColor, RoundedCornerShape(32.dp))
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }

            .drawBehind { // Dibujar borde por debajo cuando tiene el foco  )
                if (isFocused) {
                    val borderThickness = 2.dp.toPx() // Grosor del borde externo
                    val distance = 3.dp.toPx() // Distancia entre el borde interno y externo
                    val totalDistance = borderThickness / 2 + distance // Desplazamiento total
                    val cornerRadius = 32.dp.toPx() // Radio de las esquinas redondeadas

                    // Dibujar borde externo
                    drawRoundRect(
                        color = borderColor,
                        size = size.copy(
                            width = size.width + totalDistance * 2,
                            height = size.height + totalDistance * 2
                        ),
                        topLeft = Offset(-totalDistance, -totalDistance),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                        style = Stroke(width = borderThickness)
                    )
                }
            }
    ){
        Row(
            Modifier
                .fillMaxWidth()
                .focusable(false),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = "Logo Google",
                modifier = Modifier
                    .size(24.dp)
                    .focusable(false)
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 32.dp)
                    .focusable(false)
            )
        }
    }
}