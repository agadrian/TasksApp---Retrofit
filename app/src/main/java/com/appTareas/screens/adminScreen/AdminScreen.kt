package com.appTareas.screens.adminScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appTareas.model.CreateTareaDTO
import com.appTareas.utils.ErrorDialog
import com.appTareas.utils.FullScreenDataDialog

@Composable
fun AdminScreen(
    adminScreenViewModel: AdminScreenViewModel
) {

    val tareas by adminScreenViewModel.tareas.observeAsState(emptyList())
    val isFullScreenDialogVisible = remember { mutableStateOf(false) }


    val operationResult by adminScreenViewModel.operationResult.observeAsState()
    val tittleDialog by adminScreenViewModel.tittleDialog.observeAsState()
    val showErrorDialog by adminScreenViewModel.showErrorDialog.collectAsState()

    var tareaId by remember { mutableStateOf("") }
    var usuarioId by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Box(
        Modifier.fillMaxSize()
            .padding(top = 40.dp, bottom = 95.dp)
    ) {

        Column(
            Modifier
                //.background(Color.Black)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 40.dp)

        ) {
            Header()

            Spacer(Modifier.height(8.dp))

            // Id usuario
            OutlinedTextField(
                value = usuarioId,
                onValueChange = { usuarioId = it },
                label = { Text("User ID") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Id tarea
            OutlinedTextField(
                value = tareaId,
                onValueChange = { tareaId = it },
                label = { Text("Task ID") },
                modifier = Modifier.fillMaxWidth()

            )
            Spacer(Modifier.height(8.dp))


            // Titulo tarea
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Tittle") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Descripcion tarea
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(Modifier.height(25.dp))

            // Obtener todas las tareas del sistema
            Button(
                onClick = {
                    adminScreenViewModel.obtenerTodasLasTareas()
                    isFullScreenDialogVisible.value = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Get all system Tasks", fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(20.dp))


            // Obtener todas las tareas de un usuario
            Button(
                onClick = {

                    adminScreenViewModel.obtenerTareasDeUsuario(usuarioId)
                    isFullScreenDialogVisible.value = true
                    adminScreenViewModel.resetTareas()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Get all user tasks (User ID required)",
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            }


            Spacer(Modifier.height(20.dp))


            // Crear tarea usuario
            Button(
                onClick = {
                    adminScreenViewModel.crearTareaParaUsuario(
                        usuarioId,
                        CreateTareaDTO(
                            titulo = titulo,
                            descripcion = descripcion,
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Create new Task for User (User ID, title and description required)",
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            }


            Spacer(Modifier.height(20.dp))

            // Marcar tarea como hecha de un usuario
            Button(
                onClick = {
                    adminScreenViewModel.marcarTareaComoHecha(tareaId, usuarioId)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Update user Task (User ID and Task ID required)",
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(20.dp))


            // Eliminar tarea de un usuario
            Button(
                onClick = {
                    adminScreenViewModel.eliminarTareaDeUsuario(tareaId, usuarioId)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Delete user Task (User ID and Task ID required)",
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            }


            Spacer(Modifier.height(20.dp))

            // Eliminar todas las tareas de un usuario
            Button(
                onClick = {
                    adminScreenViewModel.eliminarTodasLasTareasDeUsuario(usuarioId)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Delete all user Tasks (User ID required)",
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center
                )
            }


            // MOSTRAR DIALOGO TAREAS
            if (isFullScreenDialogVisible.value && tareas?.isNotEmpty() == true) {
                FullScreenDataDialog(
                    tareas = tareas,
                    onDismiss = {
                        isFullScreenDialogVisible.value = false
                    }
                )
            }

            // Dialogo
            if (showErrorDialog && !operationResult.isNullOrBlank()) {
                ErrorDialog(tittle = tittleDialog!!, errorMessage = operationResult!!) {
                    adminScreenViewModel.dismissErrorDialog()
                }
            }

        }
    }
}




@Composable
fun Header(
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .testTag("Admin")
                .fillMaxWidth()
                .padding(top = 30.dp, start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {


            Text(
                text = "Admin Zone - Tasks",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary

            )
        }
    }
}