package com.example.inventarisshowroom.viewmodel

import com.example.inventarisshowroom.modeldata.MobilDetail

sealed class DetailMobilUiState {
    data class Success(val mobilDetail: MobilDetail) : DetailMobilUiState()
    object Error : DetailMobilUiState()
    object Loading : DetailMobilUiState()
}

