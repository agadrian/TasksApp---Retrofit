package com.appTareas.navegation

import android.app.Application
import android.content.Context

class TokenManager(private val application: Application) {
    private val prefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()  // Sobrescribe el token anterior
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun clearToken() {
        prefs.edit().remove("auth_token").apply()  // Elimina el token al cerrar sesión
    }
}
