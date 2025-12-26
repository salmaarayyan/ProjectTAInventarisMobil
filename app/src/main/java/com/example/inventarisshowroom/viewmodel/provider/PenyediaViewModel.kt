package com.example.inventarisshowroom.viewmodel.provider

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventarisshowroom.viewmodel.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.inventarisshowroom.ShowroomApplication

object PenyediaViewModel {

    val Factory = viewModelFactory {

        // LoginViewModel
        initializer {
            LoginViewModel(
                repositoryAuth = aplikasiShowroom().container.repositoryAuth
            )
        }

        // DashboardViewModel
        initializer {
            DashboardViewModel(
                repositoryMerk = aplikasiShowroom().container.repositoryMerk
            )
        }

        // ListMobilViewModel
        initializer {
            ListMobilViewModel(
                repositoryMobil = aplikasiShowroom().container.repositoryMobil
            )
        }

        // DetailMobilViewModel
        initializer {
            DetailMobilViewModel(
                repositoryMobil = aplikasiShowroom().container.repositoryMobil
            )
        }

        // FormMobilViewModel
        initializer {
            FormMobilViewModel(
                repositoryMobil = aplikasiShowroom().container.repositoryMobil
            )
        }
    }
}

fun CreationExtras.aplikasiShowroom(): ShowroomApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ShowroomApplication)

