package com.example.inventarisshowroom.repositori

import com.example.inventarisshowroom.apiservice.ServiceApiShowroom
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val repositoryMerk: RepositoryMerk
    val repositoryMobil: RepositoryMobil
    val repositoryAuth: RepositoryAuth
}

class ShowroomContainer : AppContainer {

    private val baseUrl = "http://10.0.2.2/showroom-backend/api/"

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .build()

    private val serviceApiShowroom: ServiceApiShowroom by lazy {
        retrofit.create(ServiceApiShowroom::class.java)
    }

    override val repositoryMerk: RepositoryMerk by lazy {
        JaringanRepositoryMerk(serviceApiShowroom)
    }

    override val repositoryMobil: RepositoryMobil by lazy {
        JaringanRepositoryMobil(serviceApiShowroom)
    }

    override val repositoryAuth: RepositoryAuth by lazy {
        JaringanRepositoryAuth(serviceApiShowroom)
    }
}