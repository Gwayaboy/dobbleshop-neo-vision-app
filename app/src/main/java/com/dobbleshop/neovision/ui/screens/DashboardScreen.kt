package com.dobbleshop.neovision.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dobbleshop.neovision.data.model.BowlStatus
import com.dobbleshop.neovision.data.model.DeviceStatus
import java.text.SimpleDateFormat
import java.util.*

/**
 * Home Dashboard Screen
 * Corresponds to specification section 9.3
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToDevice: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {}
) {
    // Mock device status - in real app would come from ViewModel
    val deviceStatus = remember {
        DeviceStatus(
            deviceId = "dev_001",
            bowlWeightGrams = 0f,
            foodReservoirPercent = 62,
            foodReservoirGrams = 1240,
            foodReservoirDaysRemaining = 12,
            waterReservoirPercent = 78,
            waterReservoirMl = 1560,
            waterReservoirDaysRemaining = 15,
            bowlStatus = BowlStatus.EMPTY,
            deviceTime = System.currentTimeMillis(),
            presenceDetected = false,
            presenceIntensity = null,
            cameraStatus = com.dobbleshop.neovision.data.model.CameraStatus.OK,
            batteryPercent = 74,
            isPlugged = false,
            uptime = 86400000,
            wifiRssi = -58,
            internalTemperature = 42.5f
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("DOBBLESHOP", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "NEO VISION",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.error
                    ) {
                        IconButton(onClick = { /* TODO: Open notifications */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Device status card
            DeviceStatusCard(
                deviceStatus = deviceStatus,
                isOnline = true,
                variant = "NEO VISION CAT"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* TODO: Feed now */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Restaurant, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Nourrir maintenant")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* TODO: Water */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.WaterDrop, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Eau")
                }
                
                OutlinedButton(
                    onClick = { /* TODO: Camera */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Videocam, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Caméra")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Reservoir levels
            ReservoirLevelsCard(deviceStatus)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Active animal (mock)
            ActiveAnimalCard(
                petName = "Léo",
                onChangePet = { /* TODO */ }
            )
        }
    }
}

@Composable
private fun DeviceStatusCard(
    deviceStatus: DeviceStatus,
    isOnline: Boolean,
    variant: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "DOBBLESHOP",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = variant,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Surface(
                    color = if (isOnline) Color(0xFF4CAF50) else Color.Gray,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = if (isOnline) "● En ligne" else "● Hors ligne",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Statut appareil",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Status grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusItem(
                    icon = Icons.Default.BatteryFull,
                    label = "Batterie",
                    value = "${deviceStatus.batteryPercent}%",
                    status = "OK",
                    modifier = Modifier.weight(1f)
                )
                
                StatusItem(
                    icon = Icons.Default.Restaurant,
                    label = "Trémie croquettes",
                    value = "Moyen",
                    detail = "${deviceStatus.foodReservoirGrams}g restants",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusItem(
                    icon = Icons.Default.WaterDrop,
                    label = "Réservoir eau",
                    value = "Optimal",
                    detail = "${deviceStatus.waterReservoirPercent}% - ${deviceStatus.waterReservoirMl}ml",
                    modifier = Modifier.weight(1f)
                )
                
                StatusItem(
                    icon = Icons.Default.Scale,
                    label = "Gamelle (HX711)",
                    value = "${deviceStatus.bowlWeightGrams.toInt()}g",
                    detail = deviceStatus.bowlStatus.name,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Heure machine",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(deviceStatus.deviceTime)),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "FW v1.4.2",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StatusItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    detail: String? = null,
    status: String? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            if (detail != null) {
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (status != null) {
                Surface(
                    color = Color(0xFF4CAF50),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = status,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ReservoirLevelsCard(deviceStatus: DeviceStatus) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Niveaux des réservoirs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Details")
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Food reservoir
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Croquettes (VL53L0X)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${deviceStatus.foodReservoirPercent}% · ${deviceStatus.foodReservoirGrams}g",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            LinearProgressIndicator(
                progress = { deviceStatus.foodReservoirPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Water reservoir
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Eau",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${deviceStatus.waterReservoirPercent}% · ${deviceStatus.waterReservoirMl}ml",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            LinearProgressIndicator(
                progress = { deviceStatus.waterReservoirPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            )
        }
    }
}

@Composable
private fun ActiveAnimalCard(
    petName: String,
    onChangePet: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Animal actif",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = petName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            TextButton(onClick = onChangePet) {
                Text("Changer")
            }
        }
    }
}

