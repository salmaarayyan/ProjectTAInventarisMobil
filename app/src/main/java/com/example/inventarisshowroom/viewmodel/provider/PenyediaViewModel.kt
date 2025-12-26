package com.example.inventarisshowroom.viewmodel.provider


import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventarisshowroom.viewmodel.*

object PenyediaViewModel {

    val Factory = viewModelFactory {

        // LoginViewModel
        initializer {
            LoginViewModel(
                repositoryAuth = aplikasiShowroom().container.repositoryAuth
            )
        }
    }
}

