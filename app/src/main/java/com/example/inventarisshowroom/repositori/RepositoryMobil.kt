package com.example.inventarisshowroom.repositori

import com.example.inventarisshowroom.modeldata.*

interface RepositoryMobil {
    suspend fun getMobilList(token: String, merkId: Int): List<DataMobil>
    suspend fun getMobilDetail(token: String, id: Int): MobilDetail
    suspend fun createMobil(token: String, mobilRequest: MobilRequest): ResponseApi
    suspend fun updateMobil(token: String, mobilUpdateRequest: MobilUpdateRequest): ResponseApi
    suspend fun deleteMobil(token: String, id: Int): ResponseApi
    suspend fun tambahStok(token: String, mobilId: Int): ResponseApi
    suspend fun kurangiStok(token: String, mobilId: Int): ResponseApi
}

