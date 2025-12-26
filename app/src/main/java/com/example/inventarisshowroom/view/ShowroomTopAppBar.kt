package com.example.inventarisshowroom.view


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowroomTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    showLogout: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onLogout: () -> Unit = {}
){

}