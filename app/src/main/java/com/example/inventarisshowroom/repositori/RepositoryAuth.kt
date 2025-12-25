package com.example.inventarisshowroom.repositori

import com.example.inventarisshowroom.modeldata.LoginResponse
import retrofit2.Response
import com.example.inventarisshowroom.apiservice.ServiceApiShowroom
import com.example.inventarisshowroom.modeldata.LoginRequest

interface RepositoryAuth {
    suspend fun login(email: String, password: String): LoginResponse
    suspend fun logout(token: String): Response<Void>
}

class JaringanRepositoryAuth(
    private val serviceApiShowroom: ServiceApiShowroom
) : RepositoryAuth {

    override suspend fun login(email: String, password: String): LoginResponse {
        val loginRequest = LoginRequest(email, password)
        return serviceApiShowroom.login(loginRequest)
    }

    override suspend fun logout(token: String): Response<Void> {
        return serviceApiShowroom.logout("Bearer $token")
    }
}

