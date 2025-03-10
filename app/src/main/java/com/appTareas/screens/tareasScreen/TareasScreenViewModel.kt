package com.appTareas.screens.tareasScreen


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


class TareasScreenViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TareaDTO>>(emptyList())
    val tasks: StateFlow<List<TareaDTO>> = _tasks.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

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
                    _errorMessage.value = "Error al obtener las tareas"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión"
            }
        }
    }

    // Función que crea una nueva tarea
    fun createTask(titulo: String, descripcion: String) {
        viewModelScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token == null) {
                    _errorMessage.value = "No se ha encontrado el token, por favor inicie sesión."
                    return@launch
                }

                val newTask = CreateTareaDTO(titulo, descripcion)
                val response = RetrofitClient.apiService.createTask(token, newTask)

                if (response.isSuccessful) {
                    // Al agregar la nueva tarea, recargamos la lista de tareas
                    cargarTareas()
                } else {
                    _errorMessage.value = "Error al crear la tarea"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión"
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
                    _errorMessage.value = "Error al eliminar la tarea"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión"
            }
        }
    }

    // Función para marcar una tarea como completada o pendiente
    fun updateTaskState(tareaId: String, estado: String) {
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
                    _errorMessage.value = "Error al actualizar la tarea"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión"
            }
        }
    }
}


