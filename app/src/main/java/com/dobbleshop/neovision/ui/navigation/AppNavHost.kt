package com.dobbleshop.neovision.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dobbleshop.neovision.ui.screens.DashboardScreen
import com.dobbleshop.neovision.ui.screens.CameraScreen
import com.dobbleshop.neovision.ui.screens.FeedingScreen
import com.dobbleshop.neovision.ui.screens.SettingsScreen
import com.dobbleshop.neovision.ui.screens.ReservoirsDetailScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Dashboard.route,
        modifier = modifier
    ) {
        composable(AppDestination.Dashboard.route) {
            DashboardScreen(
                onNavigateToReservoirs = {
                    navController.navigate(AppDestination.ReservoirsDetail.route)
                }
            )
        }
        
        composable(AppDestination.Feeding.route) {
            FeedingScreen()
        }
        
        composable(AppDestination.Camera.route) {
            CameraScreen()
        }
        
        composable(AppDestination.Settings.route) {
            SettingsScreen()
        }
        
        composable(AppDestination.ReservoirsDetail.route) {
            ReservoirsDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // Add more destinations as we build them
    }
}
