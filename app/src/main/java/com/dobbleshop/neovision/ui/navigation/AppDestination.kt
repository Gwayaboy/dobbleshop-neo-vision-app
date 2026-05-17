package com.dobbleshop.neovision.ui.navigation

/**
 * Navigation destinations for the DobbleShop app
 */
sealed class AppDestination(val route: String) {
    data object Dashboard : AppDestination("dashboard")
    data object Pets : AppDestination("pets")
    data object Feeding : AppDestination("feeding")
    data object Camera : AppDestination("camera")
    data object Settings : AppDestination("settings")
    
    // Detail screens
    data object PetDetail : AppDestination("pet_detail/{petId}") {
        fun createRoute(petId: String) = "pet_detail/$petId"
    }
    data object DeviceDetail : AppDestination("device_detail")
    data object History : AppDestination("history")
}

/**
 * Bottom navigation items
 */
val bottomNavDestinations = listOf(
    AppDestination.Dashboard,
    AppDestination.Pets,
    AppDestination.Feeding,
    AppDestination.Camera,
    AppDestination.Settings
)
