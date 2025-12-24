package com.example.inventarisshowroom.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class MobilListResponse(
    val success: Boolean,
    val data: List<DataMobil>
)
@Serializable
data class DataMobil(
    val id: Int,
    val nama_mobil: String,
    val merk_id: Int,
    val nama_merk: String,
    val tipe: String,
    val tahun: Int,
    val harga: Double,
    val warna: String,
    val jumlah_stok: Int,
    val created_at: String
)

@Serializable
data class DataMobilDetail(
    val success: Boolean,
    val data: MobilDetail
)

@Serializable
data class MobilDetail(
    val id: Int,
    val nama_mobil: String,
    val merk_id: Int,
    val nama_merk: String,
    val tipe: String,
    val tahun: Int,
    val harga: Double,
    val warna: String,
    val jumlah_stok: Int,
    val created_at: String,
    val stok_updated_at: String
)

@Serializable
data class MobilRequest(
    val nama_mobil: String,
    val merk_id: Int,
    val tipe: String,
    val tahun: Int,
    val harga: Double,
    val warna: String,
    val jumlah_stok: Int = 0
)

@Serializable
data class MobilUpdateRequest(
    val id: Int,
    val nama_mobil: String,
    val merk_id: Int,
    val tipe: String,
    val tahun: Int,
    val harga: Double,
    val warna: String
)

