package com.example.inventarisshowroom.viewmodel

import com.example.inventarisshowroom.modeldata.MobilDetail
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarisshowroom.repositori.RepositoryMobil

sealed class DetailMobilUiState {
    data class Success(val mobilDetail: MobilDetail) : DetailMobilUiState()
    object Error : DetailMobilUiState()
    object Loading : DetailMobilUiState()
}

class DetailMobilViewModel(
    private val repositoryMobil: RepositoryMobil
) : ViewModel() {

    var detailMobilUiState: DetailMobilUiState by mutableStateOf(DetailMobilUiState.Loading)
        private set


}

