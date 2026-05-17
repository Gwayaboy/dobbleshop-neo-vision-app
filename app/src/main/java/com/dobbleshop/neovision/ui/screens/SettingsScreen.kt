package com.dobbleshop.neovision.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Réglages") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // General Settings Section
            SettingsSection(
                title = "Général",
                items = listOf(
                    SettingItem(
                        icon = Icons.Default.PlayArrow,
                        title = "Onboarding",
                        description = "Guide de démarrage",
                        onClick = { /* Navigate to onboarding */ }
                    ),
                    SettingItem(
                        icon = Icons.Default.WaterDrop,
                        title = "Gestion de l'eau",
                        description = "Suivi consommation & alertes",
                        onClick = { /* Navigate to water management */ }
                    ),
                    SettingItem(
                        icon = Icons.Default.Wifi,
                        title = "Communication appareil",
                        description = "Bluetooth & Wi-Fi",
                        onClick = { /* Navigate to device communication */ }
                    ),
                    SettingItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        description = "Alertes & préférences",
                        onClick = { /* Navigate to notifications */ }
                    ),
                    SettingItem(
                        icon = Icons.Default.Group,
                        title = "Multi-utilisateurs",
                        description = "Partage sécurisé (V1.5)",
                        onClick = { /* Navigate to multi-user */ },
                        badge = "Bientôt"
                    )
                )
            )
            
            // Diagnostics Section
            SettingsSection(
                title = "Diagnostics & Matériel",
                items = listOf(
                    SettingItem(
                        icon = Icons.Default.Build,
                        title = "Diagnostics matériels",
                        description = "ESP32-S3 - RPI Zero 2W - capteurs",
                        onClick = { /* Navigate to hardware diagnostics */ }
                    ),
                    SettingItem(
                        icon = Icons.Default.Storage,
                        title = "Réservoirs & Gamelle",
                        description = "Niveaux - HX711 - VL53L0X",
                        onClick = { /* Navigate to reservoirs */ }
                    ),
                    SettingItem(
                        icon = Icons.Default.Security,
                        title = "Sécurité maison",
                        description = "mmWave - captures - événements",
                        onClick = { /* Navigate to home security */ }
                    )
                )
            )
            
            // About Section
            SettingsSection(
                title = "À propos",
                items = listOf(
                    SettingItem(
                        icon = Icons.Default.Info,
                        title = "Version de l'application",
                        description = "1.0.0 (Build 1)",
                        onClick = { }
                    ),
                    SettingItem(
                        icon = Icons.Default.Update,
                        title = "Firmware ESP32-S3",
                        description = "v1.4.2",
                        onClick = { /* Check for updates */ }
                    ),
                    SettingItem(
                        icon = Icons.Default.Update,
                        title = "Firmware RPI Zero 2W",
                        description = "v2.1.0",
                        onClick = { /* Check for updates */ }
                    ),
                    SettingItem(
                        icon = Icons.Default.Help,
                        title = "Aide & Support",
                        description = "Documentation et FAQ",
                        onClick = { /* Navigate to help */ }
                    )
                )
            )
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    items: List<SettingItem>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingsItemRow(item)
                    if (index < items.size - 1) {
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsItemRow(item: SettingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = item.onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    item.badge?.let { badge ->
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = badge,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}

data class SettingItem(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val onClick: () -> Unit,
    val badge: String? = null
)
