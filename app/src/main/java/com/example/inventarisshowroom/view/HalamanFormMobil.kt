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
import android.widget.Toast
import kotlinx.coroutines.launch

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

            // Harga
            OutlinedTextField(
                value = formState.harga,
                onValueChange = { viewModel.updateHarga(it) },
                label = { Text(stringResource(R.string.harga)) },
                placeholder = { Text(stringResource(R.string.harga_hint)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = formState.hargaError != null,
                supportingText = {
                    formState.hargaError?.let {
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

            // Warna
            OutlinedTextField(
                value = formState.warna,
                onValueChange = { viewModel.updateWarna(it) },
                label = { Text(stringResource(R.string.warna)) },
                placeholder = { Text(stringResource(R.string.warna_hint)) },
                isError = formState.warnaError != null,
                supportingText = {
                    formState.warnaError?.let {
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

            // Stok (hanya untuk mode tambah)
            if (!isEditMode) {
                OutlinedTextField(
                    value = formState.stok,
                    onValueChange = { viewModel.updateStok(it) },
                    label = { Text(stringResource(R.string.stok_awal)) },
                    placeholder = { Text(stringResource(R.string.stok_hint)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = formState.stokError != null,
                    supportingText = {
                        formState.stokError?.let {
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

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

            // Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
            ) {
                // Cancel Button
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(0.5f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(stringResource(R.string.batal))
                }

                // Save Button
                Button(
                    onClick = {
                        scope.launch {
                            val success = viewModel.saveMobil(token)
                            if (success) {
                                Toast.makeText(
                                    context,
                                    if (isEditMode) context.getString(R.string.toast_mobil_berhasil_diubah) else context.getString(R.string.toast_mobil_berhasil_ditambah),
                                    Toast.LENGTH_SHORT
                                ).show()
                                onNavigateBack()
                            } else {
                                Toast.makeText(context, context.getString(R.string.toast_mobil_sudah_ada), Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(stringResource(R.string.simpan))
                }
            }
        }
    }
}