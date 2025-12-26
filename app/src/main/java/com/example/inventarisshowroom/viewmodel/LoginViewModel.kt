package com.example.inventarisshowroom.viewmodel

import com.example.inventarisshowroom.modeldata.LoginResponse


sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val loginResponse: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

