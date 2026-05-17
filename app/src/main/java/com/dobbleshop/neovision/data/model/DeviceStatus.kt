package com.dobbleshop.neovision.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Device status telemetry from sensors
 * Corresponds to specification section 25.1 and 25.2
 */
data class DeviceStatus(
    val deviceId: String,
    // Sensor S1: HX711 + Load cell - Bowl weight
    val bowlWeightGrams: Float,
    
    // Sensor S2: VL53L0X - Food reservoir level
    val foodReservoirPercent: Int,
    val foodReservoirGrams: Int,
    val foodReservoirDaysRemaining: Int?,
    
    // Sensor S3: Water reservoir level
    val waterReservoirPercent: Int,
    val waterReservoirMl: Int,
    val waterReservoirDaysRemaining: Int?,
    
    // Sensor S4: Bowl food detection
    val bowlStatus: BowlStatus,
    
    // Sensor S5: DS3231 - Real-time clock
    val deviceTime: Long,
    
    // Sensor S6: mmWave presence sensor (home security)
    val presenceDetected: Boolean,
    val presenceIntensity: Float?,
    
    // Sensor S7: Camera status
    val cameraStatus: CameraStatus,
    
    // Sensor S9: Power/Battery telemetry
    val batteryPercent: Int,
    val isPlugged: Boolean,
    
    // General telemetry
    val uptime: Long,
    val wifiRssi: Int?,
    val internalTemperature: Float?,
    
    val timestamp: Long = System.currentTimeMillis()
)

enum class BowlStatus {
    EMPTY,
    LOW_QUANTITY,
    NORMAL,
    FULL,
    SENSOR_ERROR
}

enum class CameraStatus {
    OK,
    ERROR,
    OFFLINE
}

/**
 * Real-time device state for dashboard
 */
data class DeviceDashboard(
    val device: Device,
    val status: DeviceStatus,
    val nextMeal: FeedingSchedule?,
    val nextWaterRefill: Long?,
    val alerts: List<Alert>
)
