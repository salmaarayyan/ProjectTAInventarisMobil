package com.example.inventarisshowroom.view

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarisshowroom.viewmodel.DetailMobilViewModel
import com.example.inventarisshowroom.viewmodel.provider.PenyediaViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.inventarisshowroom.local.UserPreferences
import androidx.compose.material3.*
import androidx.compose.ui.res.stringResource
import com.example.inventarisshowroom.R
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import com.example.inventarisshowroom.viewmodel.DetailMobilUiState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp

@Composable
fun HalamanDetailMobil(
    mobilId: Int,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailMobilViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val token = userPreferences.getToken() ?: ""

    LaunchedEffect(mobilId) {
        viewModel.loadDetailMobil(token, mobilId)
    }

    Scaffold(
        topBar = {
            ShowroomTopAppBar(
                title = stringResource(R.string.detail_mobil),
                canNavigateBack = true,
                onNavigateBack = onBack
            )
        }
    ) { paddingValues ->
        when (val state = viewModel.detailMobilUiState) {
            is DetailMobilUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            is DetailMobilUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            stringResource(R.string.error_load_data),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.loadDetailMobil(token, mobilId) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }
            is DetailMobilUiState.Success -> {
                val mobil = state.mobilDetail

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(dimensionResource(R.dimen.padding_medium))
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = dimensionResource(R.dimen.card_elevation)
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))) {
                            // Title & Merk
                            Text(
                                text = mobil.nama_mobil,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = mobil.nama_merk,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(
                                    vertical = dimensionResource(R.dimen.padding_medium)
                                ),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )

                            // Details Grid
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(
                                    dimensionResource(R.dimen.spacing_large)
                                )
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    DetailItem(
                                        stringResource(R.string.tipe),
                                        mobil.tipe
                                    )
                                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
                                    DetailItem(
                                        stringResource(R.string.tahun),
                                        mobil.tahun.toString()
                                    )
                                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
                                    DetailItem(
                                        stringResource(R.string.warna),
                                        mobil.warna
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    DetailItem(
                                        stringResource(R.string.stok),
                                        context.getString(R.string.stok_unit, mobil.jumlah_stok)
                                    )
                                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
                                    DetailItem(
                                        stringResource(R.string.ditambahkan),
                                        mobil.created_at
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

                            // Harga
                            Text(
                                text = stringResource(R.string.harga),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = formatRupiah(mobil.harga),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

                            // Edit Button
                            Button(
                                onClick = onEdit,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                                Text(
                                    stringResource(R.string.edit_mobil),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_extra_small)))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}


