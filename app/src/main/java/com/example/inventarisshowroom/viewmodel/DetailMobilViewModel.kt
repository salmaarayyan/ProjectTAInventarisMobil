package com.example.inventarisshowroom.viewmodel

import com.example.inventarisshowroom.modeldata.MobilDetail
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarisshowroom.repositori.RepositoryMobil
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

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

    // Load detail mobil
    fun loadDetailMobil(token: String, id: Int) {
        viewModelScope.launch {
            detailMobilUiState = DetailMobilUiState.Loading
            detailMobilUiState = try {
                val response = repositoryMobil.getMobilDetail(token, id)
                DetailMobilUiState.Success(response)
            } catch (e: IOException) {
                DetailMobilUiState.Error
            } catch (e: HttpException) {
                DetailMobilUiState.Error
            }
        }
    }
}

