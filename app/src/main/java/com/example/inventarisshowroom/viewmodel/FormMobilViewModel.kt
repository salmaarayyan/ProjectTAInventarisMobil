package com.example.inventarisshowroom.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventarisshowroom.repositori.RepositoryMobil
import com.example.inventarisshowroom.modeldata.MobilRequest
import com.example.inventarisshowroom.modeldata.MobilUpdateRequest

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

    // Set mode tambah
    fun setAddMode(merkId: Int, merkName: String) {
        isEditMode = false
        editMobilId = 0
        formState = FormMobilState(
            merkId = merkId,
            merkName = merkName
        )
    }

    // Set mode edit
    fun setEditMode(
        id: Int,
        namaMobil: String,
        merkId: Int,
        merkName: String,
        tipe: String,
        tahun: Int,
        harga: Double,
        warna: String
    ) {
        isEditMode = true
        editMobilId = id
        formState = FormMobilState(
            namaMobil = namaMobil,
            merkId = merkId,
            merkName = merkName,
            tipe = tipe,
            tahun = tahun.toString(),
            harga = harga.toString(),
            warna = warna
        )
    }

    // Update fields
    fun updateNamaMobil(value: String) {
        formState = formState.copy(
            namaMobil = value,
            namaMobilError = if (value.isEmpty()) "Nama mobil harus diisi" else null
        )
    }

    fun updateTipe(value: String) {
        formState = formState.copy(
            tipe = value,
            tipeError = if (value.isEmpty()) "Tipe mobil harus dipilih" else null
        )
    }

    fun updateTahun(value: String) {
        formState = formState.copy(
            tahun = value,
            tahunError = when {
                value.isEmpty() -> "Tahun harus diisi"
                value.toIntOrNull() == null -> "Tahun harus berupa angka"
                value.toInt() < 2000 -> "Tahun minimal 2000"
                else -> null
            }
        )
    }

    fun updateHarga(value: String) {
        formState = formState.copy(
            harga = value,
            hargaError = when {
                value.isEmpty() -> "Harga harus diisi"
                value.toDoubleOrNull() == null -> "Harga harus berupa angka"
                value.toDouble() <= 0 -> "Harga harus lebih dari 0"
                else -> null
            }
        )
    }

    fun updateWarna(value: String) {
        formState = formState.copy(
            warna = value,
            warnaError = if (value.isEmpty()) "Warna harus diisi" else null
        )
    }

    fun updateStok(value: String) {
        formState = formState.copy(
            stok = value,
            stokError = when {
                value.isEmpty() -> "Stok harus diisi"
                value.toIntOrNull() == null -> "Stok harus berupa angka"
                value.toInt() < 0 -> "Stok tidak boleh negatif"
                else -> null
            }
        )
    }

    // Validasi form
    private fun validateForm(): Boolean {
        val namaMobilError = if (formState.namaMobil.isEmpty()) "Nama mobil harus diisi" else null
        val tipeError = if (formState.tipe.isEmpty()) "Tipe mobil harus dipilih" else null
        val tahunError = when {
            formState.tahun.isEmpty() -> "Tahun harus diisi"
            formState.tahun.toIntOrNull() == null -> "Tahun harus berupa angka"
            formState.tahun.toInt() < 2000 -> "Tahun minimal 2000"
            else -> null
        }
        val hargaError = when {
            formState.harga.isEmpty() -> "Harga harus diisi"
            formState.harga.toDoubleOrNull() == null -> "Harga harus berupa angka"
            formState.harga.toDouble() <= 0 -> "Harga harus lebih dari 0"
            else -> null
        }
        val warnaError = if (formState.warna.isEmpty()) "Warna harus diisi" else null
        val stokError = if (!isEditMode) {
            when {
                formState.stok.isEmpty() -> "Stok harus diisi"
                formState.stok.toIntOrNull() == null -> "Stok harus berupa angka"
                formState.stok.toInt() < 0 -> "Stok tidak boleh negatif"
                else -> null
            }
        } else null

        formState = formState.copy(
            namaMobilError = namaMobilError,
            tipeError = tipeError,
            tahunError = tahunError,
            hargaError = hargaError,
            warnaError = warnaError,
            stokError = stokError
        )

        return namaMobilError == null && tipeError == null && tahunError == null &&
                hargaError == null && warnaError == null && stokError == null
    }

    // Save mobil
    suspend fun saveMobil(token: String): Boolean {
        if (!validateForm()) return false

        return try {
            if (isEditMode) {
                val request = MobilUpdateRequest(
                    id = editMobilId,
                    nama_mobil = formState.namaMobil,
                    merk_id = formState.merkId,
                    tipe = formState.tipe,
                    tahun = formState.tahun.toInt(),
                    harga = formState.harga.toDouble(),
                    warna = formState.warna
                )
                repositoryMobil.updateMobil(token, request)
            } else {
                val request = MobilRequest(
                    nama_mobil = formState.namaMobil,
                    merk_id = formState.merkId,
                    tipe = formState.tipe,
                    tahun = formState.tahun.toInt(),
                    harga = formState.harga.toDouble(),
                    warna = formState.warna,
                    jumlah_stok = formState.stok.toInt()
                )
                repositoryMobil.createMobil(token, request)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

}
