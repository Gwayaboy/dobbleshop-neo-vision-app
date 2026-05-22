package com.dobbleshop.neovision.data.connectivity

import com.dobbleshop.neovision.data.model.Device
import kotlinx.coroutines.flow.Flow

/**
 * Connection state representing current device connectivity
 */
sealed class ConnectionState {
    object Disconnected : ConnectionState()
    object Connecting : ConnectionState()
    
    data class ConnectedBluetooth(
        val deviceId: String,
        val rssi: Int,
        val latencyMs: Long
    ) : ConnectionState()
    
    data class ConnectedCloud(
        val deviceId: String,
        val latencyMs: Long,
        val serverRegion: String
    ) : ConnectionState()
    
    data class ConnectedHybrid(
        val deviceId: String,
        val primaryMode: ConnectionMode
    ) : ConnectionState()
    
    data class Error(
        val message: String,
        val cause: Throwable?,
        val isRecoverable: Boolean = true
    ) : ConnectionState()
}

enum class ConnectionMode {
    CLOUD,      // Internet via WiFi/Mobile data
    BLUETOOTH,  // Local BLE connection
    HYBRID      // Both available, automatic switching
}

/**
 * Manages device connectivity and switching between cloud and Bluetooth modes
 */
interface ConnectionManager {
    
    /**
     * Observe current connection state
     */
    val connectionState: Flow<ConnectionState>
    
    /**
     * Current connection mode preference
     */
    val preferredMode: Flow<ConnectionMode>
    
    /**
     * Connect to device with specified mode preference
     * @param device Device to connect to
     * @param preferBluetooth True to prefer BLE over cloud (for nearby control)
     * @param forceMode Force specific mode (null = automatic selection)
     */
    suspend fun connect(
        device: Device,
        preferBluetooth: Boolean = false,
        forceMode: ConnectionMode? = null
    ): Result<ConnectionState>
    
    /**
     * Disconnect from current device
     */
    suspend fun disconnect(): Result<Unit>
    
    /**
     * Switch to cloud mode (if available)
     */
    suspend fun switchToCloud(): Result<Unit>
    
    /**
     * Switch to Bluetooth mode (if available)
     */
    suspend fun switchToBluetooth(): Result<Unit>
    
    /**
     * Test latency to device via current connection
     */
    suspend fun pingDevice(): Result<Long>
    
    /**
     * Check if device is reachable via cloud
     */
    suspend fun isCloudReachable(deviceId: String): Boolean
    
    /**
     * Check if device is reachable via Bluetooth
     */
    suspend fun isBluetoothReachable(deviceId: String): Boolean
    
    /**
     * Get recommended connection mode based on current conditions
     */
    suspend fun getRecommendedMode(deviceId: String): ConnectionMode
}

/**
 * Connection quality metrics
 */
data class ConnectionQuality(
    val latencyMs: Long,
    val signalStrength: Int, // RSSI for BLE, WiFi bars for cloud
    val stability: ConnectionStability,
    val bandwidthKbps: Int?,
    val packetLoss: Float
)

enum class ConnectionStability {
    EXCELLENT,  // < 50ms latency, < 1% loss
    GOOD,       // < 150ms latency, < 5% loss
    FAIR,       // < 500ms latency, < 15% loss
    POOR        // > 500ms latency or > 15% loss
}
