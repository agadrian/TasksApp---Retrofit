package com.appTareas.model

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val passwordRepeat: String,
    val rol: String,
    val direccion: Direccion
)