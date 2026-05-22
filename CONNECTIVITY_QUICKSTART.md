# Connectivity Quick Start Guide

## TL;DR

Your Android app now has **production-ready infrastructure** for communicating with the physical DOBBLESHOP NEO VISION pet feeder via **Internet (cloud)** or **Bluetooth (local)**.

## What's Ready to Use

### 1. Network Monitoring ✅
```kotlin
@Inject lateinit var networkMonitor: NetworkMonitor

// In your ViewModel/Activity
networkMonitor.networkState.collect { state ->
    when (state) {
        is NetworkState.Available -> // Internet connected
        is NetworkState.Unavailable -> // No internet
        is NetworkState.Capabilities -> // Check bandwidth, WiFi vs cellular
    }
}
```

### 2. Cloud API Service ✅
```kotlin
@Inject lateinit var deviceRepository: DeviceRepository

// Dispense food (automatically uses cloud if connected)
val result = deviceRepository.dispenseFood(
    deviceId = "dev_001",
    petId = "pet_123",
    grams = 80
)

// Start camera stream
val streamResult = deviceRepository.startCameraStream(
    deviceId = "dev_001",
    quality = StreamQuality.AUTO
)

if (streamResult.isSuccess) {
    val session = streamResult.getOrNull()!!
    // Use session.streamUrl with ExoPlayer
    player.setMediaItem(MediaItem.fromUri(session.streamUrl))
}
```

### 3. Connection State Management ✅
```kotlin
@Inject lateinit var connectionManager: ConnectionManager

// Observe connection state
connectionManager.connectionState.collect { state ->
    when (state) {
        is ConnectionState.ConnectedCloud -> 
            // Connected via internet (preferred)
        is ConnectionState.ConnectedBluetooth -> 
            // Connected via BLE (local)
        is ConnectionState.ConnectedHybrid -> 
            // Both available, automatic switching
        is ConnectionState.Disconnected -> 
            // Not connected
        is ConnectionState.Error -> 
            // Connection failed
    }
}

// Connect to device
val device: Device = // your device
connectionManager.connect(
    device = device,
    preferBluetooth = false, // prefer cloud
    forceMode = null // automatic selection
)
```

## How to Use in Your Screens

### Example: Dispense Food from UI

```kotlin
// In FeedingScreen.kt
@Composable
fun FeedingScreen(
    viewModel: FeedingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Button(
        onClick = {
            viewModel.dispenseFood(
                deviceId = "dev_001",
                petId = uiState.activePet?.id ?: "",
                grams = 80
            )
        }
    ) {
        Text("Distribuer")
    }
    
    // Show status
    when {
        uiState.isDispensing -> Text("Dispensing...")
        uiState.dispenseSuccess -> Text("Success!")
        uiState.errorMessage != null -> Text("Error: ${uiState.errorMessage}")
    }
}

// In FeedingViewModel.kt
@HiltViewModel
class FeedingViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FeedingUiState())
    val uiState: StateFlow<FeedingUiState> = _uiState.asStateFlow()
    
    fun dispenseFood(deviceId: String, petId: String, grams: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDispensing = true, errorMessage = null) }
            
            val result = deviceRepository.dispenseFood(deviceId, petId, grams)
            
            _uiState.update {
                when {
                    result.isSuccess -> it.copy(
                        isDispensing = false,
                        dispenseSuccess = true,
                        lastDispenseEvent = result.getOrNull()
                    )
                    result.isFailure -> it.copy(
                        isDispensing = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                    else -> it
                }
            }
        }
    }
}

data class FeedingUiState(
    val isDispensing: Boolean = false,
    val dispenseSuccess: Boolean = false,
    val lastDispenseEvent: DispenseEvent? = null,
    val errorMessage: String? = null,
    val activePet: Pet? = null
)
```

### Example: Camera Streaming

```kotlin
// In CameraScreen.kt
@Composable
fun CameraScreen(
    deviceId: String,
    deviceRepository: DeviceRepository = hiltViewModel<CameraViewModel>().repository
) {
    var streamUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // Start stream when screen opens
    LaunchedEffect(deviceId) {
        val result = deviceRepository.startCameraStream(
            deviceId = deviceId,
            quality = StreamQuality.AUTO
        )
        
        when {
            result.isSuccess -> {
                streamUrl = result.getOrNull()?.streamUrl
                isLoading = false
            }
            result.isFailure -> {
                errorMessage = result.exceptionOrNull()?.message
                isLoading = false
            }
        }
    }
    
    // Cleanup when screen closes
    DisposableEffect(deviceId) {
        onDispose {
            // Stop stream
            viewModelScope.launch {
                deviceRepository.stopCameraStream(deviceId)
            }
        }
    }
    
    // UI
    when {
        isLoading -> CircularProgressIndicator()
        errorMessage != null -> Text("Error: $errorMessage")
        streamUrl != null -> {
            // Show video player with ExoPlayer
            AndroidView(
                factory = { context ->
                    PlayerView(context).apply {
                        player = ExoPlayer.Builder(context).build().apply {
                            setMediaItem(MediaItem.fromUri(streamUrl!!))
                            prepare()
                            play()
                        }
                    }
                }
            )
        }
    }
}
```

### Example: Monitor Device Status

```kotlin
// In DashboardScreen.kt or DashboardViewModel
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : ViewModel() {
    
    // Observe device status in real-time
    val deviceStatus: StateFlow<DeviceStatus?> = deviceRepository
        .observeDeviceStatus("dev_001")
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
    
    // Connection state
    val connectionState: StateFlow<ConnectionState> = deviceRepository
        .connectionState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConnectionState.Disconnected
        )
}

// In UI
@Composable
fun DeviceStatusCard(viewModel: DashboardViewModel) {
    val status by viewModel.deviceStatus.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    
    Card {
        Column {
            // Connection indicator
            when (connectionState) {
                is ConnectionState.ConnectedCloud -> 
                    StatusBadge("☁️ Cloud", Color.Blue)
                is ConnectionState.ConnectedBluetooth -> 
                    StatusBadge("📶 Bluetooth", Color.Cyan)
                is ConnectionState.Disconnected -> 
                    StatusBadge("⚠️ Offline", Color.Red)
            }
            
            // Device telemetry
            status?.let {
                Text("Food: ${it.foodReservoirPercent}%")
                Text("Water: ${it.waterReservoirPercent}%")
                Text("Bowl: ${it.bowlWeightGrams}g")
                Text("Battery: ${it.batteryPercent}%")
            }
        }
    }
}
```

## Configuration

### 1. Set Backend URL

In `ApiModule.kt`, update the base URL:
```kotlin
private const val BASE_URL = "https://api.dobbleshop.com/v1/"
// Change to your actual backend when deployed
```

### 2. Add Required Permissions

Already in `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

When you implement Bluetooth, add:
```xml
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

## Testing Without Real Backend

### Option 1: Mock Responses in Repository

Temporarily override methods:
```kotlin
// In DeviceRepository
override suspend fun getDeviceStatus(deviceId: String): Result<DeviceStatus> {
    // Return mock data for testing
    return Result.success(DeviceStatus(
        deviceId = deviceId,
        bowlWeightGrams = 50f,
        foodReservoirPercent = 75,
        waterReservoirPercent = 60,
        // ... other fields
    ))
}
```

### Option 2: Use Mock Backend

Use tools like:
- **Mockoon**: Local API mocking server
- **json-server**: Quick REST API from JSON file
- **Postman Mock Server**: Cloud-based mock

Then point `BASE_URL` to `http://localhost:3000` or your mock URL.

## Error Handling

All repository methods return `Result<T>`:

```kotlin
val result = deviceRepository.dispenseFood(deviceId, petId, grams)

when {
    result.isSuccess -> {
        val event = result.getOrNull()
        // Handle success
    }
    result.isFailure -> {
        val error = result.exceptionOrNull()
        when (error?.message) {
            "Device not connected" -> // Show connect dialog
            "Internet connection required" -> // Show offline message
            else -> // Show generic error
        }
    }
}
```

## Common Use Cases

### Use Case 1: Manual Food Dispense
```kotlin
viewModelScope.launch {
    val result = deviceRepository.dispenseFood(
        deviceId = "dev_001",
        petId = "pet_123",
        grams = 80,
        confirmationRequired = true
    )
    // Handle result
}
```

### Use Case 2: Schedule a Meal
```kotlin
val schedule = FeedingSchedule(
    id = UUID.randomUUID().toString(),
    deviceId = "dev_001",
    petId = "pet_123",
    time = "08:00",
    portion = 80,
    daysOfWeek = listOf(1, 2, 3, 4, 5), // Mon-Fri
    isEnabled = true
)

val result = deviceRepository.createFeedingSchedule("dev_001", schedule)
```

### Use Case 3: Capture Snapshot
```kotlin
val result = deviceRepository.captureSnapshot("dev_001")
if (result.isSuccess) {
    val snapshot = result.getOrNull()!!
    // Load snapshot.imageUrl with Coil
    AsyncImage(
        model = snapshot.imageUrl,
        contentDescription = "Camera snapshot"
    )
}
```

### Use Case 4: Set Security Mode
```kotlin
// When user leaves home
deviceRepository.setSecurityMode("dev_001", SecurityMode.AWAY)

// When user returns
deviceRepository.setSecurityMode("dev_001", SecurityMode.HOME)
```

## Next Steps

1. **Deploy Backend**: Deploy your cloud API (Node.js/Python/Go)
2. **Implement Bluetooth**: Create `BluetoothDeviceService` for local control
3. **Connect ViewModels**: Inject `DeviceRepository` into your existing ViewModels
4. **Test End-to-End**: Test with real hardware (ESP32 + Raspberry Pi)
5. **Add WebSocket**: For real-time telemetry instead of polling

## Documentation

- [`CONNECTIVITY_ARCHITECTURE.md`](CONNECTIVITY_ARCHITECTURE.md) - Full architecture overview
- [`CONNECTIVITY_IMPLEMENTATION.md`](CONNECTIVITY_IMPLEMENTATION.md) - Implementation details
- [`README.md`](README.md) - Project overview

## Support

For questions about the connectivity implementation:
1. Check the architecture docs first
2. Review the API service interface (`DeviceApiService.kt`)
3. Look at repository implementation (`DeviceRepository.kt`)
4. See examples in this quick start guide

---

**Status**: ✅ Infrastructure Ready | ⏳ Backend Integration Needed | ⏳ Bluetooth Implementation Needed
