package com.appTareas.screens.tareasScreen


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appTareas.model.CreateTareaDTO
import com.appTareas.model.TareaDTO
import com.appTareas.navegation.TokenManager
import com.appTareas.retrofit.ApiService
import com.appTareas.retrofit.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject


class TareasScreenViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _titulo = MutableStateFlow("")
    val titulo: StateFlow<String> = _titulo.asStateFlow()

    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion.asStateFlow()

    fun onTituloChange(newTitulo: String) {
        _titulo.value = newTitulo
    }
    fun onDescripcionChange(newDescripcion: String) {
        _descripcion.value = newDescripcion
    }

    private val _tareasResult = MutableLiveData<String?>()
    val tareasResult: LiveData<String?> = _tareasResult

    private val _tasks = MutableStateFlow<List<TareaDTO>>(emptyList())
    val tasks: StateFlow<List<TareaDTO>> = _tasks.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    fun toggleDialog() {
        _showDialog.value = !_showDialog.value
    }

    fun closeDialog() {
        _showDialog.value = false
    }

    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog

    init {
        cargarTareas()
    }

    fun cargarTareas() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()  // Obtener el token del TokenManager
                if (token == null) {
                    _errorMessage.value = "No se ha encontrado el token, por favor inicie sesión."
                    return@launch
                }

                val response = RetrofitClient.apiService.getAllTasks(token)
                if (response.isSuccessful) {
                    _tasks.value = response.body() ?: emptyList()
                } else {

                    _tasks.value = emptyList()
                    // Guardamos el error antes de procesarlo
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)

                    // Evitar dialogo de error cuando no hay tareas, ya que la api devuelve expcecion en vez de una lista vacia
                    if (!errorMessage.contains("no tiene tareas en este momento", ignoreCase = true)){ showError(errorMessage)
                    }


                }
            } catch (e: Exception) {
                showError("Error de conexión: ${e.message}")
            }
        }
    }

    // Función que crea una nueva tarea
    fun createTask() {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token == null) {
                    _errorMessage.value = "No se ha encontrado el token, por favor inicie sesión."
                    return@launch
                }

                val newTask = CreateTareaDTO(_titulo.value, _descripcion.value)
                val response = RetrofitClient.apiService.createTask(token, newTask)
                closeDialog()
                clearTexfields()

                if (response.isSuccessful) {
                    // Al agregar la nueva tarea, recargamos la lista de tareas
                    cargarTareas()
                } else {
                    // Guardamos el error antes de procesarlo
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)

                    showError(errorMessage)
                }
            } catch (e: Exception) {
                showError("Error de conexión: ${e.message}")
            }
        }
    }

    // Función para eliminar una tarea
    fun deleteTask(tareaId: String) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token == null) {
                    _errorMessage.value = "No se ha encontrado el token, por favor inicie sesión."
                    return@launch
                }

                val response = RetrofitClient.apiService.deleteTask(token, tareaId)

                if (response.isSuccessful) {
                    // Recargar las tareas después de eliminar una
                    cargarTareas()
                } else {
                    // Guardamos el error antes de procesarlo
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)

                    showError(errorMessage)
                }
            } catch (e: Exception) {
                showError("Error de conexión: ${e.message}")
            }
        }
    }

    // Función para marcar una tarea como completada o pendiente
    fun updateTaskState(tareaId: String) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token == null) {
                    _errorMessage.value = "No se ha encontrado el token, por favor inicie sesión."
                    return@launch
                }

                val response = RetrofitClient.apiService.updateTask(token, tareaId)

                if (response.isSuccessful) {
                    // Recargar las tareas después de la actualización
                    cargarTareas()
                } else {
                    //_errorMessage.value = "Error al actualizar la tarea"

                    // Guardamos el error antes de procesarlo
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)

                    showError(errorMessage)
                }
            } catch (e: Exception) {
                showError("Error de conexión: ${e.message}")
            }
        }
    }

    private fun clearTexfields(){
        _titulo.value = ""
        _descripcion.value = ""
    }

    private fun showError(message: String) {
        _tareasResult.postValue(message) // Guarda el mensaje de error
        _showErrorDialog.value = true   // Activa el diálogo de error
    }

    // Extraer mensaje del error del servidor
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

    fun dismissErrorDialog() {
        _showErrorDialog.value = false
        _tareasResult.value = null
    }
}


