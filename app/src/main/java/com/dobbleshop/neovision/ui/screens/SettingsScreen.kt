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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onOpenDetail: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
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
            Text(
                text = "DOBBLESHOP NEO VISION",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // General Settings Section
            SettingsSection(
                title = "Général",
                items = listOf(
                    SettingItem(
                        icon = Icons.Default.PlayArrow,
                        title = "Onboarding",
                        description = "Guide de démarrage",
                        onClick = { onOpenDetail("onboarding") }
                    ),
                    SettingItem(
                        icon = Icons.Default.WaterDrop,
                        title = "Gestion de l'eau",
                        description = "Suivi consommation & alertes",
                        onClick = { onOpenDetail("water_management") }
                    ),
                    SettingItem(
                        icon = Icons.Default.Wifi,
                        title = "Communication appareil",
                        description = "Bluetooth & Wi-Fi",
                        onClick = { onOpenDetail("communication") }
                    ),
                    SettingItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        description = "Alertes & préférences",
                        onClick = { onOpenDetail("notifications") }
                    ),
                    SettingItem(
                        icon = Icons.Default.Group,
                        title = "Multi-utilisateurs",
                        description = "Partage sécurisé (V1.5)",
                        onClick = { onOpenDetail("multi_user") },
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
                        onClick = { onOpenDetail("hardware_diagnostics") }
                    ),
                    SettingItem(
                        icon = Icons.Default.Storage,
                        title = "Réservoirs & Gamelle",
                        description = "Niveaux - HX711 - VL53L0X",
                        onClick = { onOpenDetail("reservoirs_bowl") }
                    ),
                    SettingItem(
                        icon = Icons.Default.Security,
                        title = "Sécurité maison",
                        description = "mmWave - captures - événements",
                        onClick = { onOpenDetail("home_security") }
                    ),
                    SettingItem(
                        icon = Icons.Default.History,
                        title = "Historique complet",
                        description = "Distributions - graphiques",
                        onClick = { onOpenDetail("full_history") }
                    ),
                    SettingItem(
                        icon = Icons.Default.Analytics,
                        title = "Ration intelligente",
                        description = "RER/DER - V1.5",
                        onClick = { onOpenDetail("smart_ration") }
                    ),
                    SettingItem(
                        icon = Icons.Default.SystemUpdate,
                        title = "Mise à jour firmware OTA",
                        description = "v1.4.2 → v1.5.0 disponible",
                        onClick = { onOpenDetail("firmware_ota") },
                        badge = "Maj"
                    )
                )
            )

            SettingsSection(
                title = "Compte",
                items = listOf(
                    SettingItem(
                        icon = Icons.Default.Person,
                        title = "Compte utilisateur",
                        description = "Carl Tacita",
                        onClick = { onOpenDetail("user_account") }
                    ),
                    SettingItem(
                        icon = Icons.Default.Cloud,
                        title = "Synchronisation",
                        description = "Dernière synchro : il y a 2 min",
                        onClick = { onOpenDetail("synchronization") }
                    ),
                    SettingItem(
                        icon = Icons.Default.Language,
                        title = "Langue & Région",
                        description = "Français (France)",
                        onClick = { onOpenDetail("language_region") }
                    ),
                    SettingItem(
                        icon = Icons.Default.SupportAgent,
                        title = "Support & Aide",
                        description = "FAQ - contact@dobbleshop.fr",
                        onClick = { onOpenDetail("support_help") }
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
                        onClick = { onOpenDetail("firmware_esp") }
                    ),
                    SettingItem(
                        icon = Icons.Default.Update,
                        title = "Firmware RPI Zero 2W",
                        description = "v2.1.0",
                        onClick = { onOpenDetail("firmware_rpi") }
                    ),
                    SettingItem(
                        icon = Icons.Default.Help,
                        title = "Aide & Support",
                        description = "Documentation et FAQ",
                        onClick = { onOpenDetail("support_help") }
                    )
                )
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("🐾", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = "DOBBLESHOP NEO VISION",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "v1.4.2 · ESP32-S3 · RPi Zero 2W · V1+V1.5",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )

                    OutlinedButton(
                        onClick = onLogout,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFB3261E)
                        )
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Déconnexion")
                    }
                }
            }
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
