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
    }
}