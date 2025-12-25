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

    // ==================== MERK ====================
    @GET("merk/read.php")
    suspend fun getMerkList(@Header("Authorization") token: String): MerkListResponse


    @POST("merk/create.php")
    suspend fun createMerk(
        @Header("Authorization") token: String,
        @Body merkRequest: MerkRequest
    ): ResponseApi

    @PUT("merk/update.php")
    suspend fun updateMerk(
        @Header("Authorization") token: String,
        @Body merkRequest: MerkUpdateRequest
    ): ResponseApi

    @HTTP(method = "DELETE", path = "merk/delete.php", hasBody = false)
    suspend fun deleteMerk(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): ResponseApi

}