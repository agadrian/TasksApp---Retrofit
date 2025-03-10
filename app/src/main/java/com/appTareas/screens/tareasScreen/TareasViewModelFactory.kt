package com.appTareas.screens.tareasScreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appTareas.navegation.TokenManager
import com.appTareas.retrofit.ApiService

class TareasViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TareasScreenViewModel::class.java)) {
            val tokenManager = TokenManager(application)
            TareasScreenViewModel(tokenManager) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}