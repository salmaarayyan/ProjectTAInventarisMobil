package com.example.inventarisshowroom.view

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventarisshowroom.viewmodel.ListMobilViewModel
import com.example.inventarisshowroom.viewmodel.provider.PenyediaViewModel
import com.example.inventarisshowroom.local.UserPreferences


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

}

