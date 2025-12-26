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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType


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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(dimensionResource(R.dimen.padding_medium))
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
        ) {
            // Nama Mobil
            OutlinedTextField(
                value = formState.namaMobil,
                onValueChange = { viewModel.updateNamaMobil(it) },
                label = { Text(stringResource(R.string.nama_mobil)) },
                placeholder = { Text(stringResource(R.string.nama_mobil_hint)) },
                isError = formState.namaMobilError != null,
                supportingText = {
                    formState.namaMobilError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )

            // Merk (Read-only)
            OutlinedTextField(
                value = formState.merkName,
                onValueChange = {},
                label = { Text(stringResource(R.string.merk)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            // Tipe Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = formState.tipe,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.pilih_tipe)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    isError = formState.tipeError != null,
                    supportingText = {
                        formState.tipeError?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    tipeMobilList.forEach { tipe ->
                        DropdownMenuItem(
                            text = { Text(tipe) },
                            onClick = {
                                viewModel.updateTipe(tipe)
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Tahun
            OutlinedTextField(
                value = formState.tahun,
                onValueChange = { viewModel.updateTahun(it) },
                label = { Text(stringResource(R.string.tahun)) },
                placeholder = { Text(stringResource(R.string.tahun_hint)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = formState.tahunError != null,
                supportingText = {
                    formState.tahunError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )

        }
    }
}