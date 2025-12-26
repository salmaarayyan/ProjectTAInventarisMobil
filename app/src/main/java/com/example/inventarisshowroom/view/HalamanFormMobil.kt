package com.example.inventarisshowroom.view

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarisshowroom.viewmodel.DetailMobilViewModel
import com.example.inventarisshowroom.viewmodel.FormMobilViewModel
import com.example.inventarisshowroom.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanFormMobil(
    isEditMode: Boolean,
    merkId: Int,
    merkName: String,
    mobilId: Int?,
    onBack: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FormMobilViewModel = viewModel(factory = PenyediaViewModel.Factory),
    detailViewModel: DetailMobilViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {

}