package com.dobbleshop.neovision.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.dobbleshop.neovision.ui.navigation.AppDestination

/**
 * Bottom navigation bar for main app screens
 */
@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (AppDestination) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.destination.route,
                onClick = { onNavigate(item.destination) }
            )
        }
    }
}

private val bottomNavItems = listOf(
    BottomNavItem(AppDestination.Dashboard, "Accueil", Icons.Default.Home),
    BottomNavItem(AppDestination.Feeding, "Repas", Icons.Default.Restaurant),
    BottomNavItem(AppDestination.Camera, "Caméra", Icons.Default.Videocam),
    BottomNavItem(AppDestination.Settings, "Réglages", Icons.Default.Settings)
)

private data class BottomNavItem(
    val destination: AppDestination,
    val title: String,
    val icon: ImageVector
)
