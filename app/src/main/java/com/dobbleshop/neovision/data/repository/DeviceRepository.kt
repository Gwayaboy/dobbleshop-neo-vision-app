package com.dobbleshop.neovision.data.repository

import com.dobbleshop.neovision.data.api.DeviceApiService
import com.dobbleshop.neovision.data.api.DispenseFoodRequest
import com.dobbleshop.neovision.data.api.DispenseWaterRequest
import com.dobbleshop.neovision.data.api.StreamRequest
import com.dobbleshop.neovision.data.api.StreamQuality
import com.dobbleshop.neovision.data.api.StreamProtocol
import com.dobbleshop.neovision.data.api.AudioStateRequest
import com.dobbleshop.neovision.data.api.SecurityModeRequest
import com.dobbleshop.neovision.data.api.SecurityMode
// Use type aliases to avoid ambiguity
import com.dobbleshop.neovision.data.api.DispenseEvent as ApiDispenseEvent
import com.dobbleshop.neovision.data.api.SecurityEvent as ApiSecurityEvent
import com.dobbleshop.neovision.data.api.CameraStreamSession
import com.dobbleshop.neovision.data.api.SnapshotResponse
import com.dobbleshop.neovision.data.connectivity.ConnectionManager
import com.dobbleshop.neovision.data.connectivity.ConnectionMode
import com.dobbleshop.neovision.data.connectivity.ConnectionState
import com.dobbleshop.neovision.data.connectivity.NetworkMonitor
import com.dobbleshop.neovision.data.device.BluetoothDeviceManager
import com.dobbleshop.neovision.data.device.DeviceController
import com.dobbleshop.neovision.data.model.Device
import com.dobbleshop.neovision.data.model.DeviceStatus
import com.dobbleshop.neovision.data.model.FeedingSchedule
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Unified repository for device operations
 * Abstracts connectivity details (cloud vs Bluetooth) from ViewModels
 * Automatically routes commands to appropriate transport based on connection state
 */
@Singleton
class DeviceRepository @Inject constructor(
    private val apiService: DeviceApiService,
    private val bluetoothManager: BluetoothDeviceManager,
    private val connectionManager: ConnectionManager,
    private val networkMonitor: NetworkMonitor
) {
    
    /**
     * Current connection state
     */
    val connectionState: Flow<ConnectionState> = connectionManager.connectionState
    
    /**
     * Network availability
     */
    val isNetworkAvailable: Flow<Boolean> = networkMonitor.networkState
        .map { state ->
            when (state) {
                is com.dobbleshop.neovision.data.connectivity.NetworkState.Available -> true
                is com.dobbleshop.neovision.data.connectivity.NetworkState.Capabilities -> state.hasInternet
                else -> false
            }
        }
    
    // ========== Connection Management ==========
    
    /**
     * Connect to device with automatic mode selection
     */
    suspend fun connectToDevice(device: Device, preferBluetooth: Boolean = false): Result<ConnectionState> {
        return connectionManager.connect(device, preferBluetooth)
    }
    
    /**
     * Disconnect from current device
     */
    suspend fun disconnect(): Result<Unit> {
        return connectionManager.disconnect()
    }
    
    /**
     * Test connection latency
     */
    suspend fun pingDevice(): Result<Long> {
        return connectionManager.pingDevice()
    }
    
    // ========== Device Status ==========
    
    /**
     * Get current device status
     * Routes to cloud or Bluetooth based on connection
     */
    suspend fun getDeviceStatus(deviceId: String): Result<DeviceStatus> {
        return when (val state = connectionState.first()) {
            is ConnectionState.ConnectedCloud,
            is ConnectionState.ConnectedHybrid -> {
                // Use cloud API
                try {
                    val response = apiService.getDeviceStatus(deviceId)
                    if (response.isSuccessful && response.body() != null) {
                        Result.success(response.body()!!)
                    } else {
                        Result.failure(Exception("API error: ${response.code()}"))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            is ConnectionState.ConnectedBluetooth -> {
                // Use Bluetooth (not implemented yet - would need to add method to BluetoothDeviceManager)
                Result.failure(Exception("Bluetooth status reading not yet implemented"))
            }
            else -> {
                Result.failure(Exception("Device not connected"))
            }
        }
    }
    
    /**
     * Observe device status in real-time
     * Only available via cloud connection (WebSocket)
     */
    fun observeDeviceStatus(deviceId: String): Flow<DeviceStatus> {
        // TODO: Implement WebSocket connection for real-time telemetry
        // For now, poll every 5 seconds
        return flow {
            while (true) {
                val result = getDeviceStatus(deviceId)
                if (result.isSuccess) {
                    emit(result.getOrThrow())
                }
                kotlinx.coroutines.delay(5000)
            }
        }
    }
    
    // ========== Food & Water Control ==========
    
    /**
     * Dispense food to pet
     */
    suspend fun dispenseFood(
        deviceId: String,
        petId: String,
        grams: Int,
        confirmationRequired: Boolean = true
    ): Result<ApiDispenseEvent> {
        return when (val state = connectionState.first()) {
            is ConnectionState.ConnectedCloud,
            is ConnectionState.ConnectedHybrid -> {
                // Use cloud API
                try {
                    val request = DispenseFoodRequest(petId, grams, confirmationRequired)
                    val response = apiService.dispenseFood(deviceId, request)
                    if (response.isSuccessful && response.body() != null) {
                        Result.success(response.body()!!)
                    } else {
                        Result.failure(Exception("Failed to dispense food: ${response.code()}"))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            is ConnectionState.ConnectedBluetooth -> {
                // Use Bluetooth (would need to implement in BluetoothDeviceManager)
                Result.failure(Exception("Bluetooth food dispensing not yet implemented"))
            }
            else -> {
                Result.failure(Exception("Device not connected"))
            }
        }
    }
    
    /**
     * Dispense water
     */
    suspend fun dispenseWater(deviceId: String, ml: Int): Result<ApiDispenseEvent> {
        return when (val state = connectionState.first()) {
            is ConnectionState.ConnectedCloud,
            is ConnectionState.ConnectedHybrid -> {
                try {
                    val request = DispenseWaterRequest(ml)
                    val response = apiService.dispenseWater(deviceId, request)
                    if (response.isSuccessful && response.body() != null) {
                        Result.success(response.body()!!)
                    } else {
                        Result.failure(Exception("Failed to dispense water: ${response.code()}"))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            is ConnectionState.ConnectedBluetooth -> {
                Result.failure(Exception("Bluetooth water dispensing not yet implemented"))
            }
            else -> {
                Result.failure(Exception("Device not connected"))
            }
        }
    }
    
    /**
     * Get dispense history
     */
    suspend fun getDispenseHistory(
        deviceId: String,
        fromTimestamp: Long,
        toTimestamp: Long,
        limit: Int = 100
    ): Result<List<ApiDispenseEvent>> {
        // Only available via cloud
        if (!networkMonitor.isInternetAvailable()) {
            return Result.failure(Exception("Internet connection required for history"))
        }
        
        return try {
            val response = apiService.getDispenseHistory(deviceId, fromTimestamp, toTimestamp, limit)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get history: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ========== Schedules ==========
    
    /**
     * Get feeding schedules
     */
    suspend fun getFeedingSchedules(deviceId: String): Result<List<FeedingSchedule>> {
        if (!networkMonitor.isInternetAvailable()) {
            return Result.failure(Exception("Internet connection required"))
        }
        
        return try {
            val response = apiService.getFeedingSchedules(deviceId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get schedules: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Create feeding schedule
     */
    suspend fun createFeedingSchedule(
        deviceId: String,
        schedule: FeedingSchedule
    ): Result<FeedingSchedule> {
        if (!networkMonitor.isInternetAvailable()) {
            return Result.failure(Exception("Internet connection required"))
        }
        
        return try {
            val response = apiService.createFeedingSchedule(deviceId, schedule)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to create schedule: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Update feeding schedule
     */
    suspend fun updateFeedingSchedule(
        deviceId: String,
        scheduleId: String,
        schedule: FeedingSchedule
    ): Result<FeedingSchedule> {
        if (!networkMonitor.isInternetAvailable()) {
            return Result.failure(Exception("Internet connection required"))
        }
        
        return try {
            val response = apiService.updateFeedingSchedule(deviceId, scheduleId, schedule)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to update schedule: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Delete feeding schedule
     */
    suspend fun deleteFeedingSchedule(deviceId: String, scheduleId: String): Result<Unit> {
        if (!networkMonitor.isInternetAvailable()) {
            return Result.failure(Exception("Internet connection required"))
        }
        
        return try {
            val response = apiService.deleteFeedingSchedule(deviceId, scheduleId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete schedule: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ========== Camera & Media ==========
    
    /**
     * Start camera stream
     * Returns HLS stream URL
     */
    suspend fun startCameraStream(
        deviceId: String,
        quality: StreamQuality = StreamQuality.AUTO
    ): Result<CameraStreamSession> {
        if (!networkMonitor.isInternetAvailable()) {
            return Result.failure(Exception("Internet connection required for camera"))
        }
        
        return try {
            val request = StreamRequest(quality = quality, protocol = StreamProtocol.HLS)
            val response = apiService.startCameraStream(deviceId, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to start stream: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Stop camera stream
     */
    suspend fun stopCameraStream(deviceId: String): Result<Unit> {
        return try {
            val response = apiService.stopCameraStream(deviceId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to stop stream: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Capture camera snapshot
     */
    suspend fun captureSnapshot(deviceId: String): Result<SnapshotResponse> {
        if (!networkMonitor.isInternetAvailable()) {
            return Result.failure(Exception("Internet connection required"))
        }
        
        return try {
            val response = apiService.captureSnapshot(deviceId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to capture snapshot: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ========== Audio Control ==========
    
    /**
     * Enable/disable speaker (speak to pet)
     */
    suspend fun setSpeakerEnabled(deviceId: String, enabled: Boolean): Result<Unit> {
        return try {
            val request = AudioStateRequest(enabled)
            val response = apiService.setSpeakerState(deviceId, request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to set speaker: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Enable/disable microphone (listen to pet)
     */
    suspend fun setMicrophoneEnabled(deviceId: String, enabled: Boolean): Result<Unit> {
        return try {
            val request = AudioStateRequest(enabled)
            val response = apiService.setMicrophoneState(deviceId, request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to set microphone: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ========== Security ==========
    
    /**
     * Set security mode (mmWave presence detection)
     */
    suspend fun setSecurityMode(deviceId: String, mode: SecurityMode): Result<Unit> {
        return try {
            val request = SecurityModeRequest(mode)
            val response = apiService.setSecurityMode(deviceId, request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to set security mode: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get security events
     */
    suspend fun getSecurityEvents(
        deviceId: String,
        fromTimestamp: Long,
        limit: Int = 50
    ): Result<List<ApiSecurityEvent>> {
        if (!networkMonitor.isInternetAvailable()) {
            return Result.failure(Exception("Internet connection required"))
        }
        
        return try {
            val response = apiService.getSecurityEvents(deviceId, fromTimestamp, limit)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get events: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
