package com.appTareas.model

data class TareaDTO(
    val id: String?,
    val titulo: String,
    val descripcion: String,
    val estado: String = "PENDIENTE",  // Puede ser "PENDIENTE" o "COMPLETADA"
    val usuarioId: String
)