package com.dobbleshop.neovision.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onOpenDetail: (String) -> Unit = {},
    onOpenSmartRation: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val pageBackground = Color(0xFFF3F5F8)

    Scaffold(containerColor = pageBackground) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Réglages",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "DOBBLESHOP NEO VISION",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp,
                    color = Color(0xFF667085)
                )
                Text(
                    text = "DOBBLESHOP NEO VISION",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.8.sp,
                    color = Color(0xFF667085)
                )
            }

            SettingsSection(
                title = null,
                items = listOf(
                    SettingItem(Icons.Default.RocketLaunch, Color(0xFF2970FF), "Onboarding", "Guide de démarrage", { onOpenDetail("onboarding") }),
                    SettingItem(Icons.Default.WaterDrop, Color(0xFF22A6B3), "Gestion de l'eau", "Suivi consommation & alertes", { onOpenDetail("water_management") }),
                    SettingItem(Icons.Default.Wifi, Color(0xFF2F80ED), "Communication appareil", "Bluetooth & Wi-Fi", { onOpenDetail("communication") }),
                    SettingItem(Icons.Default.Notifications, Color(0xFFF59E0B), "Notifications", "Alertes & préférences", { onOpenDetail("notifications") }),
                    SettingItem(Icons.Default.Groups, Color(0xFF5B50D6), "Multi-utilisateurs", "Partage sécurisé (V1.5)", { onOpenDetail("multi_user") })
                )
            )

            SettingsSection(
                title = "MATÉRIEL & DIAGNOSTICS",
                items = listOf(
                    SettingItem(Icons.Default.MedicalServices, Color(0xFFEF4444), "Diagnostics matériels", "ESP32-S3 · RPi Zero 2W · capteurs", { onOpenDetail("hardware_diagnostics") }),
                    SettingItem(Icons.Default.Layers, Color(0xFF3B82F6), "Réservoirs & Gamelle", "Niveaux · HX711 · VL53L0X", { onOpenDetail("reservoirs_bowl") }),
                    SettingItem(Icons.Default.Shield, Color(0xFF22C55E), "Sécurité maison", "mmWave · captures · événements", { onOpenDetail("home_security") }),
                    SettingItem(Icons.Default.History, Color(0xFFF97316), "Historique complet", "Distributions · graphiques", { onOpenDetail("full_history") }),
                    SettingItem(Icons.Default.PieChart, Color(0xFF2F7AE5), "Ration intelligente", "RER/DER · V1.5", onOpenSmartRation),
                    SettingItem(Icons.Default.SystemUpdateAlt, Color(0xFFF59E0B), "Mise à jour firmware OTA", "v1.4.2 → v1.5.0 disponible", { onOpenDetail("firmware_ota") }, badge = "Maj")
                )
            )

            SettingsSection(
                title = "COMPTE",
                items = listOf(
                    SettingItem(Icons.Default.Person, Color(0xFF6366F1), "Compte utilisateur", "Carl Tacita", { onOpenDetail("user_account") }),
                    SettingItem(Icons.Default.Cloud, Color(0xFF06B6D4), "Synchronisation", "Dernière synchro : il y a 2 min", { onOpenDetail("synchronization") }),
                    SettingItem(Icons.Default.Language, Color(0xFF22C55E), "Langue & Région", "Français (France)", { onOpenDetail("language_region") }),
                    SettingItem(Icons.Default.Settings, Color(0xFF6B7280), "Support & Aide", "FAQ · contact@dobbleshop.fr", { onOpenDetail("support_help") })
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text("🐾", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "DOBBLESHOP NEO VISION",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF475467),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "v1.4.2 · ESP32-S3 · RPi Zero 2W · V1+V1.5",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF98A2B3)
                )

                TextButton(onClick = onLogout) {
                    Text("Déconnexion", color = Color(0xFFB42318))
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String?,
    items: List<SettingItem>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                color = Color(0xFF667085),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingsItemRow(item)
                    if (index < items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 84.dp),
                            color = Color(0xFFE9EEF5)
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
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(10.dp),
                color = item.iconBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
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
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827)
                    )
                    item.badge?.let { badge ->
                        Surface(
                            color = Color(0xFFFFEAB6),
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Text(
                                text = badge,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFA15C00),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF667085)
                )
            }
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = Color(0xFFD0D5DD)
        )
    }
}

data class SettingItem(
    val icon: ImageVector,
    val iconBackground: Color,
    val title: String,
    val description: String,
    val onClick: () -> Unit,
    val badge: String? = null
)
