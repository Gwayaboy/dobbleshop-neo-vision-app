package com.dobbleshop.neovision.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Pet/Animal profile model
 * Corresponds to specification section 11.1
 */
@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val species: Species,
    val breed: String? = null,
    val dateOfBirth: Long? = null,
    val ageMonths: Int? = null,
    val weightKg: Float,
    val sex: Sex? = null,
    val isSterilized: Boolean = false,
    val activityLevel: ActivityLevel,
    val nutritionalGoal: NutritionalGoal,
    val manualPortionSizeGrams: Int? = null,
    val dailyWaterTargetMl: Int? = null,
    val notes: String? = null,
    val photoUrl: String? = null,
    val deviceId: String,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class Species {
    CAT,
    DOG
}

enum class Sex {
    MALE,
    FEMALE
}

enum class ActivityLevel {
    LOW,
    NORMAL,
    HIGH
}

enum class NutritionalGoal {
    MAINTAIN,
    WEIGHT_CONTROL,
    PERFORMANCE
}

/**
 * Calculated daily ration recommendations
 * Corresponds to specification section 11.2 and 11.3
 */
data class DailyRation(
    val recommendedDailyFood: Int, // grams
    val recommendedMealsPerDay: Int,
    val foodPerMeal: Int, // grams
    val recommendedDailyWater: Int, // ml
    val waterRenewalVolume: Int // ml
)
