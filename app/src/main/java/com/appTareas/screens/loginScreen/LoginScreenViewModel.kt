package com.appTareas.screens.loginScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.appTareas.model.LoginRequest
import com.appTareas.navegation.AppScreen
import com.appTareas.navegation.TokenManager
import com.appTareas.retrofit.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject


class LoginScreenViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email

    private val _password = MutableStateFlow("")
    val password = _password


    private val _loginResult = MutableLiveData<String?>()
    val loginResult: LiveData<String?> = _loginResult

    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog

    private val _isVisiblePassword = MutableStateFlow(false)
    val isVisiblePassword = _isVisiblePassword


    fun onEmailChange(newEmail: String){
        _email.value = newEmail
    }


    fun onPasswordChange(newPassword: String){
        _password.value = newPassword
    }



    fun login(navController: NavController) {
        viewModelScope.launch {

            try {
                val response = RetrofitClient.apiService.login(LoginRequest(email.value, password.value))


                if (response.isSuccessful) {
                    val token = response.body()?.token

                    if (token!= null){
                        tokenManager.saveToken("Bearer $token")
                        _loginResult.value = "Login exitoso"
                        navController.navigate(route = AppScreen.TareasScreen.route)
                    }else
                    {
                        _loginResult.value = "Login fallido"
                    }

                } else {
                    // Guardamos el error antes de procesarlo
                    val errorBodyString = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBodyString)

                    showError(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("errorConexion", e.toString())
                showError("Error de conexión: ${e.message}")
            }
        }
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

    private fun showError(message: String) {
        _loginResult.postValue(message) // Guarda el mensaje de error
        _showErrorDialog.value = true   // Activa el diálogo de error
    }

    fun dismissErrorDialog() {
        _showErrorDialog.value = false
        _loginResult.value = null
    }

    fun onPasswordVisibleChange(){
        _isVisiblePassword.value = !_isVisiblePassword.value
    }


}
