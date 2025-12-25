package com.example.inventarisshowroom.repositori

import com.example.inventarisshowroom.modeldata.*
import com.example.inventarisshowroom.apiservice.ServiceApiShowroom

interface RepositoryMobil {
    suspend fun getMobilList(token: String, merkId: Int): List<DataMobil>
    suspend fun getMobilDetail(token: String, id: Int): MobilDetail
    suspend fun createMobil(token: String, mobilRequest: MobilRequest): ResponseApi
    suspend fun updateMobil(token: String, mobilUpdateRequest: MobilUpdateRequest): ResponseApi
    suspend fun deleteMobil(token: String, id: Int): ResponseApi
    suspend fun tambahStok(token: String, mobilId: Int): ResponseApi
    suspend fun kurangiStok(token: String, mobilId: Int): ResponseApi
}

class JaringanRepositoryMobil(
    private val serviceApiShowroom: ServiceApiShowroom
) : RepositoryMobil {

    override suspend fun getMobilList(token: String, merkId: Int): List<DataMobil> {
        val response = serviceApiShowroom.getMobilList("Bearer $token", merkId)
        return response.data  // ← AMBIL .data dari wrapper
    }

    override suspend fun getMobilDetail(token: String, id: Int): MobilDetail {
        val response = serviceApiShowroom.getMobilDetail("Bearer $token", id)
        return response.data
    }

    override suspend fun createMobil(token: String, mobilRequest: MobilRequest): ResponseApi {
        return serviceApiShowroom.createMobil("Bearer $token", mobilRequest)
    }

    override suspend fun updateMobil(token: String, mobilUpdateRequest: MobilUpdateRequest): ResponseApi {
        return serviceApiShowroom.updateMobil("Bearer $token", mobilUpdateRequest)
    }

    override suspend fun deleteMobil(token: String, id: Int): ResponseApi {
        return serviceApiShowroom.deleteMobil("Bearer $token", id)
    }

    override suspend fun tambahStok(token: String, mobilId: Int): ResponseApi {
        val stokRequest = StokRequest(mobil_id = mobilId)
        return serviceApiShowroom.tambahStok("Bearer $token", stokRequest)
    }

    override suspend fun kurangiStok(token: String, mobilId: Int): ResponseApi {
        val stokRequest = StokRequest(mobil_id = mobilId)
        return serviceApiShowroom.kurangiStok("Bearer $token", stokRequest)
    }
}
