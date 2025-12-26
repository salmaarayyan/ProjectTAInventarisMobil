package com.example.inventarisshowroom.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventarisshowroom.repositori.RepositoryMobil

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

class FormMobilViewModel(
    private val repositoryMobil: RepositoryMobil
) : ViewModel() {

    var formState by mutableStateOf(FormMobilState())
        private set

    var isEditMode by mutableStateOf(false)
        private set

    var editMobilId by mutableStateOf(0)
        private set


}
