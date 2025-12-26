package com.example.inventarisshowroom.viewmodel

import com.example.inventarisshowroom.modeldata.DataMobil
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarisshowroom.repositori.RepositoryMobil
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed class ListMobilUiState {
    data class Success(val mobilList: List<DataMobil>) : ListMobilUiState()
    object Error : ListMobilUiState()
    object Loading : ListMobilUiState()
}

class ListMobilViewModel(
    private val repositoryMobil: RepositoryMobil
) : ViewModel() {

    var listMobilUiState: ListMobilUiState by mutableStateOf(ListMobilUiState.Loading)
        private set

    var currentMerkId by mutableStateOf(0)
        private set

    var currentMerkName by mutableStateOf("")
        private set

    fun setCurrentMerk(merkId: Int, merkName: String) {
        currentMerkId = merkId
        currentMerkName = merkName
    }

    fun loadMobilList(token: String, merkId: Int) {
        viewModelScope.launch {
            listMobilUiState = ListMobilUiState.Loading
            listMobilUiState = try {
                val mobilList = repositoryMobil.getMobilList(token, merkId)
                ListMobilUiState.Success(mobilList)
            } catch (e: IOException) {
                ListMobilUiState.Error
            } catch (e: HttpException) {
                ListMobilUiState.Error
            }
        }
    }

    suspend fun tambahStok(token: String, mobilId: Int): Boolean {
        return try {
            repositoryMobil.tambahStok(token, mobilId)
            true
        } catch (e: Exception) {
            false
        }
    }



}

