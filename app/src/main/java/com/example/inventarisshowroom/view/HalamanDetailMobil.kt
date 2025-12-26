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

        }

    }
}

