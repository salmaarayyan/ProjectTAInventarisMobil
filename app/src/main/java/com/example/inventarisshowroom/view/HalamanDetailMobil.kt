package com.example.inventarisshowroom.view

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarisshowroom.viewmodel.DetailMobilViewModel
import com.example.inventarisshowroom.viewmodel.provider.PenyediaViewModel

@Composable
fun HalamanDetailMobil(
    mobilId: Int,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailMobilViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {

}

