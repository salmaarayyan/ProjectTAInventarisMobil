package com.example.inventarisshowroom.viewmodel

import com.example.inventarisshowroom.modeldata.DataMobil

sealed class ListMobilUiState {
    data class Success(val mobilList: List<DataMobil>) : ListMobilUiState()
    object Error : ListMobilUiState()
    object Loading : ListMobilUiState()
}


