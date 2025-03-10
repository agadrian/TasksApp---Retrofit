package com.appTareas.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.appTareas.R
import com.appTareas.model.TareaDTO

fun customToast(context: Context, msg: String){
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

@Composable
fun ErrorDialog(tittle: String = "Error", errorMessage: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = tittle) },
        text = { Text(text = errorMessage) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}

@Composable
fun CustomOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    textStyle: TextStyle = TextStyle(color = MaterialTheme.colorScheme.onPrimary),
    focusedBorderColor: Color = MaterialTheme.colorScheme.onPrimary,
    unfocusedBorderColor: Color = colorResource(R.color.gray),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        maxLines = maxLines,
        singleLine = singleLine,
        textStyle = textStyle,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor
        ),
        modifier = modifier
            .fillMaxWidth()
    )
}


@Composable
fun FullScreenDataDialog(tareas: List<TareaDTO?>?, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Título
                Text(text = "Tareas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                Spacer(modifier = Modifier.height(8.dp))

                // Lista de tareas
                if (tareas!!.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(tareas) { tareaItem ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = "ID: ${tareaItem!!.id}", fontWeight = FontWeight.Bold)
                                    Text(text = "Título: ${tareaItem.titulo}")
                                    Text(text = "Descripción: ${tareaItem.descripcion}")
                                    Text(text = "Estado: ${tareaItem.estado}")
                                }
                            }
                        }
                    }
                }



                Spacer(modifier = Modifier.height(16.dp))

                // Botón para cerrar
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}





/**
 * Boton con efecto de click. Usado en login y registro.
 */
@Composable
fun ButtonWithEffect(
    text: String,
    onClick: () -> Unit
){

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




/**
 * Texto con cambio de color al hacer click
 */
@Composable
fun TextWithEffect(
    texto: String,
    onClick: () -> Unit,
    defaultColor: Color,
    pressedColor: Color
) {

    var textColor by remember { mutableStateOf(defaultColor) }

    // Para que se repinte la interfaz
    LaunchedEffect(defaultColor) {
        textColor = defaultColor
    }

    Text(
        text = texto,
        color = textColor,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        textColor = pressedColor
                        tryAwaitRelease()
                        textColor = defaultColor
                    },
                    onTap = { onClick() }
                )
            }
    )
}

