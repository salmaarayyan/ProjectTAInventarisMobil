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

    // ==================== MOBIL ====================
    @GET("mobil/read.php")
    suspend fun getMobilList(
        @Header("Authorization") token: String,
        @Query("merk_id") merkId: Int
    ): MobilListResponse

    @GET("mobil/read_detail.php")
    suspend fun getMobilDetail(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): DataMobilDetail

    @POST("mobil/create.php")
    suspend fun createMobil(
        @Header("Authorization") token: String,
        @Body mobilRequest: MobilRequest
    ): ResponseApi

    @PUT("mobil/update.php")
    suspend fun updateMobil(
        @Header("Authorization") token: String,
        @Body mobilRequest: MobilUpdateRequest
    ): ResponseApi

    @HTTP(method = "DELETE", path = "mobil/delete.php", hasBody = false)
    suspend fun deleteMobil(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): ResponseApi

    // ==================== STOK ====================
    @PUT("stok/tambah.php")
    suspend fun tambahStok(
        @Header("Authorization") token: String,
        @Body stokRequest: StokRequest
    ): ResponseApi

    @PUT("stok/kurangi.php")
    suspend fun kurangiStok(
        @Header("Authorization") token: String,
        @Body stokRequest: StokRequest
    ): ResponseApi
}