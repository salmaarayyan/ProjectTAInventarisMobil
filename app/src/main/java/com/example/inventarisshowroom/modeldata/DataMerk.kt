package com.example.inventarisshowroom.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class MerkListResponse(
    val success: Boolean,
    val data: List<DataMerk>
)
@Serializable
data class DataMerk(
    val id: Int,
    val nama_merk: String,
    val jumlah_mobil: Int,
    val created_at: String
)

@Serializable
data class MerkRequest(
    val merk: String  // Backend expect "merk", bukan "nama_merk"
)

@Serializable
data class MerkUpdateRequest(
    val id: Int,
    val nama_merk: String
)
