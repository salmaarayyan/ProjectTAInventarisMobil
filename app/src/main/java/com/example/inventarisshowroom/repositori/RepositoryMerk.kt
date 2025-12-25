package com.example.inventarisshowroom.repositori

import com.example.inventarisshowroom.modeldata.ResponseApi
import com.example.inventarisshowroom.modeldata.DataMerk

interface RepositoryMerk {
    suspend fun getMerkList(token: String): List<DataMerk>
    suspend fun createMerk(token: String, namaMerk: String): ResponseApi
    suspend fun updateMerk(token: String, id: Int, namaMerk: String): ResponseApi
    suspend fun deleteMerk(token: String, id: Int): ResponseApi
}

