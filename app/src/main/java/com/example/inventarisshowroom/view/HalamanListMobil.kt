package com.example.inventarisshowroom.view

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarisshowroom.viewmodel.ListMobilViewModel
import com.example.inventarisshowroom.viewmodel.provider.PenyediaViewModel


@Composable
fun HalamanListMobil(
    merkId: Int,
    merkName: String,
    onBack: () -> Unit,
    onAddMobil: () -> Unit,
    onMobilClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListMobilViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {

}

