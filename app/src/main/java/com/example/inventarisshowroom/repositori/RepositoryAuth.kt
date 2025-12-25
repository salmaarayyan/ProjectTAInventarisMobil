package com.example.inventarisshowroom.repositori

import com.example.inventarisshowroom.modeldata.LoginResponse
import retrofit2.Response

interface RepositoryAuth {
    suspend fun login(email: String, password: String): LoginResponse
    suspend fun logout(token: String): Response<Void>
}

