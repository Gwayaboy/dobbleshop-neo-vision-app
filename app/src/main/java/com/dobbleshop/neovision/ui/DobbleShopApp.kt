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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
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
    val showBottomBar = bottomNavDestinations.any { it.route == currentDestination?.route }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    tonalElevation = 8.dp
                ) {
                    bottomNavDestinations.forEach { destination ->
                        val selected = currentDestination?.route == destination.route

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = when (destination) {
                                        is AppDestination.Dashboard -> Icons.Default.Home
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
        }
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
