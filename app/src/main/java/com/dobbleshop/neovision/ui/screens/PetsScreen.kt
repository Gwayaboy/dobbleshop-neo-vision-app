package com.dobbleshop.neovision.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dobbleshop.neovision.R
import com.dobbleshop.neovision.data.model.Pet
import com.dobbleshop.neovision.data.model.Species
import com.dobbleshop.neovision.ui.components.AddPetDialog
import com.dobbleshop.neovision.ui.viewmodel.PetsUiState
import com.dobbleshop.neovision.ui.viewmodel.PetsViewModel
import java.util.concurrent.TimeUnit

/**
 * Pets/Animals screen matching DOBBLESHOP web app design
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetsScreen(
    viewModel: PetsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showAddDialog by viewModel.showAddDialog.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            text = "DOBBLESHOP",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2196F3)
                        )
                        Text(
                            text = "NEO VISION",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF4FC3F7)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: User profile */ }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color(0xFF2196F3)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddPetDialog() },
                containerColor = Color(0xFF2196F3)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Pet",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            when (uiState) {
                is PetsUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is PetsUiState.Empty -> {
                    EmptyPetsView(
                        onAddPet = { viewModel.showAddPetDialog() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is PetsUiState.Success -> {
                    val pets = (uiState as PetsUiState.Success).pets
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Animaux",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        
                        items(pets) { pet ->
                            PetCard(
                                pet = pet,
                                viewModel = viewModel
                            )
                        }
                    }
                }
                is PetsUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Erreur: ${(uiState as PetsUiState.Error).message}",
                            color = Color.Red
                        )
                        Button(
                            onClick = { /* TODO: Retry */ },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Réessayer")
                        }
                    }
                }
            }
        }
        
        // Add Pet Dialog
        if (showAddDialog) {
            AddPetDialog(
                onDismiss = { viewModel.hideAddPetDialog() },
                onConfirm = { pet ->
                    viewModel.addPet(pet)
                }
            )
        }
    }
}

@Composable
fun PetCard(
    pet: Pet,
    viewModel: PetsViewModel
) {
    val ration = remember(pet) { viewModel.calculateDailyRation(pet) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Pet header with image and name
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pet image placeholder
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (pet.species == Species.CAT) 
                            Icons.Default.Pets 
                        else 
                            Icons.Default.Pets,
                        contentDescription = null,
                        tint = Color(0xFF9E9E9E),
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pet.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = pet.breed ?: when (pet.species) {
                            Species.CAT -> "Chat Domestique"
                            Species.DOG -> "Chien"
                        },
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    if (pet.ageMonths != null) {
                        val years = pet.ageMonths / 12
                        val months = pet.ageMonths % 12
                        Text(
                            text = if (months > 0) "$years ans $months mois" else "$years ans",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Pet stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Weight and Age
                Column(modifier = Modifier.weight(1f)) {
                    StatItem(label = "Poids", value = "%.1f kg".format(pet.weightKg))
                    Spacer(modifier = Modifier.height(8.dp))
                    StatItem(
                        label = "Croquettes / jour",
                        value = "${ration.recommendedDailyFood} g",
                        icon = Icons.Default.Restaurant
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    if (pet.ageMonths != null) {
                        val years = pet.ageMonths / 12
                        val months = pet.ageMonths % 12
                        StatItem(
                            label = "Âge",
                            value = if (months > 0) "$years ans $months mois" else "$years ans"
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    StatItem(
                        label = "Eau / jour",
                        value = if (ration.recommendedDailyWater >= 1000) 
                            "%.1f L".format(ration.recommendedDailyWater / 1000.0)
                        else 
                            "${ration.recommendedDailyWater} ml",
                        icon = Icons.Default.WaterDrop
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Details button
            Button(
                onClick = { viewModel.selectPet(pet) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5F5F5),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Détails")
            }
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EmptyPetsView(
    onAddPet: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Pets,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Aucun animal",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Ajoutez votre premier animal",
            fontSize = 14.sp,
            color = Color.Gray
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
