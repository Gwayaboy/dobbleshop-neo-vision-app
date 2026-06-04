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
import com.dobbleshop.neovision.ui.screens.SettingsDetailScreen
import com.dobbleshop.neovision.ui.screens.ReservoirsDetailScreen
import com.dobbleshop.neovision.ui.screens.HistoryScreen
import com.dobbleshop.neovision.ui.screens.auth.LoginScreen

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
                },
                onNavigateToHistory = {
                    navController.navigate(AppDestination.History.route)
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
            SettingsScreen(
                onOpenDetail = { itemId ->
                    navController.navigate(AppDestination.SettingsDetail.createRoute(itemId))
                },
                onLogout = {
                    navController.navigate(AppDestination.Login.route)
                }
            )
        }

        composable(AppDestination.SettingsDetail.route) { backStackEntry ->
            SettingsDetailScreen(
                itemId = backStackEntry.arguments?.getString("itemId").orEmpty(),
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppDestination.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppDestination.Dashboard.route) {
                        popUpTo(AppDestination.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    // Registration flow is not part of this iteration.
                }
            )
        }
        
        composable(AppDestination.ReservoirsDetail.route) {
            ReservoirsDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(AppDestination.History.route) {
            HistoryScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        // Add more destinations as we build them
    }
}
