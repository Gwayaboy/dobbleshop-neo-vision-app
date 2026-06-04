package com.dobbleshop.neovision.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.dobbleshop.neovision.ui.viewmodel.CameraViewModel
import com.dobbleshop.neovision.data.api.SecurityMode
import com.dobbleshop.neovision.ui.camera.CameraPluginType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val streamSession by viewModel.streamSession.collectAsState()
    val isMicEnabled by viewModel.isMicEnabled.collectAsState()
    val isSpeakerEnabled by viewModel.isSpeakerEnabled.collectAsState()
    val securityMode by viewModel.securityMode.collectAsState()
    val selectedPlugin by viewModel.selectedPlugin.collectAsState()
    val availablePlugins by viewModel.availablePlugins.collectAsState()
    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }
    
    // Auto-start camera stream when screen loads
    LaunchedEffect(Unit) {
        if (streamSession == null && !uiState.isStreaming) {
            viewModel.startCameraStream("dev_001")
        }
    }

    LaunchedEffect(selectedPlugin) {
        if (selectedPlugin == CameraPluginType.PHONE && !hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    
    // Cleanup when leaving screen
    DisposableEffect(Unit) {
        onDispose {
            // Stream will be stopped automatically
        }
    }
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSuccessMessage()
        }
    }
    
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearErrorMessage()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Caméra",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1C1C1E)
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF000000)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CameraSourceSelector(
                selectedPlugin = selectedPlugin,
                availablePlugins = availablePlugins.map { it.type to it.label },
                onPluginSelected = { pluginType ->
                    viewModel.selectPlugin(pluginType, "dev_001")
                }
            )

            // Camera Feed Section
            CameraFeedCard(
                selectedPlugin = selectedPlugin,
                hasCameraPermission = hasCameraPermission,
                onRequestPermission = {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            )
            
            // Camera Controls
            CameraControlsCard()
            
            // Audio Section
            AudioControlsCard(
                isMicEnabled = isMicEnabled,
                onMicToggle = { viewModel.toggleMicrophone("dev_001") },
                isSpeakerEnabled = isSpeakerEnabled,
                onSpeakerToggle = { viewModel.toggleSpeaker("dev_001") }
            )
            
            // Hardware Info
            HardwareInfoCard()
            
            // Security Section
            SecurityCard(
                currentMode = securityMode,
                onModeChange = { mode ->
                    viewModel.setSecurityMode("dev_001", mode)
                }
            )
        }
    }
}

@Composable
fun CameraSourceSelector(
    selectedPlugin: CameraPluginType,
    availablePlugins: List<Pair<CameraPluginType, String>>,
    onPluginSelected: (CameraPluginType) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2E))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Source caméra",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                availablePlugins.forEach { (type, label) ->
                    FilterChip(
                        selected = selectedPlugin == type,
                        onClick = { onPluginSelected(type) },
                        label = { Text(label) }
                    )
                }
            }
        }
    }
}

@Composable
fun CameraFeedCard(selectedPlugin: CameraPluginType) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (selectedPlugin == CameraPluginType.PHONE) {
                PhoneCameraPreview(modifier = Modifier.fillMaxSize())
            } else {
                // Placeholder for feeder stream until remote stream renderer is integrated.
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "Flux H.264 · RPi Zero 2W",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
            
            // EN DIRECT badge
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                color = Color(0xFF4CAF50),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "EN DIRECT",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
            
            // REC badge
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                color = Color.Red,
                shape = RoundedCornerShape(6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color.White, shape = CircleShape)
                    )
                    Text(
                        text = "REC",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CameraFeedCard(
    selectedPlugin: CameraPluginType,
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    if (selectedPlugin == CameraPluginType.PHONE && !hasCameraPermission) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Autorisez l'acces camera pour le flux local.",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onRequestPermission) {
                    Text("Autoriser la camera")
                }
            }
        }
    } else {
        CameraFeedCard(selectedPlugin = selectedPlugin)
    }
}

@Composable
private fun PhoneCameraPreview(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { previewView }
    )

    DisposableEffect(lifecycleOwner, previewView) {
        val providerFuture = ProcessCameraProvider.getInstance(context)
        val executor = ContextCompat.getMainExecutor(context)

        val listener = Runnable {
            runCatching {
                val cameraProvider = providerFuture.get()
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview
                )
            }
        }

        providerFuture.addListener(listener, executor)

        onDispose {
            runCatching {
                ProcessCameraProvider.getInstance(context).get().unbindAll()
            }
        }
    }
}

@Composable
fun CameraControlsCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Contrôles caméra",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = { /* Fullscreen */ },
                    modifier = Modifier.size(56.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Fullscreen, contentDescription = "Plein écran")
                        Text(
                            text = "Plein écran",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                
                IconButton(
                    onClick = { /* Snapshot */ },
                    modifier = Modifier.size(56.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "Capture")
                        Text(
                            text = "Capture",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                
                IconButton(
                    onClick = { /* Night mode */ },
                    modifier = Modifier.size(56.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.NightsStay, contentDescription = "Mode nuit")
                        Text(
                            text = "Mode nuit",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                
                IconButton(
                    onClick = { /* Settings */ },
                    modifier = Modifier.size(56.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Settings, contentDescription = "Paramètres")
                        Text(
                            text = "Paramètres",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AudioControlsCard(
    isMicEnabled: Boolean,
    onMicToggle: () -> Unit,
    isSpeakerEnabled: Boolean,
    onSpeakerToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Audio bidirectionnel",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            // Microphone Control (Parler)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isMicEnabled) Icons.Default.Mic else Icons.Default.MicOff,
                        contentDescription = null,
                        tint = if (isMicEnabled) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                    Column {
                        Text(
                            text = "Parler (push-to-talk)",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "INMP441 Microphone",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                
                Switch(
                    checked = isMicEnabled,
                    onCheckedChange = { onMicToggle() }
                )
            }
            
            Divider()
            
            // Speaker Control (Écouter)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isSpeakerEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = null,
                        tint = if (isSpeakerEnabled) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                    Column {
                        Text(
                            text = "Écouter",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "MAX98357A Amplificateur + Haut-parleur",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                
                Switch(
                    checked = isSpeakerEnabled,
                    onCheckedChange = { onSpeakerToggle() }
                )
            }
        }
    }
}

@Composable
fun HardwareInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2E)
        ),
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
                Column {
                    Text(
                        text = "Salon — Caméra 1",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Surface(
                        color = Color(0xFF4CAF50),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = "En ligne",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Text(
                        text = "Wi-Fi : excellent (-42 dBm)",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                // Control buttons grid
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = { /* Action */ },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                        }
                        IconButton(
                            onClick = { /* Action */ },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color.White)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = { /* Action */ },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White)
                        }
                        IconButton(
                            onClick = { /* Action */ },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SecurityCard(
    currentMode: SecurityMode = SecurityMode.AUTO,
    onModeChange: (SecurityMode) -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2E)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "🛡️ Sécurité domicile (mmWave · RPi UART)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Mode",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                    Text(
                        text = when (currentMode) {
                            SecurityMode.OFF -> "Désactivé"
                            SecurityMode.HOME -> "Domicile"
                            SecurityMode.AWAY -> "Absent"
                            SecurityMode.AUTO -> "Automatique"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                Surface(
                    color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✅",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Aucune",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Mode buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "⏸ Off" to SecurityMode.OFF,
                    "🏠 Home" to SecurityMode.HOME,
                    "🚨 Absent" to SecurityMode.AWAY,
                    "🤖 Auto" to SecurityMode.AUTO
                ).forEach { (label, mode) ->
                    OutlinedButton(
                        onClick = { onModeChange(mode) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (currentMode == mode) Color(0xFF3A4D6E) else Color.Transparent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 10.sp
                        )
                    }
                }
            }
            
            // View security events button
            OutlinedButton(
                onClick = { /* View events */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF64B5F6)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Voir les événements sécurité")
            }
        }
    }
}
