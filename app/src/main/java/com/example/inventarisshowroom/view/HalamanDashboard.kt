package com.example.inventarisshowroom.view


import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarisshowroom.viewmodel.DashboardViewModel
import com.example.inventarisshowroom.viewmodel.provider.PenyediaViewModel

@Composable
fun HalamanDashboard(
    onMerkClick: (Int, String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(factory = PenyediaViewModel.Factory)
){}