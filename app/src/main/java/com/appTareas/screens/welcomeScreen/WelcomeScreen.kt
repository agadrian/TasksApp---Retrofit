package com.appTareas.screens.welcomeScreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appTareas.R
import com.appTareas.navegation.AppScreen
import com.appTareas.utils.ErrorDialog
import com.appTareas.utils.FullScreenDataDialog
import com.appTareas.utils.TareaCard

@Composable
fun WelcomeScreen(
    navController: NavController,
    welcomeScreenViewModel: WelcomeScreenViewModel,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    //val tokenManager = remember { TokenManager(context) } // Inicializar TokenManager

//    LaunchedEffect(Unit) {
//        welcomeScreenViewModel.obtenerTareas() // Llamar API con el token
//    }


    val tareas by welcomeScreenViewModel.tareas.observeAsState(emptyList())
    val tarea by welcomeScreenViewModel.tarea.observeAsState()

    val isFullScreenDialogVisible = remember { mutableStateOf(false) }
    val isTareaCardVisible = remember { mutableStateOf(false) }


    var tareaId by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    val operationResult by welcomeScreenViewModel.operationResult.observeAsState()
    val tittleDialog by welcomeScreenViewModel.tittleDialog.observeAsState()
    val showErrorDialog by welcomeScreenViewModel.showErrorDialog.collectAsState()




    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.black))
    ) {

        Header(
            onBackClick = { navController.navigateUp() } // Volver a la pantalla anterior
        )

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(Modifier.height(15.dp))

            // Crear tarea
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))



            // Obtener tarea por ID
            OutlinedTextField(
                value = tareaId,
                onValueChange = { tareaId = it },
                label = { Text("ID de la tarea", fontSize = 17.sp) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(30.dp))

            Button(onClick = { welcomeScreenViewModel.crearTarea(titulo, descripcion) }) {
                Text("Crear Tarea", fontSize = 17.sp)
            }

            Spacer(Modifier.height(10.dp))

            Button(onClick = {
                    welcomeScreenViewModel.obtenerTareaPorId(tareaId)
                    isTareaCardVisible.value = true
                    welcomeScreenViewModel.resetTarea()
            }
            ) {
                Text("Obtener Tarea", fontSize = 17.sp)
            }

            Spacer(Modifier.height(10.dp))

            if (tarea!= null && isTareaCardVisible.value){
                TareaCard(tarea!!) {
                    isTareaCardVisible.value = false
                }
            }


            // Mostrar el diálogo
            if (isFullScreenDialogVisible.value && tareas!!.isNotEmpty()) {
                FullScreenDataDialog(
                    tareas = tareas,
                    onDismiss = {
                        isFullScreenDialogVisible.value = false
                    }
                )
            }




            // Obtener todas las tareas
            Button(
                onClick = {
                    welcomeScreenViewModel.obtenerTareas()
                    isFullScreenDialogVisible.value = true}
            ) {
                Text("Obtener Tareas", fontSize = 17.sp)
            }


            Spacer(Modifier.height(10.dp))

            // Actualizar tarea
            Button(onClick = { welcomeScreenViewModel.actualizarTarea(tareaId) }) {
                Text("Actualizar Tarea", fontSize = 17.sp)
            }


            Spacer(Modifier.height(10.dp))

            // Eliminar una tarea
            Button(onClick = { welcomeScreenViewModel.eliminarTarea(tareaId) }) {
                Text("Eliminar Tarea", fontSize = 17.sp)
            }

            Spacer(Modifier.height(10.dp))

            // Eliminar todas las tareas
            Button(onClick = { welcomeScreenViewModel.eliminarTodasLasTareas() }) {
                Text("Eliminar Todas las Tareas", fontSize = 17.sp)
            }

            Spacer(Modifier.height(10.dp))

            // Mostrar el error en un AlertDialog si hay un error
            if (showErrorDialog && !operationResult.isNullOrBlank()) {
                ErrorDialog(tittle= tittleDialog!!, errorMessage = operationResult!!) {
                    welcomeScreenViewModel.dismissErrorDialog()
                }
            }

        }



        Spacer(Modifier.height(100.dp))

        // Botón extra para usar un Toast
        ContinueButton(
            onClick = {
                navController.navigate(route = AppScreen.AdminScreen.route)
            }
        )
    }
}


@Composable
fun Header(
    onBackClick: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .testTag("test1")
                .fillMaxWidth()
                .padding(top = 30.dp, start = 30.dp, end = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                modifier = Modifier
                    .size(40.dp),
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back",
                    tint = colorResource(R.color.white),
                    modifier = Modifier
                        .size(30.dp)
                )
            }

            Text(
                text = "Welcome!",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = colorResource(R.color.white)

            )
        }
    }
}





@Composable
fun ContinueButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){

    // Crear el efecto de pulsar un botón.
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val sizeScale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "Efecto click size")


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
    ) {
        Button(
            onClick = onClick,
            enabled = true,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPressed){
                    colorResource(R.color.greenButtonPressed)
                }else{
                    colorResource(R.color.green)
                },
                disabledContainerColor = colorResource(R.color.gray),

                contentColor = colorResource(R.color.white)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(
                    scaleX = sizeScale,
                    scaleY = sizeScale
                ),
            interactionSource = interactionSource
        ) {
            Text(
                text = "Admin Zone",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}


//@Preview
//@Composable
//fun previewWelcome(){
//    WelcomeScreen(rememberNavController(), WelcomeScreenViewModel(TokenManager(context = LocalContext.current)))
//}