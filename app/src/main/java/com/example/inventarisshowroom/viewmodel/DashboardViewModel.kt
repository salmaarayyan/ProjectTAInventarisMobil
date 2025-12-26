package com.example.inventarisshowroom.viewmodel

import com.example.inventarisshowroom.modeldata.DataMerk
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarisshowroom.repositori.RepositoryMerk
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DashboardUiState {
    data class Success(val merkList: List<DataMerk>) : DashboardUiState()
    object Error : DashboardUiState()
    object Loading : DashboardUiState()
}

data class MerkFormState(
    val namaMerk: String = "",
    val namaMerkError: String? = null
)

class DashboardViewModel(
    private val repositoryMerk: RepositoryMerk
) : ViewModel() {

    var dashboardUiState: DashboardUiState by mutableStateOf(DashboardUiState.Loading)
        private set

    var merkFormState by mutableStateOf(MerkFormState())
        private set

    var isDialogOpen by mutableStateOf(false)
        private set

    var editMerkId by mutableStateOf<Int?>(null)
        private set

    // Load merk list
    fun loadMerkList(token: String) {
        viewModelScope.launch {
            dashboardUiState = DashboardUiState.Loading
            dashboardUiState = try {
                val merkList = repositoryMerk.getMerkList(token)
                DashboardUiState.Success(merkList)
            } catch (e: IOException) {
                DashboardUiState.Error
            } catch (e: HttpException) {
                DashboardUiState.Error
            }
        }
    }

    // Update nama merk
    fun updateNamaMerk(nama: String) {
        merkFormState = merkFormState.copy(
            namaMerk = nama,
            namaMerkError = if (nama.isEmpty()) "Nama merk harus diisi" else null
        )
    }

    // Open dialog untuk tambah merk
    fun openAddDialog() {
        editMerkId = null
        merkFormState = MerkFormState()
        isDialogOpen = true
    }

    // Open dialog untuk edit merk
    fun openEditDialog(id: Int, namaMerk: String) {
        editMerkId = id
        merkFormState = MerkFormState(namaMerk = namaMerk)
        isDialogOpen = true
    }

    // Close dialog
    fun closeDialog() {
        isDialogOpen = false
        merkFormState = MerkFormState()
        editMerkId = null
    }

    // Save merk (create or update)
    suspend fun saveMerk(token: String): Boolean {
        if (merkFormState.namaMerk.isEmpty()) {
            merkFormState = merkFormState.copy(namaMerkError = "Nama merk harus diisi")
            return false
        }

        return try {
            if (editMerkId == null) {
                repositoryMerk.createMerk(token, merkFormState.namaMerk)
            } else {
                repositoryMerk.updateMerk(token, editMerkId!!, merkFormState.namaMerk)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    // Delete merk
    suspend fun deleteMerk(token: String, id: Int): Boolean {
        return try {
            repositoryMerk.deleteMerk(token, id)
            true
        } catch (e: Exception) {
            false
        }
    }

}

