package com.example.inventarisshowroom.view

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarisshowroom.viewmodel.LoginViewModel
import com.example.inventarisshowroom.viewmodel.provider.PenyediaViewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.inventarisshowroom.R
import com.example.inventarisshowroom.local.UserPreferences
import com.example.inventarisshowroom.viewmodel.LoginUiState
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment

@Composable
fun HalamanLogin(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val loginUiState = viewModel.loginUiState
    val formState = viewModel.formState
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(loginUiState) {
        when (loginUiState) {
            is LoginUiState.Success -> {
                val response = loginUiState.loginResponse
                userPreferences.saveUserData(
                    token = response.token ?: "",
                    userId = response.user?.id ?: 0,
                    userName = response.user?.nama_lengkap ?: "",
                    userEmail = response.user?.email ?: ""
                )
                Toast.makeText(context, context.getString(R.string.toast_login_berhasil), Toast.LENGTH_SHORT).show()
                viewModel.resetState()
                onLoginSuccess()
            }
            is LoginUiState.Error -> {
                Toast.makeText(context, loginUiState.message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {

        }
    }

}