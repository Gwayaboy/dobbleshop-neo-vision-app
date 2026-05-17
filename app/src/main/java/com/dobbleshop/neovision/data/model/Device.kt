package com.dobbleshop.neovision.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Device model representing the DOBBLESHOP NEO VISION hardware
 * Corresponds to specification section 16.3
 */
@Entity(tableName = "devices")
data class Device(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val serialNumber: String,
    val model: DeviceModel,
    val variant: DeviceVariant,
    val hardwareRevision: String,
    val esp32FirmwareVersion: String,
    val rpiFirmwareVersion: String,
    val connectivityStatus: ConnectivityStatus,
    val batteryStatus: BatteryStatus,
    val lastSeenAt: Long,
    val timezone: String,
    val isOnline: Boolean = false,
    val wifiSsid: String? = null,
    val wifiSignalStrength: Int? = null
)

enum class DeviceModel {
    NEO_VISION
}

enum class DeviceVariant {
    CAT,
    DOG
}

enum class ConnectivityStatus {
    BLUETOOTH_ONLY,
    WIFI_CONNECTED,
    OFFLINE
}

data class BatteryStatus(
    val percentage: Int,
    val isCharging: Boolean,
    val voltage: Float
)
