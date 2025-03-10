package com.appTareas.screens.loginScreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appTareas.navegation.TokenManager

class LoginScreenViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
            val tokenManager = TokenManager(application)
            LoginScreenViewModel(tokenManager) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
