package com.dobbleshop.neovision.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedingScreen() {
    var selectedPortion by remember { mutableStateOf(80) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Croquettes, 1 = Eau
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Repas", color = Color.Black)
                },
                actions = {
                    IconButton(onClick = { /* Notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Animal Actif Card
            ActiveAnimalRepasCard()
            
            // Distribution journalière Card
            DailyDistributionCard()
            
            // Tabs for Croquettes / Eau
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("🥣 Croquettes") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("💧 Eau") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            if (selectedTab == 0) {
                // Prochain Repas
                NextMealCard()
                
                // Manual Distribution
                ManualDistributionLightCard(
                    selectedPortion = selectedPortion,
                    onPortionChange = { selectedPortion = it }
                )
                
                // Scheduled Meals
                ScheduledMealsLightCard()
            } else {
                // Water info card
                WaterInfoCard()
            }
        }
    }
}

@Composable
fun ActiveAnimalRepasCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🐱",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.size(48.dp)
                )
                
                Column {
                    Text(
                        text = "Animal actif",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Léo",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Details",
                tint = Color(0xFF2196F3)
            )
        }
    }
}

@Composable
fun DailyDistributionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Distribution journalière",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            // Croquettes servies
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "🥣 Croquettes servies",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "200g / 65g",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                LinearProgressIndicator(
                    progress = { 200f / 65f }, // Over target
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = Color(0xFFFF9800), // Orange for over
                    trackColor = Color(0xFFFFE0B2)
                )
            }
            
            // Eau distribuée
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "💧 Eau distribuée",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "550ml / 220ml",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                LinearProgressIndicator(
                    progress = { 550f / 220f }, // Over target
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = Color(0xFF00BCD4),
                    trackColor = Color(0xFFB2EBF2)
                )
            }
            
            Text(
                text = "Gamelle actuelle : 0g (HX711)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun NextMealCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🥣",
                    style = MaterialTheme.typography.displaySmall
                )
                
                Column {
                    Text(
                        text = "Prochain repas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "08:00",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            
            Text(
                text = "80g",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ManualDistributionLightCard(
    selectedPortion: Int,
    onPortionChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Distribution manuelle",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            // Portion Control
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🥣 Portion",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedIconButton(
                        onClick = { if (selectedPortion > 20) onPortionChange(selectedPortion - 10) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color(0xFF2196F3))
                    }
                    
                    Text(
                        text = "${selectedPortion}g",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    OutlinedIconButton(
                        onClick = { if (selectedPortion < 200) onPortionChange(selectedPortion + 10) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color(0xFF2196F3))
                    }
                }
            }
            
            // Distribute Button
            Button(
                onClick = { /* Distribute food */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Distribuer ${selectedPortion}g", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ScheduledMealsLightCard() {
    val mockSchedules = listOf(
        ScheduledMeal("Matin — 08:00", 80, true),
        ScheduledMeal("Midi — 12:30", 40, true),
        ScheduledMeal("Soir — 18:30", 80, true),
        ScheduledMeal("Nuit — 22:00", 0, false, waterOnly = true)
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Planning repas (DS3231)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                TextButton(onClick = { /* Add schedule */ }) {
                    Text("+ Ajouter", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
                }
            }
            
            mockSchedules.forEach { schedule ->
                ScheduledMealItemLight(schedule)
            }
            
            // Quick chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Matin", "Midi", "Soir", "Nuit").forEachIndexed { index, label ->
                    val time = listOf("08:00", "12:30", "18:30", "22:00")[index]
                    val amount = listOf("80g", "40g", "80g", "—")[index]
                    
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF5F5F5),
                        tonalElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                            Text(
                                text = time,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = amount,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduledMealItemLight(schedule: ScheduledMeal) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🥣",
                style = MaterialTheme.typography.headlineSmall
            )
            
            Column {
                Text(
                    text = schedule.time,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = if (schedule.waterOnly) "Eau seulement" else "${schedule.portionGrams}g",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }
        
        Switch(
            checked = schedule.isActive,
            onCheckedChange = { /* Toggle schedule */ },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF2196F3),
                checkedTrackColor = Color(0xFF64B5F6)
            )
        )
    }
    
    if (schedule != mockSchedules.last()) {
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

private val mockSchedules = listOf(
    ScheduledMeal("Matin — 08:00", 80, true),
    ScheduledMeal("Midi — 12:30", 40, true),
    ScheduledMeal("Soir — 18:30", 80, true),
    ScheduledMeal("Nuit — 22:00", 0, false, waterOnly = true)
)

@Composable
fun WaterInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "💧",
                style = MaterialTheme.typography.displayLarge
            )
            
            Text(
                text = "Gestion de l'eau",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Text(
                text = "Distribution automatique basée sur la consommation journalière de l'animal",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Button(
                onClick = { /* Configure water */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BCD4)
                )
            ) {
                Text("Configurer")
            }
        }
    }
}

data class ScheduledMeal(
    val time: String,
    val portionGrams: Int,
    val isActive: Boolean,
    val waterOnly: Boolean = false
)
