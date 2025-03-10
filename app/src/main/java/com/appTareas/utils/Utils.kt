package com.appTareas.utils

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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



@Composable
fun TareaCard(tarea: TareaDTO, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ){
            Column {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "ID: ${tarea.id}", fontWeight = FontWeight.Bold)
                        Text(text = "Título: ${tarea.titulo}")
                        Text(text = "Descripción: ${tarea.descripcion}")
                        Text(text = "Estado: ${tarea.estado}")
                    }
                }

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
