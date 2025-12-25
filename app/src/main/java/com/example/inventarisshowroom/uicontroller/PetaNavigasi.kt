package com.example.inventarisshowroom.uicontroller

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventarisshowroom.uicontroller.route.*
import com.example.inventarisshowroom.view.*

@Composable
fun PetaNavigasi(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiLogin.route,
        modifier = modifier
    ) {
        // Login Screen
        composable(route = DestinasiLogin.route) {
            HalamanLogin(
                onLoginSuccess = {
                    navController.navigate(DestinasiDashboard.route) {
                        popUpTo(DestinasiLogin.route) { inclusive = true }
                    }
                }
            )
        }
        // Dashboard Screen (List Merk)
        composable(route = DestinasiDashboard.route) {
            HalamanDashboard(
                onMerkClick = { merkId, merkName ->
                    navController.navigate(
                        DestinasiListMobil.createRoute(merkId, merkName)
                    )
                },
                onLogout = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        // List Mobil Screen
        composable(
            route = DestinasiListMobil.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiListMobil.MERK_ID) { type = NavType.IntType },
                navArgument(DestinasiListMobil.MERK_NAME) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val merkId = backStackEntry.arguments?.getInt(DestinasiListMobil.MERK_ID) ?: 0
            val merkName = backStackEntry.arguments?.getString(DestinasiListMobil.MERK_NAME) ?: ""

            HalamanListMobil(
                merkId = merkId,
                merkName = merkName,
                onBack = { navController.popBackStack() },
                onAddMobil = {
                    navController.navigate(
                        DestinasiFormMobil.createRouteAdd(merkId, merkName)
                    )
                },
                onMobilClick = { mobilId ->
                    navController.navigate(
                        DestinasiDetailMobil.createRoute(mobilId)
                    )
                }
            )
        }
        // Detail Mobil Screen
        composable(
            route = DestinasiDetailMobil.routeWithArgs,
            arguments = listOf(
                navArgument(DestinasiDetailMobil.MOBIL_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val mobilId = backStackEntry.arguments?.getInt(DestinasiDetailMobil.MOBIL_ID) ?: 0

            HalamanDetailMobil(
                mobilId = mobilId,
                onBack = { navController.popBackStack() },
                onEdit = {
                    navController.navigate(
                        DestinasiFormMobil.createRouteEdit(mobilId)
                    )
                }
            )
        }
        // Form Mobil - Add Mode
        composable(
            route = DestinasiFormMobil.routeAdd,
            arguments = listOf(
                navArgument(DestinasiFormMobil.MERK_ID) { type = NavType.IntType },
                navArgument(DestinasiFormMobil.MERK_NAME) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val merkId = backStackEntry.arguments?.getInt(DestinasiFormMobil.MERK_ID) ?: 0
            val merkName = backStackEntry.arguments?.getString(DestinasiFormMobil.MERK_NAME) ?: ""

            HalamanFormMobil(
                isEditMode = false,
                merkId = merkId,
                merkName = merkName,
                mobilId = null,
                onBack = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        // Form Mobil - Edit Mode
        composable(
            route = DestinasiFormMobil.routeEdit,
            arguments = listOf(
                navArgument(DestinasiFormMobil.MOBIL_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val mobilId = backStackEntry.arguments?.getInt(DestinasiFormMobil.MOBIL_ID) ?: 0

            HalamanFormMobil(
                isEditMode = true,
                merkId = 0,
                merkName = "",
                mobilId = mobilId,
                onBack = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}