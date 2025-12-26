package com.example.inventarisshowroom.view


import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarisshowroom.viewmodel.LoginViewModel
import com.example.inventarisshowroom.viewmodel.provider.PenyediaViewModel

@Composable
fun HalamanLogin(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {

}