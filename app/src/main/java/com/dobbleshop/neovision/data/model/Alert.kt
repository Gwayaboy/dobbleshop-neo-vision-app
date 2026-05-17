package com.dobbleshop.neovision.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Alert/Notification model
 * Corresponds to specification section 14
 */
@Entity(tableName = "alerts")
data class Alert(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val deviceId: String,
    val type: AlertType,
    val priority: AlertPriority,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val actionLink: String? = null
)

enum class AlertType {
    FOOD_LOW,
    WATER_LOW,
    FOOD_RESERVOIR_CRITICAL,
    WATER_RESERVOIR_CRITICAL,
    BOWL_NOT_EMPTY,
    DISTRIBUTION_FAILED,
    DISTRIBUTION_INCOMPLETE,
    SENSOR_FAULT,
    MOTOR_BLOCKED,
    PUMP_ERROR,
    NETWORK_LOST,
    PRESENCE_DETECTED, // Home security
    BATTERY_LOW,
    FIRMWARE_AVAILABLE,
    MAINTENANCE_REMINDER
}

enum class AlertPriority {
    CRITICAL, // Push + In-app
    HIGH,     // Push
    MEDIUM,   // In-app + optional push
    LOW       // In-app only
}

/**
 * Home security event
 * Corresponds to specification section 13
 */
@Entity(tableName = "security_events")
data class SecurityEvent(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val deviceId: String,
    val detectedAt: Long,
    val confidence: Float, // 0.0 - 1.0
    val snapshotUrl: String?,
    val videoClipUrl: String?,
    val notes: String? = null
)
