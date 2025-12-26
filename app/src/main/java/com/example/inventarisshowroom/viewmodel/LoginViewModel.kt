package com.example.inventarisshowroom.viewmodel

import com.example.inventarisshowroom.modeldata.LoginResponse
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventarisshowroom.repositori.RepositoryAuth


sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val loginResponse: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null
)

class LoginViewModel(
    private val repositoryAuth: RepositoryAuth
) : ViewModel() {

    var loginUiState: LoginUiState by mutableStateOf(LoginUiState.Idle)
        private set

    var formState by mutableStateOf(LoginFormState())
        private set

    // Update email
    fun updateEmail(email: String) {
        formState = formState.copy(
            email = email,
            emailError = validateEmail(email)
        )
    }

    // Update password
    fun updatePassword(password: String) {
        formState = formState.copy(
            password = password,
            passwordError = validatePassword(password)
        )
    }

    // Validasi email real-time
    private fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> "Email harus diisi"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Format email tidak valid"
            else -> null
        }
    }

    // Validasi password real-time
    private fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Password harus diisi"
            else -> null
        }
    }


}


