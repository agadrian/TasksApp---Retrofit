package com.appTareas.navegation

import android.app.Application
import android.content.Context

class TokenManager(private val application: Application) {
    private val prefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // Sobrescribe el token anterior
    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

}
