package com.dobbleshop.neovision.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.dobbleshop.neovision.data.model.BowlStatus
import com.dobbleshop.neovision.data.model.DeviceStatus
import com.dobbleshop.neovision.data.model.Pet
import com.dobbleshop.neovision.ui.components.AddPetDialog
import com.dobbleshop.neovision.ui.viewmodel.PetsViewModel
import com.dobbleshop.neovision.ui.viewmodel.PetsUiState
import com.dobbleshop.neovision.ui.viewmodel.DashboardViewModel
import com.dobbleshop.neovision.data.connectivity.ConnectionState
import java.text.SimpleDateFormat
import java.util.*

/**
 * Home Dashboard Screen
 * Corresponds to specification section 9.3
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    petsViewModel: PetsViewModel = hiltViewModel(),
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToReservoirs: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {}
) {
    val petsUiState by petsViewModel.uiState.collectAsState()
    val dashboardUiState by dashboardViewModel.uiState.collectAsState()
    val connectionState by dashboardViewModel.connectionState.collectAsState()
    val isNetworkAvailable by dashboardViewModel.isNetworkAvailable.collectAsState()
    
    var showAnimalDialog by remember { mutableStateOf(false) }
    var showAddPetDialog by remember { mutableStateOf(false) }
    
    // Auto-connect to device when screen loads
    LaunchedEffect(Unit) {
        if (connectionState is ConnectionState.Disconnected) {
            dashboardViewModel.connectToDevice("dev_001", preferBluetooth = false)
        }
    }
    
    // Get the first active pet or create a mock pet
    val activePet = when (val state = petsUiState) {
        is PetsUiState.Success -> {
            if (state.pets.isEmpty()) {
                // Mock pet when database is empty
                Pet(
                    id = "0",
                    name = "Léo",
                    species = com.dobbleshop.neovision.data.model.Species.CAT,
                    breed = "Chat Domestique",
                    weightKg = 4.5f,
                    ageMonths = 36,
                    sex = com.dobbleshop.neovision.data.model.Sex.MALE,
                    isSterilized = true,
                    activityLevel = com.dobbleshop.neovision.data.model.ActivityLevel.NORMAL,
                    nutritionalGoal = com.dobbleshop.neovision.data.model.NutritionalGoal.MAINTAIN,
                    deviceId = "dev_001"
                )
            } else {
                state.pets.firstOrNull()
            }
        }
        else -> {
            // Mock pet for other states
            Pet(
                    id = "0",
                    name = "Léo",
                    species = com.dobbleshop.neovision.data.model.Species.CAT,
                    breed = "Chat Domestique",
                    weightKg = 4.5f,
                    ageMonths = 36,
                    sex = com.dobbleshop.neovision.data.model.Sex.MALE,
                    isSterilized = true,
                    activityLevel = com.dobbleshop.neovision.data.model.ActivityLevel.NORMAL,
                    nutritionalGoal = com.dobbleshop.neovision.data.model.NutritionalGoal.MAINTAIN,
                    deviceId = "dev_001"
                )
        }
    }
    
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
                        Text(
                            "DOBBLESHOP",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2196F3)
                        )
                        Text(
                            "NEO VISION",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4FC3F7)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Open notifications */ }) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color(0xFFF44336)
                                ) {
                                    Text("3", color = Color.White)
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color(0xFF2196F3)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
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
            Button(
                onClick = { /* TODO: Feed now */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                ),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(Icons.Default.Restaurant, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Nourrir maintenant", style = MaterialTheme.typography.labelLarge)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Eau and Caméra buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* TODO: Water screen */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF00BCD4)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0F7FA)),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Icon(
                        Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = Color(0xFF00BCD4)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Eau",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF00BCD4)
                    )
                }
                
                OutlinedButton(
                    onClick = { /* TODO: Camera screen */ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF2196F3)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE3F2FD)),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Icon(
                        Icons.Default.Videocam,
                        contentDescription = null,
                        tint = Color(0xFF2196F3)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Caméra",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF2196F3)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Reservoir levels
            ReservoirLevelsCard(
                deviceStatus = deviceStatus,
                onNavigateToDetail = onNavigateToReservoirs
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Active animal
            activePet?.let {
                ActiveAnimalCard(
                    pet = it,
                    onChangePet = { showAnimalDialog = true },
                    onQuickFeed = {
                        dashboardViewModel.quickFeed(grams = 80)
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Recent Activity
            RecentActivityCard(onNavigateToHistory = onNavigateToHistory)
        }
    }
    
    // Animal selection dialog
    if (showAnimalDialog) {
        AnimalSelectionDialog(
            petsUiState = petsUiState,
            onDismiss = { showAnimalDialog = false },
            onSelectPet = { pet ->
                // TODO: Set active pet in view model
                showAnimalDialog = false
            },
            onAddPet = {
                showAnimalDialog = false
                showAddPetDialog = true
            }
        )
    }
    
    // Add Pet Dialog
    if (showAddPetDialog) {
        AddPetDialog(
            onDismiss = { showAddPetDialog = false },
            onConfirm = { pet: Pet ->
                petsViewModel.addPet(pet)
                showAddPetDialog = false
            }
        )
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Device icon
                    Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = Color(0xFFE3F2FD),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Pets,
                                contentDescription = null,
                                tint = Color(0xFF2196F3),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = "DOBBLESHOP",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2196F3)
                        )
                        Text(
                            text = "NEO VISION",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF4FC3F7)
                        )
                    }
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
                    iconColor = Color(0xFF4CAF50),
                    label = "Batterie",
                    value = "${deviceStatus.batteryPercent}%",
                    status = "OK",
                    modifier = Modifier.weight(1f)
                )
                
                StatusItem(
                    icon = Icons.Default.Restaurant,
                    iconColor = Color(0xFF2196F3),
                    label = "Réservoir croquettes",
                    value = "Plein",
                    detail = "1.2 kg",
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
                    iconColor = Color(0xFF00BCD4),
                    label = "Niveau d'eau",
                    value = "Optimal",
                    modifier = Modifier.weight(1f)
                )
                
                StatusItem(
                    icon = Icons.Default.Restaurant,
                    iconColor = Color(0xFF2196F3),
                    label = "Croquettes restantes",
                    value = "1.2 kg",
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
    iconColor: Color = Color(0xFF2196F3),
    label: String,
    value: String,
    detail: String? = null,
    status: String? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = iconColor.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF757575)
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF212121)
            )
            
            if (detail != null) {
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575)
                )
            }
            
            if (status != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = Color(0xFF4CAF50),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "● $status",
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
private fun ReservoirLevelsCard(
    deviceStatus: DeviceStatus,
    onNavigateToDetail: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
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
                IconButton(onClick = onNavigateToDetail) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Details",
                        tint = Color(0xFF2196F3)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Food reservoir
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Circle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Croquettes (VL53L0X)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
                Text(
                    text = "${deviceStatus.foodReservoirPercent}% · ${deviceStatus.foodReservoirGrams}g",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            LinearProgressIndicator(
                progress = { deviceStatus.foodReservoirPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .padding(vertical = 2.dp),
                color = Color(0xFF4CAF50),
                trackColor = Color(0xFFE8F5E9)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Water reservoir
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Circle,
                        contentDescription = null,
                        tint = Color(0xFF00BCD4),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Eau",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
                Text(
                    text = "${deviceStatus.waterReservoirPercent}% · ${deviceStatus.waterReservoirMl}ml",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            LinearProgressIndicator(
                progress = { deviceStatus.waterReservoirPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .padding(vertical = 2.dp),
                color = Color(0xFF00BCD4),
                trackColor = Color(0xFFE0F7FA)
            )
        }
    }
}

@Composable
private fun ActiveAnimalCard(
    pet: Pet,
    onChangePet: () -> Unit,
    onQuickFeed: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pet avatar
            Surface(
                shape = CircleShape,
                color = Color(0xFFE3F2FD),
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = when(pet.species) {
                            com.dobbleshop.neovision.data.model.Species.CAT -> "🐱"
                            com.dobbleshop.neovision.data.model.Species.DOG -> "🐶"
                        },
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Animal actif",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                if (pet.breed?.isNotBlank() == true) {
                    Text(
                        text = pet.breed,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Daily rations (if available)
                // TODO: Get from ViewModel
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "🥣 65g/j",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "💧 220ml/j",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = onChangePet) {
                    Text("Changer", color = Color(0xFF2196F3))
                }
                
                OutlinedButton(
                    onClick = onQuickFeed,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF2196F3)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2196F3)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Default.Restaurant,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ration", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun RecentActivityCard(onNavigateToHistory: () -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Activité récente",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                TextButton(onClick = onNavigateToHistory) {
                    Text("Historique", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Mock activity items
            ActivityItem(
                icon = "⚠️",
                title = "Bourrage (résolu)",
                time = "11:12",
                bgColor = Color(0xFFFFE0B2)
            )
            
            ActivityItem(
                icon = "💧",
                title = "150ml distribués",
                time = "20:00",
                bgColor = Color(0xFFB2EBF2)
            )
            
            ActivityItem(
                icon = "💧",
                title = "100ml distribués",
                time = "16:00",
                bgColor = Color(0xFFB2EBF2)
            )
            
            ActivityItem(
                icon = "🥣",
                title = "80g servis (réel: 80g · HX711)",
                time = "19:30",
                bgColor = Color(0xFFC8E6C9)
            )
            
            ActivityItem(
                icon = "🔐",
                title = "Présence détectée — 92%",
                time = "14:15",
                bgColor = Color(0xFFFFF9C4),
                hasCamera = true
            )
        }
    }
}

@Composable
private fun ActivityItem(
    icon: String,
    title: String,
    time: String,
    bgColor: Color,
    hasCamera: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = bgColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = icon,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
        
        if (hasCamera) {
            Text(
                text = "📸",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun AnimalSelectionDialog(
    petsUiState: PetsUiState,
    onDismiss: () -> Unit,
    onSelectPet: (Pet) -> Unit,
    onAddPet: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF5F5F5)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Surface(
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Animaux",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Fermer")
                        }
                    }
                }
                
                // Content
                when (petsUiState) {
                    is PetsUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is PetsUiState.Empty -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Aucun animal",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ajoutez un animal pour commencer",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = onAddPet,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2196F3)
                                )
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ajouter un animal")
                            }
                        }
                    }
                    is PetsUiState.Success -> {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(petsUiState.pets) { pet ->
                                AnimalSelectionCard(
                                    pet = pet,
                                    isActive = petsUiState.pets.indexOf(pet) == 0, // First one is active
                                    onSelect = { onSelectPet(pet) }
                                )
                            }
                            
                            item {
                                Button(
                                    onClick = onAddPet,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2196F3))
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF2196F3))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Ajouter un animal", color = Color(0xFF2196F3))
                                }
                            }
                        }
                    }
                    is PetsUiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = petsUiState.message,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimalSelectionCard(
    pet: Pet,
    isActive: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isActive) { onSelect() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color(0xFFE3F2FD) else Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pet avatar
                    Surface(
                        shape = CircleShape,
                        color = if (isActive) Color(0xFF2196F3).copy(alpha = 0.1f) else Color(0xFFE3F2FD),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = when(pet.species) {
                                    com.dobbleshop.neovision.data.model.Species.CAT -> "🐱"
                                    com.dobbleshop.neovision.data.model.Species.DOG -> "🐶"
                                },
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = pet.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (pet.breed?.isNotBlank() == true) {
                            Text(
                                text = pet.breed,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${(pet.ageMonths ?: 0) / 12} ans",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                if (isActive) {
                    Surface(
                        color = Color(0xFF4CAF50),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Actif",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(12.dp))
            
            // Pet details grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Poids",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (pet.weightKg != null) "${pet.weightKg} kg" else "—",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Âge",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(pet.ageMonths ?: 0) / 12}a ${pet.ageMonths}m",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Croquettes / jour",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "🥣 65g",  // TODO: Calculate from ViewModel
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Eau / jour",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "💧 220 ml",  // TODO: Calculate from ViewModel
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            if (!isActive) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* TODO: View details */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text("Détails")
                    }
                    Button(
                        onClick = onSelect,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        )
                    ) {
                        Text("Sélectionner")
                    }
                }
            }
        }
    }
}

