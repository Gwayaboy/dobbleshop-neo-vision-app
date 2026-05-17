package com.dobbleshop.neovision.data.local

import androidx.room.*
import com.dobbleshop.neovision.data.model.Pet
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Pet entity
 */
@Dao
interface PetDao {
    
    @Query("SELECT * FROM pets ORDER BY createdAt DESC")
    fun getAllPets(): Flow<List<Pet>>
    
    @Query("SELECT * FROM pets WHERE id = :petId")
    suspend fun getPetById(petId: String): Pet?
    
    @Query("SELECT * FROM pets WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getActivePets(): Flow<List<Pet>>
    
    @Query("SELECT * FROM pets WHERE deviceId = :deviceId ORDER BY createdAt DESC")
    fun getPetsByDevice(deviceId: String): Flow<List<Pet>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet)
    
    @Update
    suspend fun updatePet(pet: Pet)
    
    @Delete
    suspend fun deletePet(pet: Pet)
    
    @Query("DELETE FROM pets WHERE id = :petId")
    suspend fun deletePetById(petId: String)
    
    @Query("UPDATE pets SET isActive = 0 WHERE id = :petId")
    suspend fun deactivatePet(petId: String)
    
    @Query("UPDATE pets SET isActive = 1 WHERE id = :petId")
    suspend fun activatePet(petId: String)
}
