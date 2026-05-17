package com.dobbleshop.neovision.data.device

import com.dobbleshop.neovision.data.model.*
import kotlinx.coroutines.flow.Flow

/**
 * WiFi/Cloud device communication interface for real-time control
 * Corresponds to specification section 18.2
 */
interface DeviceController {
    
    /**
     * Get real-time device status stream
     */
    fun observeDeviceStatus(deviceId: String): Flow<DeviceStatus>
    
    /**
     * Send manual feed command
     * Corresponds to specification section 9.8
     */
    suspend fun dispenseFood(
        deviceId: String,
        petId: String,
        grams: Int,
        confirmationRequired: Boolean = true
    ): Result<DispenseEvent>
    
    /**
     * Send manual water dispense command
     */
    suspend fun dispenseWater(
        deviceId: String,
        ml: Int
    ): Result<DispenseEvent>
    
    /**
     * Update feeding schedule
     */
    suspend fun updateFeedingSchedule(
        deviceId: String,
        schedule: FeedingSchedule
    ): Result<Unit>
    
    /**
     * Delete feeding schedule
     */
    suspend fun deleteFeedingSchedule(
        deviceId: String,
        scheduleId: String
    ): Result<Unit>
    
    /**
     * Update water renewal schedule
     */
    suspend fun updateWaterSchedule(
        deviceId: String,
        schedule: WaterSchedule
    ): Result<Unit>
    
    /**
     * Request camera live stream URL
     * Corresponds to specification section 13.1
     */
    suspend fun startCameraStream(deviceId: String): Result<CameraStreamSession>
    
    /**
     * Stop camera stream
     */
    suspend fun stopCameraStream(deviceId: String): Result<Unit>
    
    /**
     * Take snapshot from camera
     */
    suspend fun captureSnapshot(deviceId: String): Result<String> // Returns image URL
    
    /**
     * Enable/disable speaker (speak to pet)
     * Corresponds to specification section 13.2
     */
    suspend fun enableSpeaker(deviceId: String, enable: Boolean): Result<Unit>
    
    /**
     * Enable/disable microphone (listen to environment)
     */
    suspend fun enableMicrophone(deviceId: String, enable: Boolean): Result<Unit>
    
    /**
     * Set home security mode
     * Corresponds to specification section 13.3
     */
    suspend fun setSecurityMode(
        deviceId: String,
        mode: SecurityMode
    ): Result<Unit>
    
    /**
     * Start firmware OTA update
     * Corresponds to specification section 19.4
     */
    suspend fun startFirmwareUpdate(
        deviceId: String,
        targetVersion: String
    ): Result<FirmwareUpdateProgress>
    
    /**
     * Observe firmware update progress
     */
    fun observeFirmwareUpdateProgress(deviceId: String): Flow<FirmwareUpdateProgress>
    
    /**
     * Run hardware diagnostics
     * Corresponds to specification section 19.3
     */
    suspend fun runDiagnostics(deviceId: String): Result<DiagnosticsReport>
    
    /**
     * Sync device settings from cloud
     */
    suspend fun syncFromCloud(deviceId: String): Result<Unit>
    
    /**
     * Push current settings to device
     */
    suspend fun syncToDevice(deviceId: String): Result<Unit>
}

/**
 * Camera stream session info
 */
data class CameraStreamSession(
    val streamUrl: String,
    val protocol: String, // "HLS", "WebRTC", etc.
    val expiresAt: Long,
    val sessionId: String
)

/**
 * Security mode settings
 */
enum class SecurityMode {
    DISABLED,
    SIMPLE_SURVEILLANCE, // Detection + notification
    REINFORCED_SURVEILLANCE // Detection + capture + clip + audio
}

/**
 * Firmware update progress
 */
data class FirmwareUpdateProgress(
    val deviceId: String,
    val currentVersion: String,
    val targetVersion: String,
    val status: FirmwareUpdateStatus,
    val progressPercent: Int,
    val message: String?
)

enum class FirmwareUpdateStatus {
    DOWNLOADING,
    INSTALLING,
    VERIFYING,
    COMPLETED,
    FAILED
}

/**
 * Hardware diagnostics report
 */
data class DiagnosticsReport(
    val deviceId: String,
    val timestamp: Long,
    val esp32Status: ComponentStatus,
    val rpiStatus: ComponentStatus,
    val cameraStatus: ComponentStatus,
    val foodMotorStatus: ComponentStatus,
    val waterPumpStatus: ComponentStatus,
    val loadCellStatus: ComponentStatus,
    val foodLevelSensorStatus: ComponentStatus,
    val waterLevelSensorStatus: ComponentStatus,
    val presenceSensorStatus: ComponentStatus,
    val microphoneStatus: ComponentStatus,
    val speakerStatus: ComponentStatus,
    val rtcStatus: ComponentStatus,
    val wifiStatus: ComponentStatus,
    val bluetoothStatus: ComponentStatus
)

data class ComponentStatus(
    val name: String,
    val status: HealthStatus,
    val message: String? = null,
    val lastTestAt: Long
)

enum class HealthStatus {
    OK,
    WARNING,
    ERROR
}
