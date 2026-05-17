package com.dobbleshop.neovision.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dobbleshop.neovision.R
import com.dobbleshop.neovision.ui.navigation.AppDestination
import com.dobbleshop.neovision.ui.navigation.AppNavHost
import com.dobbleshop.neovision.ui.navigation.bottomNavDestinations

@Composable
fun DobbleShopApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = NavigationBarDefaults.Elevation
            ) {
                bottomNavDestinations.forEach { destination ->
                    val selected = currentDestination?.hierarchy?.any { 
                        it.route == destination.route 
                    } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = when (destination) {
                                    is AppDestination.Dashboard -> Icons.Default.Home
                                    is AppDestination.Pets -> Icons.Default.Pets
                                    is AppDestination.Feeding -> Icons.Default.Restaurant
                                    is AppDestination.Camera -> Icons.Default.Videocam
                                    is AppDestination.Settings -> Icons.Default.Settings
                                    else -> Icons.Default.Home
                                },
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = when (destination) {
                                    is AppDestination.Dashboard -> stringResource(R.string.nav_home)
                                    is AppDestination.Pets -> stringResource(R.string.nav_pets)
                                    is AppDestination.Feeding -> stringResource(R.string.nav_feeding)
                                    is AppDestination.Camera -> stringResource(R.string.nav_camera)
                                    is AppDestination.Settings -> stringResource(R.string.nav_settings)
                                    else -> ""
                                }
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
