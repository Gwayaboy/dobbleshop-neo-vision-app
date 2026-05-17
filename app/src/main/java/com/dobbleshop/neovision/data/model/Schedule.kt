package com.dobbleshop.neovision.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Feeding schedule for automated meal distribution
 * Corresponds to specification section 9.6
 */
@Entity(tableName = "feeding_schedules")
data class FeedingSchedule(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val petId: String,
    val deviceId: String,
    val timeHour: Int, // 0-23
    val timeMinute: Int, // 0-59
    val portionGrams: Int,
    val daysOfWeek: Set<Int>, // 1-7 (Monday-Sunday)
    val isEnabled: Boolean = true,
    val restrictIfBowlNotEmpty: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Water renewal schedule
 * Corresponds to specification section 9.7
 */
@Entity(tableName = "water_schedules")
data class WaterSchedule(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val deviceId: String,
    val timeHour: Int,
    val timeMinute: Int,
    val volumeMl: Int,
    val renewalMode: WaterRenewalMode,
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

enum class WaterRenewalMode {
    PERIODIC, // Renew at scheduled times
    MAINTAIN_MINIMUM // Keep bowl at minimum level
}

/**
 * Distribution event history
 * Corresponds to specification section 9.9
 */
@Entity(tableName = "dispense_events")
data class DispenseEvent(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val deviceId: String,
    val petId: String?,
    val type: DispenseType,
    val requestedAmount: Int, // grams or ml
    val actualAmount: Int, // measured amount
    val result: DispenseResult,
    val reason: DispenseReason,
    val bowlWeightBefore: Float,
    val bowlWeightAfter: Float,
    val timestamp: Long = System.currentTimeMillis(),
    val errorCode: String? = null
)

enum class DispenseType {
    FOOD,
    WATER
}

enum class DispenseResult {
    SUCCESS,
    PARTIAL_SUCCESS,
    FAILED
}

enum class DispenseReason {
    MANUAL,
    SCHEDULED,
    EMERGENCY
}
