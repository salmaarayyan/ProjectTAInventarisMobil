package com.example.inventarisshowroom.viewmodel

import com.example.inventarisshowroom.modeldata.DataMerk
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarisshowroom.repositori.RepositoryMerk

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

}

