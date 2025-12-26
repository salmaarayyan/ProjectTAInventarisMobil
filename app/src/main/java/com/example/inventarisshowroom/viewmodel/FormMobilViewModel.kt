package com.example.inventarisshowroom.viewmodel


data class FormMobilState(
    val namaMobil: String = "",
    val merkId: Int = 0,
    val merkName: String = "",
    val tipe: String = "",
    val tahun: String = "",
    val harga: String = "",
    val warna: String = "",
    val stok: String = "0",

    val namaMobilError: String? = null,
    val tipeError: String? = null,
    val tahunError: String? = null,
    val hargaError: String? = null,
    val warnaError: String? = null,
    val stokError: String? = null
)

