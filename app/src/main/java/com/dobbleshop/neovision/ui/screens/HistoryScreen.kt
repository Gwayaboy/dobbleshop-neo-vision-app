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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Aujourd'hui, 1 = Cette semaine
    
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
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color(0xFF2196F3)
                        )
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
            // Title
            Text(
                text = "Historique",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            // Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("Aujourd'hui") },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.White,
                        selectedLabelColor = Color.Black
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedTab == 0,
                        borderColor = if (selectedTab == 0) Color(0xFF2196F3) else Color.LightGray,
                        borderWidth = if (selectedTab == 0) 2.dp else 1.dp
                    )
                )
                FilterChip(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("Cette semaine") },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color.White,
                        selectedLabelColor = Color.Black
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedTab == 1,
                        borderColor = if (selectedTab == 1) Color(0xFF2196F3) else Color.LightGray,
                        borderWidth = if (selectedTab == 1) 2.dp else 1.dp
                    )
                )
            }
            
            // Food Chart
            FoodChartCard()
            
            // Water Chart
            WaterChartCard()
            
            // Journal des distributions
            DistributionJournalCard()
        }
    }
}

@Composable
fun FoodChartCard() {
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Croquettes (par repas)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            
            // Bar chart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                BarItem(height = 80, label = "08:00", value = "80g", color = Color(0xFF2196F3))
                BarItem(height = 40, label = "12:30", value = "40g", color = Color(0xFF2196F3))
                BarItem(height = 5, label = "15:00", value = "0g", color = Color(0xFFE0E0E0))
                BarItem(height = 80, label = "18:30", value = "80g", color = Color(0xFF2196F3))
                BarItem(height = 0, label = "19:00", value = "0g", color = Color(0xFFE0E0E0))
            }
            
            HorizontalDivider()
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "200g",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3)
                )
            }
        }
    }
}

@Composable
fun WaterChartCard() {
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = Color(0xFF00BCD4),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Eau (par cycle)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            
            // Bar chart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                BarItem(height = 100, label = "08:00", value = "", color = Color(0xFF00BCD4))
                BarItem(height = 90, label = "12:30", value = "", color = Color(0xFF00BCD4))
                BarItem(height = 60, label = "15:00", value = "", color = Color(0xFF00BCD4))
                BarItem(height = 10, label = "18:30", value = "", color = Color(0xFFE0E0E0))
                BarItem(height = 100, label = "19:00", value = "", color = Color(0xFF00BCD4))
            }
            
            HorizontalDivider()
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "550ml",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00BCD4)
                )
            }
        }
    }
}

@Composable
fun RowScope.BarItem(
    height: Int,
    label: String,
    value: String,
    color: Color
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (value.isNotEmpty()) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(if (height > 0) height.dp else 1.dp)
                .background(color, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            fontSize = 10.sp
        )
    }
}

@Composable
fun DistributionJournalCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Journal des distributions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Journal items
            JournalItem(
                icon = "⚠️",
                title = "Bourrage résolu",
                date = "23/10 11:12",
                bgColor = Color(0xFFFFE0B2),
                hasCheckmark = false
            )
            
            JournalItem(
                icon = "💧",
                title = "150ml distribués",
                date = "24/10 20:00",
                subtitle = "Léo",
                bgColor = Color(0xFFB2EBF2),
                hasCheckmark = true
            )
            
            JournalItem(
                icon = "💧",
                title = "100ml distribués",
                date = "24/10 16:00",
                subtitle = "Léo",
                bgColor = Color(0xFFB2EBF2),
                hasCheckmark = true
            )
            
            JournalItem(
                icon = "🥣",
                title = "80g distribués (pesé: 80g)",
                date = "24/10 19:30",
                subtitle = "Léo",
                bgColor = Color(0xFFC8E6C9),
                hasCheckmark = true
            )
            
            JournalItem(
                icon = "🔐",
                title = "Présence détectée — 92%",
                date = "24/10 14:15",
                bgColor = Color(0xFFFFF9C4),
                hasCheckmark = false
            )
            
            JournalItem(
                icon = "💧",
                title = "150ml distribués",
                date = "24/10 13:00",
                subtitle = "Léo",
                bgColor = Color(0xFFB2EBF2),
                hasCheckmark = true
            )
            
            JournalItem(
                icon = "🥣",
                title = "40g distribués (pesé: 41g)",
                date = "24/10 13:31",
                subtitle = "Léo",
                bgColor = Color(0xFFC8E6C9),
                hasCheckmark = true
            )
            
            JournalItem(
                icon = "💧",
                title = "150ml distribués",
                date = "24/10 09:05",
                subtitle = "Léo",
                bgColor = Color(0xFFB2EBF2),
                hasCheckmark = true
            )
            
            JournalItem(
                icon = "🥣",
                title = "80g distribués (pesé: 79g)",
                date = "24/10 09:02",
                subtitle = "Léo",
                bgColor = Color(0xFFC8E6C9),
                hasCheckmark = true,
                isLast = true
            )
        }
    }
}

@Composable
fun JournalItem(
    icon: String,
    title: String,
    date: String,
    subtitle: String? = null,
    bgColor: Color,
    hasCheckmark: Boolean,
    isLast: Boolean = false
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
                modifier = Modifier.size(40.dp),
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
                    text = date + if (subtitle != null) " · $subtitle" else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
        
        if (hasCheckmark) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Confirmé",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(20.dp)
            )
        }
    }
    
    if (!isLast) {
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
    }
}
