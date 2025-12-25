package com.example.inventarisshowroom.apiservice

import com.example.inventarisshowroom.modeldata.*
import retrofit2.Response
import retrofit2.http.*

interface ServiceApiShowroom {

    // ==================== AUTH ====================
    @POST("login.php")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("logout.php")
    suspend fun logout(@Header("Authorization") token: String): Response<Void>


}