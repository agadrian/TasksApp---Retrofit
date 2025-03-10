package com.appTareas.screens.welcomeScreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appTareas.navegation.TokenManager


class WelcomeScreeenViewModelFactory (
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WelcomeScreenViewModel::class.java)) {
            val tokenManager = TokenManager(application)
            WelcomeScreenViewModel(tokenManager) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

