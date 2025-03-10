package com.appTareas.screens.registerScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.appTareas.model.Direccion
import com.appTareas.model.RegisterRequest
import com.appTareas.retrofit.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class RegisterViewModel : ViewModel() {

    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog


    private val _username = MutableStateFlow("")
    val username = _username

    private val _email = MutableStateFlow("")
    val email = _email

    private val _password = MutableStateFlow("")
    val password = _password

    private val _passwordRepeat = MutableStateFlow("")
    val passwordRepeat = _passwordRepeat


    private val _direccion = MutableStateFlow(Direccion("", "", "", "", ""))
    val direccion = _direccion

    fun onUsernameChange(value: String) { _username.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onPasswordChange(value: String) { _password.value = value }
    fun onPasswordRepeatChange(value: String) { _passwordRepeat.value = value }

    fun onCalleChange(newCalle: String) {
        _direccion.value = _direccion.value.copy(calle = newCalle)
    }

    fun onNumChange(newNum: String) {
        _direccion.value = _direccion.value.copy(num = newNum)
    }

    fun onMunicipioChange(newMunicipio: String) {
        _direccion.value = _direccion.value.copy(municipio = newMunicipio)
    }

    fun onProvinciaChange(newProvincia: String) {
        _direccion.value = _direccion.value.copy(provincia = newProvincia)
    }

    fun onCpChange(newCp: String) {
        _direccion.value = _direccion.value.copy(cp = newCp)
    }


    // Variables para mostrar el resultado y manejar el estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading

    private val _loginResult = MutableLiveData<String?>()
    val loginResult: LiveData<String?> = _loginResult

    private val _navigationEvent = MutableStateFlow<String?>(null) // Para emitir un evento de navegación
    val navigationEvent = _navigationEvent



    fun register(navController: NavController) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = RegisterRequest(
                    username.value,
                    email.value,
                    password.value,
                    passwordRepeat.value,
                    "USER",
                    direccion.value
                )

                val response = RetrofitClient.apiService.register(request)

                if (response.isSuccessful) {
                    val username = response.body()?.username
                    val email = response.body()?.email
                    if (username != null && email != null) {
                        _loginResult.value = "Registro exitoso"
                        _navigationEvent.value = "success"

                        navController.popBackStack()


                    } else {
                        showError("Error inesperado: usuario o email vacíos")
                    }
                } else {
                    // Guardamos el error antes de procesarlo para evitar que se pierda
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)

                    showError(errorMessage)
                }
            } catch (e: Exception) {
                showError("Error de conexión: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun extractErrorMessage(errorBody: String?): String {
        return try {
            if (errorBody.isNullOrEmpty()) {
                "Error desconocido: la respuesta del servidor está vacía"
            } else {
                val jsonObject = JSONObject(errorBody)
                jsonObject.optString("message", "Ocurrió un error desconocido") // Evita fallos si no hay "message"
            }
        } catch (e: Exception) {
            "Error procesando la respuesta del servidor"
        }
    }

    private fun showError(message: String) {
        _loginResult.postValue(message) // Guarda el mensaje de error
        _showErrorDialog.value = true   // Activa el error

    }

    fun dismissErrorDialog() {
        _showErrorDialog.value = false
        _loginResult.value = null
    }

}