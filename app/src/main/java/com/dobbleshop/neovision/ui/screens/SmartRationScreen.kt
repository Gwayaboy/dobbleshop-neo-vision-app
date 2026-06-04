package com.dobbleshop.neovision.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartRationScreen(
    onBackClick: () -> Unit = {}
) {
    val pageBackground = Color(0xFFF3F5F8)

    Scaffold(
        containerColor = pageBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ration intelligente",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = pageBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MainRationCard()
            NutritionFormulaCard()
            DailyTrackingCard()
            AiAdviceCard()
        }
    }
}

@Composable
private fun MainRationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6F48F6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Ration quotidienne",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = "108g",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 44.sp
            )

            Text(
                text = "+12g ajustement actif",
                color = Color(0xFFEADFFF),
                style = MaterialTheme.typography.bodyMedium
            )

            HorizontalDivider(color = Color.White.copy(alpha = 0.25f))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusPill(text = "Normal", bg = Color(0xFF3BAF68))
                StatusPill(text = "Hydratation OK", bg = Color(0xFF2F8BE6))
                StatusPill(text = "DER stable", bg = Color(0xFF8A63FF))
            }
        }
    }
}

@Composable
private fun NutritionFormulaCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Calcul nutrition",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            FormulaLine("Poids", "5.2 kg")
            FormulaLine("RER", "248 kcal")
            FormulaLine("DER", "372 kcal")
            FormulaLine("Profil", "Adulte stérilisé")
        }
    }
}

@Composable
private fun DailyTrackingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Suivi du jour",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            ProgressRow(label = "Croquettes", current = 84f, total = 108f, color = Color(0xFF6F48F6))
            ProgressRow(label = "Hydratation", current = 210f, total = 240f, color = Color(0xFF2F8BE6), unit = "ml")
        }
    }
}

@Composable
private fun AiAdviceCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF5FF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD8E8FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF1D4ED8)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Conseil IA",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D4ED8)
                )
                Text(
                    text = "Activite levee aujourd'hui: +5g supplementaires recommandes ce soir.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1E3A8A)
                )
            }
        }
    }
}

@Composable
private fun StatusPill(text: String, bg: Color) {
    Surface(shape = RoundedCornerShape(999.dp), color = bg) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun FormulaLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF475467)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF111827)
        )
    }
}

@Composable
private fun ProgressRow(
    label: String,
    current: Float,
    total: Float,
    color: Color,
    unit: String = "g"
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = Color(0xFF475467), style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "${current.toInt()}/${total.toInt()}$unit",
                color = Color(0xFF111827),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        LinearProgressIndicator(
            progress = { (current / total).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(6.dp)),
            color = color,
            trackColor = Color(0xFFE9EEF5)
        )
    }
}
