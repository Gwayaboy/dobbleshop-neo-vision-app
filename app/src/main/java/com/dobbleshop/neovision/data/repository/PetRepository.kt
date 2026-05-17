package com.dobbleshop.neovision.data.repository

import com.dobbleshop.neovision.data.local.PetDao
import com.dobbleshop.neovision.data.model.Pet
import com.dobbleshop.neovision.data.model.DailyRation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

/**
 * Repository for Pet data management
 * Handles local database operations and business logic
 */
@Singleton
class PetRepository @Inject constructor(
    private val petDao: PetDao
) {
    
    /**
     * Get all pets as Flow for real-time updates
     */
    fun getAllPets(): Flow<List<Pet>> = petDao.getAllPets()
    
    /**
     * Get active pets only
     */
    fun getActivePets(): Flow<List<Pet>> = petDao.getActivePets()
    
    /**
     * Get pets for a specific device
     */
    fun getPetsByDevice(deviceId: String): Flow<List<Pet>> = 
        petDao.getPetsByDevice(deviceId)
    
    /**
     * Get single pet by ID
     */
    suspend fun getPetById(petId: String): Pet? = petDao.getPetById(petId)
    
    /**
     * Add new pet
     */
    suspend fun addPet(pet: Pet) {
        petDao.insertPet(pet.copy(
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        ))
    }
    
    /**
     * Update existing pet
     */
    suspend fun updatePet(pet: Pet) {
        petDao.updatePet(pet.copy(updatedAt = System.currentTimeMillis()))
    }
    
    /**
     * Delete pet
     */
    suspend fun deletePet(pet: Pet) {
        petDao.deletePet(pet)
    }
    
    /**
     * Deactivate pet (soft delete)
     */
    suspend fun deactivatePet(petId: String) {
        petDao.deactivatePet(petId)
    }
    
    /**
     * Activate pet
     */
    suspend fun activatePet(petId: String) {
        petDao.activatePet(petId)
    }
    
    /**
     * Calculate daily ration based on pet characteristics
     * Uses RER (Resting Energy Requirement) formula
     */
    fun calculateDailyRation(pet: Pet): DailyRation {
        // Calculate RER (Resting Energy Requirement) in kcal
        val rer = 70.0 * Math.pow(pet.weightKg.toDouble(), 0.75)
        
        // Activity multiplier
        val activityMultiplier = when (pet.activityLevel) {
            com.dobbleshop.neovision.data.model.ActivityLevel.LOW -> 1.2
            com.dobbleshop.neovision.data.model.ActivityLevel.NORMAL -> 1.4
            com.dobbleshop.neovision.data.model.ActivityLevel.HIGH -> 1.8
        }
        
        // Nutritional goal adjustment
        val goalMultiplier = when (pet.nutritionalGoal) {
            com.dobbleshop.neovision.data.model.NutritionalGoal.WEIGHT_CONTROL -> 0.8
            com.dobbleshop.neovision.data.model.NutritionalGoal.MAINTAIN -> 1.0
            com.dobbleshop.neovision.data.model.NutritionalGoal.PERFORMANCE -> 1.3
        }
        
        // Daily energy requirement
        val dailyKcal = (rer * activityMultiplier * goalMultiplier).toInt()
        
        // Assume average dry food = 350 kcal per 100g
        val dailyFoodGrams = pet.manualPortionSizeGrams ?: ((dailyKcal / 350.0) * 100).toInt()
        
        // Meals per day based on species and weight
        val mealsPerDay = when {
            pet.species == com.dobbleshop.neovision.data.model.Species.CAT -> 3
            pet.weightKg < 10 -> 3
            pet.weightKg < 25 -> 2
            else -> 2
        }
        
        val foodPerMeal = dailyFoodGrams / mealsPerDay
        
        // Water: approximately 50-70 ml per kg of body weight
        val dailyWaterMl = pet.dailyWaterTargetMl ?: (pet.weightKg * 60).toInt()
        
        // Water renewal: typically 2-3 times per day
        val waterRenewalVolume = dailyWaterMl / 3
        
        return DailyRation(
            recommendedDailyFood = dailyFoodGrams,
            recommendedMealsPerDay = mealsPerDay,
            foodPerMeal = foodPerMeal,
            recommendedDailyWater = dailyWaterMl,
            waterRenewalVolume = waterRenewalVolume
        )
    }
}
