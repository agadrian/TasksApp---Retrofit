package com.appTareas.screens.adminScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appTareas.model.CreateTareaDTO
import com.appTareas.model.TareaDTO
import com.appTareas.navegation.TokenManager
import com.appTareas.retrofit.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class AdminScreenViewModel(
    private val tokenManager: TokenManager
): ViewModel() {

    private var _tareas = MutableLiveData<List<TareaDTO?>?>(emptyList())
    val tareas: MutableLiveData<List<TareaDTO?>?> get() = _tareas


    private val _mensajeError = MutableLiveData<String?>()
    val mensajeError: LiveData<String?> get() = _mensajeError



    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog

    private val _operationResult = MutableLiveData<String?>()
    val operationResult: LiveData<String?> = _operationResult

    private val _tittleDialog = MutableLiveData<String?>()
    val tittleDialog: LiveData<String?> = _tittleDialog

    fun obtenerTodasLasTareas() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()!!
                val response = RetrofitClient.apiService.obtenerTodasLasTareas(token)
                Log.d("AdminScreenViewModel", "Enviando token: $token")

                if (response.isSuccessful){
                    _tareas.value = response.body()
                    Log.d("AdminScreenViewModel", "Succesful response\n ${_tareas.value}")
                }else{
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)
                    Log.e("AdminScreenViewModel", "Error ${response.code()}: $errorBodyString")

                    showDialog("Error", errorMessage)
                }

            } catch (e: Exception) {
                _mensajeError.value = "Error al obtener todas las tareas: ${e.message}"
                showDialog("Error", mensajeError.toString())
            }
        }
    }


    fun obtenerTareasDeUsuario(usuarioId: String) {
        viewModelScope.launch {
            try {

                if (usuarioId.isBlank()){
                    showDialog("Error", "Id de usuario requerida")
                    return@launch
                }

                val token = tokenManager.getToken()!!
                val response = RetrofitClient.apiService.obtenerTareasDeUsuario(token, usuarioId)


                if (response.isSuccessful) {
                    _tareas.value = response.body()
                }else{
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)
                    Log.e("AdminScreenViewModel", "Error ${response.code()}: $errorBodyString")

                    showDialog("Error", errorMessage)
                }


            } catch (e: Exception) {
                _mensajeError.value = "Error al obtener tareas del usuario: ${e.message}"
                showDialog("Error", mensajeError.toString())
            }
        }
    }


    fun crearTareaParaUsuario(usuarioId: String, tarea: CreateTareaDTO) {
        viewModelScope.launch {
            try {
                if (usuarioId.isBlank()){
                    showDialog("Error", "Id de usuario requerida")
                    return@launch
                }

                if (tarea.titulo.isBlank() || tarea.descripcion.isBlank()){
                    showDialog("Error", "Titulo y descripcion requerida")
                    return@launch
                }


                val token = tokenManager.getToken()!!
                val response = RetrofitClient.apiService.crearTareaParaUsuario(token, usuarioId ,tarea)

                if (response.isSuccessful) {
                    showDialog("Éxito", "Tarea creada correctamente")
                }else{
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)
                    Log.e("AdminScreenViewModel", "Error ${response.code()}: $errorBodyString")

                    showDialog("Error", errorMessage)
                }

            } catch (e: Exception) {
                _mensajeError.value = "Error al crear tarea: ${e.message}"
            }
        }
    }

    fun marcarTareaComoHecha(tareaId: String, usuarioId: String) {
        viewModelScope.launch {
            try {

                if (usuarioId.isBlank()){
                    showDialog("Error", "Id de usuario requerida")
                    return@launch
                }


                if (tareaId.isBlank()){
                    showDialog("Error", "Id de tarea requerida")
                    return@launch
                }

                val token = tokenManager.getToken()!!
                val response = RetrofitClient.apiService.marcarTareaComoHecha(token, tareaId, usuarioId)

                if (response.isSuccessful) {
                    showDialog("Éxito", "Tarea modificada correctamente")
                }else{
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)
                    Log.e("AdminScreenViewModel", "Error ${response.code()}: $errorBodyString")

                    showDialog("Error", errorMessage)
                }


            } catch (e: Exception) {
                _mensajeError.value = "Error al marcar tarea como hecha: ${e.message}"
            }
        }
    }



    fun eliminarTareaDeUsuario(tareaId: String, usuarioId: String) {
        viewModelScope.launch {
            try {
                if (usuarioId.isBlank()){
                    showDialog("Error", "Id de usuario requerida")
                    return@launch
                }

                if (tareaId.isBlank()){
                    showDialog("Error", "Id de tarea requerida")
                    return@launch
                }

                val token = tokenManager.getToken()!!
                val response = RetrofitClient.apiService.eliminarTareaDeUsuario(token, tareaId ,usuarioId)

                if (response.isSuccessful) {
                    showDialog("Éxito", "Tarea eliminada correctamente")
                }else{
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)
                    Log.e("AdminScreenViewModel", "Error ${response.code()}: $errorBodyString")

                    showDialog("Error", errorMessage)
                }

            } catch (e: Exception) {
                _mensajeError.value = "Error al eliminar tarea: ${e.message}"
            }
        }
    }



    fun eliminarTodasLasTareasDeUsuario(usuarioId: String) {
        viewModelScope.launch {
            try {
                if (usuarioId.isBlank()){
                    showDialog("Error", "Id de usuario requerida")
                    return@launch
                }

                val token = tokenManager.getToken()!!
                val response = RetrofitClient.apiService.eliminarTodasLasTareasDeUsuario(token, usuarioId)

                if (response.isSuccessful) {
                    showDialog("Éxito", "Tareas eliminadas correctamente")
                }else{
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)
                    Log.e("AdminScreenViewModel", "Error ${response.code()}: $errorBodyString")

                    showDialog("Error", errorMessage)
                }


            } catch (e: Exception) {
                _mensajeError.value = "Error al eliminar todas las tareas del usuario: ${e.message}"
            }
        }
    }



    private fun showDialog(tittle:String, message: String) {
        _operationResult.postValue(message) // Guarda el mensaje de error
        _tittleDialog.value = tittle
        _showErrorDialog.value = true   // Activa el error

    }

    fun dismissErrorDialog() {
        _showErrorDialog.value = false
        _tittleDialog.value = ""
        _operationResult.value = null
    }

    private fun extractErrorMessage(errorBody: String?): String {
        return try {
            if (errorBody.isNullOrEmpty()) {
                "Error, no tienes un ROL autorizado "
            } else {
                val jsonObject = JSONObject(errorBody)
                jsonObject.optString("message", "Ocurrió un error desconocido")
            }
        } catch (e: Exception) {
            "Error procesando la respuesta del servidor"
        }
    }


    fun resetTareas(){
        _tareas.value = null
    }

}