package com.appTareas.screens.welcomeScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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

class WelcomeScreenViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private var _tareas = MutableLiveData<List<TareaDTO?>?>()
    val tareas: MutableLiveData<List<TareaDTO?>?> get() = _tareas

    private val _tarea = MutableLiveData<TareaDTO?>()
    val tarea: LiveData<TareaDTO?> get() = _tarea

    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage



    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog

    private val _operationResult = MutableLiveData<String?>()
    val operationResult: LiveData<String?> = _operationResult

    private val _tittleDialog = MutableLiveData<String?>()
    val tittleDialog: LiveData<String?> = _tittleDialog



    fun obtenerTareas() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                val response = RetrofitClient.apiService.getAllTasks(token!!)

                if (response.isSuccessful) {
                    _tareas.value = response.body()

                    if (tareas.value!!.isEmpty()){
                        showDialog(tittle = "Error", "No hay tareas")
                    }

                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                    showDialog(tittle = "Error", errorMessage.toString())
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión"
                showDialog(tittle = "Error", errorMessage.toString())
            }
        }
    }

    fun obtenerTareaPorId(id: String) {
        viewModelScope.launch {
            try {
                if (id.isBlank()){
                    showDialog("Error", "Id no puede estar vacía")
                    return@launch
                }
                val token = tokenManager.getToken()!!
                val response = RetrofitClient.apiService.getTaskById(token, id)

                if (response.isSuccessful) {
                    _tarea.value = response.body()
                    _showDialog.value = true
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)

                    showDialog("Error", errorMessage)
                    _errorMessage.value = "Tarea no encontrada"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión"
            }
        }
    }



    fun crearTarea(titulo: String, descripcion: String) {
        viewModelScope.launch {
            try {
                val nuevaTarea = CreateTareaDTO(titulo, descripcion)
                val token = tokenManager.getToken()

                if (titulo.isBlank() || descripcion.isBlank()){
                    showDialog("Error","Titulo y descripcion no válidos")
                    return@launch
                }

                Log.d("CrearTarea", "Creando tarea con título: $titulo y descripción: $descripcion. TOKEN: $token")

                val response = RetrofitClient.apiService.createTask(tokenManager.getToken()!!, nuevaTarea)
                if (response.isSuccessful) {
                    //obtenerTareas() // Recargar lista
                    showDialog("Éxito","Tarea creada correctamente")
                } else {
                    Log.e("CrearTarea", "Error al crear la tarea. Código de respuesta: ${response.code()} - Mensaje: ${response.message()}")
                    _errorMessage.value = "Error al crear la tarea"
                }
            } catch (e: Exception) {
                Log.e("CrearTarea", "Error de conexión", e)
                _errorMessage.value += "Error de conexión"
            }
        }
    }

    fun actualizarTarea(id: String) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()!!
                val response = RetrofitClient.apiService.updateTask(token, id)

                if (id.isBlank()){
                    showDialog("Error","La id no puede estar vacía")
                    return@launch
                }

                if (response.isSuccessful) {
                    showDialog("Exito", "Tarea editada correctamente")
                    //obtenerTareas()
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)

                    showDialog("Error", errorMessage)
                    _errorMessage.value = "Error al actualizar tarea"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión"
            }
        }
    }

    fun eliminarTarea(id: String) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()!!
                val response = RetrofitClient.apiService.deleteTask(token, id)

                if (id.isBlank()){
                    showDialog("Error","La id no puede estar vacía")
                    return@launch
                }

                if (response.isSuccessful) {
                    showDialog("Éxito","Tarea eliminada correctamente")

                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)

                    showDialog("Error", errorMessage)
                    _errorMessage.value = "Error al eliminar tarea"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión"
            }
        }
    }

    fun eliminarTodasLasTareas() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()!!
                val response = RetrofitClient.apiService.deleteAllTasks(token)

                if (response.isSuccessful) {
                    _tareas.value = emptyList()
                    showDialog("Error", "Todas las tareas borradas correctamente ")
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)

                    showDialog("Error", errorMessage)
                    _errorMessage.value = "Error al eliminar todas las tareas"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión"
            }
        }
    }

    private fun extractErrorMessage(errorBody: String?): String {
        return try {
            if (errorBody.isNullOrEmpty()) {
                "Error desconocido: la respuesta del servidor está vacía"
            } else {
                val jsonObject = JSONObject(errorBody)
                jsonObject.optString("message", "Ocurrió un error desconocido")
            }
        } catch (e: Exception) {
            "Error procesando la respuesta del servidor"
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

    fun resetTarea() {
        _tarea.value = null
    }

    fun resetTareas(){
        _tareas.value = null
    }


}

