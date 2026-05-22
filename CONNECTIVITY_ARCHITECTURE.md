# DOBBLESHOP NEO VISION - Connectivity Architecture

## Overview
The app communicates with the physical DOBBLESHOP NEO VISION pet feeder through two connectivity channels:
- **Internet (Cloud/WiFi)**: Remote access from anywhere
- **Bluetooth Low Energy (BLE)**: Local direct connection

## Hardware Components

### ESP32-S3 (Main Controller)
- **Connectivity**: BLE 5.0 + WiFi 802.11 b/g/n
- **Controls**: Servo motor (food), peristaltic pump (water), sensors
- **Sensors**: HX711 load cell, VL53L0X distance, water level, DS3231 RTC
- **Power**: Battery + USB-C charging with INA219 monitoring

### Raspberry Pi Zero 2W (Media & Security)
- **Connectivity**: WiFi 802.11 b/g/n
- **Camera**: USB camera with H.264 encoding
- **Audio**: INMP441 microphone (input), MAX98357A speaker (output)
- **Security**: LD2410C mmWave presence sensor
- **Streaming**: HLS/WebRTC video streaming

## Connectivity Modes

### 1. Cloud Mode (Primary - Internet Required)
**Use Case**: Remote monitoring and control from anywhere

**Communication Flow**:
```
[Android App] <--> [HTTPS/WSS] <--> [Cloud Backend] <--> [MQTT/CoAP] <--> [Device]
```

**Features Available**:
- ✅ Real-time device status monitoring
- ✅ Manual food/water dispensing
- ✅ Schedule management (CRUD operations)
- ✅ Live camera streaming (HLS/WebRTC)
- ✅ Two-way audio communication
- ✅ Security mode control (mmWave sensor)
- ✅ Historical data and analytics
- ✅ Push notifications
- ✅ OTA firmware updates

**Requirements**:
- Device connected to WiFi network
- Internet connectivity on phone
- Valid authentication token

### 2. Local Bluetooth Mode (Fallback - No Internet Required)
**Use Case**: Device setup, local control when WiFi unavailable

**Communication Flow**:
```
[Android App] <--> [BLE GATT] <--> [ESP32-S3]
```

**Features Available**:
- ✅ Device discovery and pairing
- ✅ WiFi provisioning (onboarding)
- ✅ Manual food/water dispensing (limited)
- ✅ Real-time sensor readings
- ✅ Basic device status

**Limitations**:
- ❌ No camera/audio access (Raspberry Pi requires WiFi)
- ❌ No historical data
- ❌ No schedule sync with cloud
- ❌ Limited range (~10 meters)

### 3. Hybrid Mode (Optimal)
**Use Case**: Seamless switching based on availability

**Behavior**:
- Prefer cloud connection when available (lower battery drain)
- Fallback to BLE when device is offline or app has no internet
- Automatically switch back to cloud when connectivity restored
- Queue commands locally and sync when cloud available

## Android App Architecture

### Connection Manager
```kotlin
sealed class ConnectionState {
    object Disconnected : ConnectionState()
    object Connecting : ConnectionState()
    data class ConnectedBluetooth(val deviceId: String, val rssi: Int) : ConnectionState()
    data class ConnectedCloud(val deviceId: String, val latency: Long) : ConnectionState()
    data class ConnectedHybrid(val deviceId: String) : ConnectionState()
    data class Error(val message: String, val cause: Throwable?) : ConnectionState()
}

interface ConnectionManager {
    val connectionState: Flow<ConnectionState>
    suspend fun connect(deviceId: String, preferBluetooth: Boolean = false): Result<Unit>
    suspend fun disconnect(): Result<Unit>
    suspend fun switchToCloud(): Result<Unit>
    suspend fun switchToBluetooth(): Result<Unit>
}
```

### Communication Layers

#### 1. API Service (Internet - Retrofit + WebSocket)
**File**: `app/src/main/java/com/dobbleshop/neovision/data/api/DeviceApiService.kt`

- REST API for commands (food, water, schedules)
- WebSocket for real-time telemetry stream
- HLS endpoint for camera streaming
- WebRTC signaling for two-way audio

**Endpoints**:
```
POST   /api/v1/devices/{deviceId}/dispense/food
POST   /api/v1/devices/{deviceId}/dispense/water
GET    /api/v1/devices/{deviceId}/status
WS     /api/v1/devices/{deviceId}/stream/telemetry
GET    /api/v1/devices/{deviceId}/stream/camera.m3u8
POST   /api/v1/devices/{deviceId}/audio/speaker/enable
POST   /api/v1/devices/{deviceId}/security/mode
```

#### 2. Bluetooth Service (Local - BLE GATT)
**File**: `app/src/main/java/com/dobbleshop/neovision/data/device/BluetoothDeviceService.kt`

- GATT characteristics for commands
- Notifications for sensor data
- WiFi provisioning during onboarding

**GATT Profile**:
```
Service UUID: 6E400001-B5A3-F393-E0A9-E50E24DCCA9E (Nordic UART)
├── TX Characteristic (Notify): 6E400003-B5A3-F393-E0A9-E50E24DCCA9E
├── RX Characteristic (Write):  6E400002-B5A3-F393-E0A9-E50E24DCCA9E
└── Status Characteristic:      6E400004-B5A3-F393-E0A9-E50E24DCCA9E
```

**Command Protocol** (Binary - Protobuf or Custom):
```
[Header: 1 byte] [Command: 1 byte] [Length: 2 bytes] [Payload: N bytes] [CRC: 2 bytes]

Commands:
0x01 - Dispense Food (payload: petId, grams)
0x02 - Dispense Water (payload: ml)
0x10 - Get Status
0x20 - WiFi Provision (payload: ssid, password)
```

#### 3. Repository Layer (Abstraction)
**File**: `app/src/main/java/com/dobbleshop/neovision/data/repository/DeviceRepository.kt`

Abstracts connectivity details from ViewModels:
```kotlin
class DeviceRepository(
    private val apiService: DeviceApiService,
    private val bluetoothService: BluetoothDeviceService,
    private val connectionManager: ConnectionManager
) {
    suspend fun dispenseFood(deviceId: String, petId: String, grams: Int): Result<DispenseEvent> {
        return when (val state = connectionManager.connectionState.first()) {
            is ConnectionState.ConnectedCloud -> apiService.dispenseFood(deviceId, petId, grams)
            is ConnectionState.ConnectedBluetooth -> bluetoothService.dispenseFood(petId, grams)
            else -> Result.failure(Exception("Device not connected"))
        }
    }
}
```

## Camera Streaming

### Remote (Internet)
**Protocol**: HLS (HTTP Live Streaming) from Raspberry Pi
**Format**: H.264 video, AAC audio
**Latency**: 2-5 seconds
**Implementation**: Media3 ExoPlayer

```kotlin
// In CameraScreen
val streamUrl = "https://api.dobbleshop.com/devices/${deviceId}/stream/camera.m3u8"
player.setMediaItem(MediaItem.fromUri(streamUrl))
player.prepare()
player.play()
```

### Local (Bluetooth + WiFi)
**Protocol**: Direct HTTP streaming from Pi (device creates local hotspot or same WiFi)
**Format**: MJPEG or H.264
**Latency**: <1 second
**Implementation**: OkHttp + bitmap decoding

## Audio (Two-Way)

### Speak to Pet (Speaker)
**Remote**: WebRTC audio channel or HTTP POST raw audio
**Local**: BLE command triggers Pi to fetch audio via local HTTP

### Hear Pet (Microphone)
**Remote**: WebRTC audio channel or WebSocket audio stream
**Local**: Pi streams to local HTTP endpoint, app pulls via OkHttp

## Security Considerations

### Internet Communication
- ✅ TLS 1.3 for all HTTPS connections
- ✅ Certificate pinning
- ✅ JWT authentication tokens (refresh + access)
- ✅ End-to-end encryption for sensitive commands
- ✅ Rate limiting and abuse prevention

### Bluetooth Communication
- ✅ BLE pairing with PIN code
- ✅ Encrypted GATT characteristics (BLE Security Level 2+)
- ✅ Session tokens expire after onboarding
- ✅ Only allowed during setup or when device offline

## Offline Handling

### Queued Commands
When device offline or no internet:
- Commands stored locally in Room database
- Synced when connectivity restored
- User notified of pending sync status

### Cached Data
- Last known device status cached (30 min TTL)
- Schedule data cached locally
- Camera snapshots cached

### Conflict Resolution
- Server timestamp wins for schedules
- Last-write-wins for settings
- Manual dispensing always takes precedence

## Network State Monitoring

```kotlin
class NetworkMonitor(context: Context) {
    val networkState: Flow<NetworkState> = callbackFlow {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(NetworkState.Available)
            }
            override fun onLost(network: Network) {
                trySend(NetworkState.Lost)
            }
        }
        connectivityManager?.registerDefaultNetworkCallback(callback)
        awaitClose { connectivityManager?.unregisterNetworkCallback(callback) }
    }
}
```

## Power Optimization

### Cloud Mode
- WebSocket keepalive: 30 seconds
- Telemetry polling: 5 seconds when active, 60 seconds background
- Camera stream: Only when screen active

### Bluetooth Mode
- GATT connection interval: 100ms active, 1000ms idle
- Disconnect after 5 minutes inactivity
- Wake on notification when sensor alert

## Error Handling & Retry Logic

### Connection Failures
1. Retry cloud connection 3 times (exponential backoff)
2. Fallback to Bluetooth if available
3. Show user-friendly error message
4. Queue command for later if both fail

### Timeouts
- API calls: 10 seconds
- BLE operations: 5 seconds
- Camera stream buffering: 30 seconds
- Audio latency threshold: 500ms

## Development Phases

### Phase 1: Mock Implementation ✅ (Current)
- UI complete with mock data
- Interface definitions in place
- Room database for local storage

### Phase 2: Bluetooth Implementation 🔄 (Next)
- BLE device scanning and pairing
- WiFi provisioning flow
- Basic commands (dispense food/water)
- Sensor status reading

### Phase 3: Cloud API Implementation
- REST API integration with Retrofit
- WebSocket telemetry streaming
- Authentication and token management
- Command queuing and sync

### Phase 4: Media Streaming
- HLS camera stream with ExoPlayer
- Two-way audio with WebRTC
- Snapshot capture and caching

### Phase 5: Production Hardening
- Error handling and retry logic
- Offline mode and queue management
- Performance optimization
- Security hardening

## Testing Strategy

### Unit Tests
- Connection state machine
- Command serialization/deserialization
- Repository layer logic

### Integration Tests
- API service with mock server
- Bluetooth service with mock device
- Connection switching scenarios

### End-to-End Tests
- Complete feeding workflow
- Camera streaming
- Offline and recovery scenarios

---

**Last Updated**: May 21, 2026
**Status**: Phase 1 Complete, Phase 2 Planning
