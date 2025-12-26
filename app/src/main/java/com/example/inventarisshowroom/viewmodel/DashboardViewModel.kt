package com.example.inventarisshowroom.viewmodel

import com.example.inventarisshowroom.modeldata.DataMerk

sealed class DashboardUiState {
    data class Success(val merkList: List<DataMerk>) : DashboardUiState()
    object Error : DashboardUiState()
    object Loading : DashboardUiState()
}

data class MerkFormState(
    val namaMerk: String = "",
    val namaMerkError: String? = null
)

