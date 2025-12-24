package com.example.inventarisshowroom.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class ResponseApi(
    val success: Boolean,
    val message: String,
    val id: Int? = null,  // Untuk response create (return ID baru)
    val jumlah_stok: Int? = null  // Untuk response tambah/kurangi stok
)

@Serializable
data class StokRequest(
    val mobil_id: Int
)