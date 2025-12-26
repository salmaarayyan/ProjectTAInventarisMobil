package com.example.inventarisshowroom.view

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarisshowroom.viewmodel.DashboardViewModel
import com.example.inventarisshowroom.viewmodel.provider.PenyediaViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.inventarisshowroom.local.UserPreferences
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.ui.res.stringResource
import com.example.inventarisshowroom.R
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.inventarisshowroom.viewmodel.DashboardUiState
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.DirectionsCar
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.text.font.FontStyle
import com.example.inventarisshowroom.modeldata.DataMerk

@Composable
fun HalamanDashboard(
    onMerkClick: (Int, String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val token = userPreferences.getToken() ?: ""
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadMerkList(token)
    }

    Scaffold(
        topBar = {
            ShowroomTopAppBar(
                title = stringResource(R.string.dashboard),
                canNavigateBack = false,
                showLogout = true,
                onLogout = { showLogoutDialog = true }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddDialog() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.tambah_merk),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        when (val state = viewModel.dashboardUiState) {
            is DashboardUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is DashboardUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.error_load_data),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.loadMerkList(token) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }

            is DashboardUiState.Success -> {
                if (state.merkList.isEmpty()) {
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
                                text = stringResource(R.string.belum_ada_merk),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = stringResource(R.string.tambah_merk_baru),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = paddingValues,
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.merkList) { merk ->
                            MerkCard(
                                merk = merk,
                                onClick = { onMerkClick(merk.id, merk.nama_merk) },
                                onEdit = { viewModel.openEditDialog(merk.id, merk.nama_merk) },
                                onDelete = {
                                    scope.launch {
                                        val success = viewModel.deleteMerk(token, merk.id)
                                        if (success) {
                                            Toast.makeText(context, context.getString(R.string.toast_merk_berhasil_dihapus), Toast.LENGTH_SHORT).show()
                                            viewModel.loadMerkList(token)
                                        } else {
                                            Toast.makeText(context, context.getString(R.string.toast_gagal_merk), Toast.LENGTH_SHORT).show()
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

    // Dialog Add/Edit Merk
    if (viewModel.isDialogOpen) {
        MerkDialog(
            title = if (viewModel.editMerkId == null) stringResource(R.string.tambah_merk) else stringResource(R.string.edit_merk),
            namaMerk = viewModel.merkFormState.namaMerk,
            error = viewModel.merkFormState.namaMerkError,
            onNameChange = { viewModel.updateNamaMerk(it) },
            onDismiss = { viewModel.closeDialog() },
            onSave = {
                scope.launch {
                    val success = viewModel.saveMerk(token)
                    if (success) {
                        Toast.makeText(
                            context,
                            if (viewModel.editMerkId == null) context.getString(R.string.toast_merk_berhasil_ditambah) else context.getString(R.string.toast_merk_berhasil_diubah),
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.closeDialog()
                        viewModel.loadMerkList(token)
                    } else {
                        Toast.makeText(context, context.getString(R.string.toast_merk_sudah_ada), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(stringResource(R.string.dialog_yakin_logout)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        userPreferences.clearUserData()
                        Toast.makeText(context, context.getString(R.string.toast_logout_berhasil), Toast.LENGTH_SHORT).show()
                        onLogout()
                    }
                ) {
                    Text(
                        stringResource(R.string.dialog_ya),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(
                        stringResource(R.string.dialog_tidak),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

@Composable
fun MerkCard(
    merk: DataMerk,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ===== 🚗 ICON MOBIL (STATIC) =====
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            // Content Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = merk.nama_merk,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = context.getString(R.string.jumlah_mobil, merk.jumlah_mobil),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.klik_untuk_melihat_mobil),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    fontStyle = FontStyle.Italic
                )
            }

            // Action Buttons
            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit_merk),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.hapus_merk),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.dialog_yakin_hapus_merk)) },
            text = {
                Text(context.getString(R.string.dialog_semua_mobil_terhapus, merk.nama_merk))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text(
                        stringResource(R.string.dialog_hapus),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        stringResource(R.string.dialog_tidak),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

@Composable
fun MerkDialog(
    title: String,
    namaMerk: String,
    error: String?,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {

}
