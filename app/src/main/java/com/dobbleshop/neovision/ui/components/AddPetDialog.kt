package com.dobbleshop.neovision.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dobbleshop.neovision.data.model.*
import java.util.UUID

/**
 * Dialog for adding a new pet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetDialog(
    onDismiss: () -> Unit,
    onConfirm: (Pet) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var species by remember { mutableStateOf(Species.CAT) }
    var breed by remember { mutableStateOf("") }
    var weightKg by remember { mutableStateOf("") }
    var ageYears by remember { mutableStateOf("") }
    var ageMonths by remember { mutableStateOf("") }
    var activityLevel by remember { mutableStateOf(ActivityLevel.NORMAL) }
    var nutritionalGoal by remember { mutableStateOf(NutritionalGoal.MAINTAIN) }
    var sex by remember { mutableStateOf<Sex?>(Sex.MALE) }
    var isSterilized by remember { mutableStateOf(false) }
    
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nouvel animal",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                
                Divider()
                
                // Form content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nom *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    // Species
                    Text(
                        text = "Espèce *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = species == Species.CAT,
                            onClick = { species = Species.CAT },
                            label = { Text("Chat") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = species == Species.DOG,
                            onClick = { species = Species.DOG },
                            label = { Text("Chien") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // Breed
                    OutlinedTextField(
                        value = breed,
                        onValueChange = { breed = it },
                        label = { Text("Race") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text(if (species == Species.CAT) "Chat Domestique" else "Chien") }
                    )
                    
                    // Weight
                    OutlinedTextField(
                        value = weightKg,
                        onValueChange = { weightKg = it },
                        label = { Text("Poids (kg) *") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                    
                    // Age
                    Text(
                        text = "Âge",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = ageYears,
                            onValueChange = { ageYears = it },
                            label = { Text("Années") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = ageMonths,
                            onValueChange = { ageMonths = it },
                            label = { Text("Mois") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                    
                    // Sex
                    Text(
                        text = "Sexe",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = sex == Sex.MALE,
                            onClick = { sex = Sex.MALE },
                            label = { Text("Mâle") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = sex == Sex.FEMALE,
                            onClick = { sex = Sex.FEMALE },
                            label = { Text("Femelle") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // Sterilized
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Stérilisé(e)")
                        Switch(
                            checked = isSterilized,
                            onCheckedChange = { isSterilized = it }
                        )
                    }
                    
                    // Activity Level
                    Text(
                        text = "Niveau d'activité *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = activityLevel == ActivityLevel.LOW,
                            onClick = { activityLevel = ActivityLevel.LOW },
                            label = { Text("Bas") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = activityLevel == ActivityLevel.NORMAL,
                            onClick = { activityLevel = ActivityLevel.NORMAL },
                            label = { Text("Normal") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = activityLevel == ActivityLevel.HIGH,
                            onClick = { activityLevel = ActivityLevel.HIGH },
                            label = { Text("Élevé") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // Nutritional Goal
                    Text(
                        text = "Objectif nutritionnel *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = nutritionalGoal == NutritionalGoal.MAINTAIN,
                            onClick = { nutritionalGoal = NutritionalGoal.MAINTAIN },
                            label = { Text("Maintien du poids") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        FilterChip(
                            selected = nutritionalGoal == NutritionalGoal.WEIGHT_CONTROL,
                            onClick = { nutritionalGoal = NutritionalGoal.WEIGHT_CONTROL },
                            label = { Text("Contrôle du poids") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        FilterChip(
                            selected = nutritionalGoal == NutritionalGoal.PERFORMANCE,
                            onClick = { nutritionalGoal = NutritionalGoal.PERFORMANCE },
                            label = { Text("Performance") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    // Error message
                    if (showError) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
                
                Divider()
                
                // Footer buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Annuler")
                    }
                    
                    Button(
                        onClick = {
                            // Validate
                            if (name.isBlank()) {
                                showError = true
                                errorMessage = "Le nom est requis"
                                return@Button
                            }
                            
                            val weight = weightKg.toFloatOrNull()
                            if (weight == null || weight <= 0) {
                                showError = true
                                errorMessage = "Poids invalide"
                                return@Button
                            }
                            
                            // Calculate age in months
                            val years = ageYears.toIntOrNull() ?: 0
                            val months = ageMonths.toIntOrNull() ?: 0
                            val totalAgeMonths = if (years > 0 || months > 0) {
                                (years * 12) + months
                            } else null
                            
                            // Create pet
                            val pet = Pet(
                                id = UUID.randomUUID().toString(),
                                name = name,
                                species = species,
                                breed = breed.ifBlank { null },
                                weightKg = weight,
                                ageMonths = totalAgeMonths,
                                sex = sex,
                                isSterilized = isSterilized,
                                activityLevel = activityLevel,
                                nutritionalGoal = nutritionalGoal,
                                deviceId = "MOCK_DEVICE_001", // TODO: Use actual device ID
                                isActive = true
                            )
                            
                            onConfirm(pet)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        )
                    ) {
                        Text("Ajouter")
                    }
                }
            }
        }
    }
}
