package com.example.inventarisshowroom.view

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarisshowroom.viewmodel.DetailMobilViewModel
import com.example.inventarisshowroom.viewmodel.FormMobilViewModel
import com.example.inventarisshowroom.viewmodel.provider.PenyediaViewModel
import com.example.inventarisshowroom.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.inventarisshowroom.local.UserPreferences
import com.example.inventarisshowroom.viewmodel.DetailMobilUiState

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
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val token = userPreferences.getToken() ?: ""
    val scope = rememberCoroutineScope()
    val formState = viewModel.formState

    val tipeMobilList = listOf(
        "Sedan", "SUV", "MPV", "Crossover", "Hatchback",
        "Off Road", "Sport", "Pickup", "Electric", "Hybrid", "LCGC"
    )

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(isEditMode, mobilId) {
        if (isEditMode && mobilId != null) {
            detailViewModel.loadDetailMobil(token, mobilId)
        } else {
            viewModel.setAddMode(merkId, merkName)
        }
    }

    LaunchedEffect(detailViewModel.detailMobilUiState) {
        if (isEditMode && detailViewModel.detailMobilUiState is DetailMobilUiState.Success) {
            val mobil = (detailViewModel.detailMobilUiState as DetailMobilUiState.Success).mobilDetail
            viewModel.setEditMode(
                id = mobil.id,
                namaMobil = mobil.nama_mobil,
                merkId = mobil.merk_id,
                merkName = mobil.nama_merk,
                tipe = mobil.tipe,
                tahun = mobil.tahun,
                harga = mobil.harga,
                warna = mobil.warna
            )
        }
    }

    Scaffold(
        topBar = {
            ShowroomTopAppBar(
                title = if (isEditMode) stringResource(R.string.form_edit_mobil) else stringResource(R.string.form_tambah_mobil),
                canNavigateBack = true,
                onNavigateBack = onBack
            )
        }
    ) { paddingValues ->


    }
}