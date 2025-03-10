package com.appTareas.retrofit

import com.appTareas.model.CreateTareaDTO
import com.appTareas.model.LoginRequest
import com.appTareas.model.LoginResponse
import com.appTareas.model.RegisterRequest
import com.appTareas.model.RegisterResponse
import com.appTareas.model.TareaDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("usuarios/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("tareas")
    suspend fun getAllTasks(@Header("Authorization") token: String): Response<List<TareaDTO>>

    @GET("tareas/{tareaId}")
    suspend fun getTaskById(@Header("Authorization") token: String, @Path("tareaId") tareaId: String): Response<TareaDTO>

    @POST("tareas")
    suspend fun createTask(@Header("Authorization") token: String, @Body request: CreateTareaDTO): Response<TareaDTO>

    @PUT("tareas/{tareaId}")
    suspend fun updateTask(@Header("Authorization") token: String, @Path("tareaId") tareaId: String): Response<TareaDTO>

    @DELETE("tareas/{tareaId}")
    suspend fun deleteTask(@Header("Authorization") token: String, @Path("tareaId") tareaId: String): Response<Unit>

    @DELETE("tareas")
    suspend fun deleteAllTasks(@Header("Authorization") token: String): Response<Unit>


    /**
     * ADMIN PART
     */


    @GET("admin/tareas")
    suspend fun obtenerTodasLasTareas(
        @Header("Authorization") token: String
    ): Response<List<TareaDTO>>

    @GET("admin/usuario/{usuarioId}/tareas")
    suspend fun obtenerTareasDeUsuario(
        @Header("Authorization") token: String,
        @Path("usuarioId") usuarioId: String
    ): Response<List<TareaDTO>>

    @POST("admin/usuario/{usuarioId}/tareas")
    suspend fun crearTareaParaUsuario(
        @Header("Authorization") token: String,
        @Path("usuarioId") usuarioId: String,
        @Body tarea: CreateTareaDTO
    ): Response<TareaDTO>

    @PUT("admin/tareas/{tareaId}/usuario/{usuarioId}")
    suspend fun marcarTareaComoHecha(
        @Header("Authorization") token: String,
        @Path("tareaId") tareaId: String,
        @Path("usuarioId") usuarioId: String
    ): Response<TareaDTO>

    @DELETE("admin/tareas/{tareaId}/usuario/{usuarioId}")
    suspend fun eliminarTareaDeUsuario(
        @Header("Authorization") token: String,
        @Path("tareaId") tareaId: String,
        @Path("usuarioId") usuarioId: String
    ): Response<Unit>

    @DELETE("admin/usuario/{usuarioId}/tareas")
    suspend fun eliminarTodasLasTareasDeUsuario(
        @Header("Authorization") token: String,
        @Path("usuarioId") usuarioId: String
    ): Response<Unit>






}