package com.appTareas.screens.tareasScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appTareas.utils.ErrorDialog


@Composable
fun TareasScreen(
    tareasViewModel: TareasScreenViewModel
) {

    val tasks by tareasViewModel.tasks.collectAsState()
    val errorMessage by tareasViewModel.errorMessage.collectAsState()

    val titulo by tareasViewModel.titulo.collectAsState()
    val descripcion by tareasViewModel.descripcion.collectAsState()
    val showDialog by tareasViewModel.showDialog.collectAsState()

    val tareasResult by tareasViewModel.tareasResult.observeAsState()
    val showErrorDialog by tareasViewModel.showErrorDialog.collectAsState()




    Box(
        modifier = Modifier
            .fillMaxSize()

    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, start = 30.dp, end = 30.dp, bottom = 80.dp)

        ) {

            Text(
                text = "My Tasks",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary

            )

            Spacer(Modifier.height(40.dp))

            // Si hay algún error, lo mostramos
            if (!errorMessage.isNullOrEmpty()) {
                Text(errorMessage!!, color = Color.Red, fontWeight = FontWeight.Bold)
            }

            // Mostrar el error en un AlertDialog si hay un error
            if (showErrorDialog && !tareasResult.isNullOrBlank()) {
                ErrorDialog(errorMessage = tareasResult!!) {
                    tareasViewModel.dismissErrorDialog()
                }
            }

            // Mostrar la lista de tareas
            LazyColumn {
                items(tasks) { task ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Checkbox para marcar la tarea como completada
                        Checkbox(
                            checked = task.estado == "COMPLETADA",
                            onCheckedChange = {
                                tareasViewModel.updateTaskState(
                                    task.id ?: ""
                                )
                            }
                        )
                        // Mostrar la información de la tarea
                        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                            Text(task.titulo, fontWeight = FontWeight.Bold)
                            Text(task.descripcion)
                        }
                        // Botón para eliminar la tarea
                        IconButton(onClick = { task.id?.let { tareasViewModel.deleteTask(it) } }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }


            }
        }

        // Boton flotante para añadir tarea
        FloatingActionButton(
            onClick = { tareasViewModel.toggleDialog() },
            modifier = Modifier
                .padding(bottom = 120.dp)
                .align(Alignment.BottomEnd),
            containerColor = MaterialTheme.colorScheme.primary

        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Task")
        }


        // Mostrar el cuadro de diálogo para agregar la tarea
        if (showDialog) {
            AddTaskDialog(
                onDismiss = { tareasViewModel.closeDialog() },
                onSave = { tareasViewModel.createTask() },
                titulo = titulo,
                descripcion = descripcion,
                onTituloChange = { tareasViewModel.onTituloChange(it) },
                onDescripcionChange = { tareasViewModel.onDescripcionChange(it) }
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    titulo: String,
    descripcion: String,
    onTituloChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Agregar Tarea") },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        text = {
            Column {
                TextField(
                    value = titulo,
                    onValueChange = onTituloChange,
                    label = { Text(
                        text = "Título de la tarea",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp
                    ) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = descripcion,
                    onValueChange = onDescripcionChange,
                    label = { Text(
                        text = "Descripción de la tarea",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp
                    ) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                // Llamamos a la función onSave para guardar los datos
                if (titulo.isNotBlank() && descripcion.isNotBlank()){
                    onSave()
                }

            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}