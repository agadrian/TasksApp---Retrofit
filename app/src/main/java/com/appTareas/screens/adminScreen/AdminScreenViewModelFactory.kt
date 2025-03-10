package com.appTareas.screens.adminScreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appTareas.navegation.TokenManager


class AdminScreenViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AdminScreenViewModel::class.java)) {
            val tokenManager = TokenManager(application)
            AdminScreenViewModel(tokenManager) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
