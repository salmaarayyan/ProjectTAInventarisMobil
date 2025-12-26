package com.example.inventarisshowroom.view

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarisshowroom.viewmodel.ListMobilViewModel
import com.example.inventarisshowroom.viewmodel.provider.PenyediaViewModel
import com.example.inventarisshowroom.local.UserPreferences
import com.example.inventarisshowroom.viewmodel.ListMobilUiState
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.inventarisshowroom.R
import com.example.inventarisshowroom.modeldata.DataMobil

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
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val token = userPreferences.getToken() ?: ""
    val scope = rememberCoroutineScope()

    LaunchedEffect(merkId, merkName) {
        viewModel.setCurrentMerk(merkId, merkName)
        viewModel.loadMobilList(token, merkId)
    }

    Scaffold(
        topBar = {
            ShowroomTopAppBar(
                title = context.getString(R.string.daftar_mobil, merkName),
                canNavigateBack = true,
                onNavigateBack = onBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddMobil,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.tambah_mobil),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->

        when (val state = viewModel.listMobilUiState) {
            is ListMobilUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is ListMobilUiState.Error -> {
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
                            onClick = { viewModel.loadMobilList(token, merkId) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }

            is ListMobilUiState.Success -> {
                if (state.mobilList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.belum_ada_mobil),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = stringResource(R.string.tambah_mobil_baru),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = paddingValues,
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
                    ) {
                        items(state.mobilList) { mobil ->
                            MobilCard(
                                mobil = mobil,
                                onDetailClick = { onMobilClick(mobil.id) },
                                onTambahStok = {
                                    scope.launch {
                                        val success = viewModel.tambahStok(token, mobil.id)
                                        if (success) {
                                            Toast.makeText(context, context.getString(R.string.toast_stok_berhasil_ditambah), Toast.LENGTH_SHORT).show()
                                            viewModel.loadMobilList(token, merkId)
                                        } else {
                                            Toast.makeText(context, context.getString(R.string.toast_gagal_stok_tambah), Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                onKurangiStok = {
                                    scope.launch {
                                        val success = viewModel.kurangiStok(token, mobil.id)
                                        if (success) {
                                            Toast.makeText(context, context.getString(R.string.toast_stok_berhasil_dikurangi), Toast.LENGTH_SHORT).show()
                                            viewModel.loadMobilList(token, merkId)
                                        } else {
                                            Toast.makeText(context, context.getString(R.string.toast_stok_nol), Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                onDelete = {
                                    scope.launch {
                                        val success = viewModel.deleteMobil(token, mobil.id)
                                        if (success) {
                                            Toast.makeText(context, context.getString(R.string.toast_mobil_berhasil_dihapus), Toast.LENGTH_SHORT).show()
                                            viewModel.loadMobilList(token, merkId)
                                        } else {
                                            Toast.makeText(context, context.getString(R.string.toast_gagal_mobil), Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MobilCard(
    mobil: DataMobil,
    onDetailClick: () -> Unit,
    onTambahStok: () -> Unit,
    onKurangiStok: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showTambahStokDialog by remember { mutableStateOf(false) }
    var showKurangiStokDialog by remember { mutableStateOf(false) }



}


