package com.example.inventarisshowroom.repositori

import com.example.inventarisshowroom.modeldata.ResponseApi
import com.example.inventarisshowroom.modeldata.DataMerk
import com.example.inventarisshowroom.apiservice.ServiceApiShowroom
import com.example.inventarisshowroom.modeldata.MerkRequest
import com.example.inventarisshowroom.modeldata.MerkUpdateRequest

interface RepositoryMerk {
    suspend fun getMerkList(token: String): List<DataMerk>
    suspend fun createMerk(token: String, namaMerk: String): ResponseApi
    suspend fun updateMerk(token: String, id: Int, namaMerk: String): ResponseApi
    suspend fun deleteMerk(token: String, id: Int): ResponseApi
}

class JaringanRepositoryMerk(
    private val serviceApiShowroom: ServiceApiShowroom
) : RepositoryMerk {

    override suspend fun getMerkList(token: String): List<DataMerk> {
        val response = serviceApiShowroom.getMerkList("Bearer $token")
        return response.data
    }

    override suspend fun createMerk(token: String, namaMerk: String): ResponseApi {
        val merkRequest = MerkRequest(merk = namaMerk)
        return serviceApiShowroom.createMerk("Bearer $token", merkRequest)
    }

    override suspend fun updateMerk(token: String, id: Int, namaMerk: String): ResponseApi {
        val merkUpdateRequest = MerkUpdateRequest(id = id, nama_merk = namaMerk)
        return serviceApiShowroom.updateMerk("Bearer $token", merkUpdateRequest)
    }

    override suspend fun deleteMerk(token: String, id: Int): ResponseApi {
        return serviceApiShowroom.deleteMerk("Bearer $token", id)
    }
}

