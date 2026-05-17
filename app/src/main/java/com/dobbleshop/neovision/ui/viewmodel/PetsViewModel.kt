package com.dobbleshop.neovision.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dobbleshop.neovision.data.model.Pet
import com.dobbleshop.neovision.data.model.DailyRation
import com.dobbleshop.neovision.data.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Pets screen
 * Manages pet list and CRUD operations
 */
@HiltViewModel
class PetsViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow<PetsUiState>(PetsUiState.Loading)
    val uiState: StateFlow<PetsUiState> = _uiState.asStateFlow()
    
    // Selected pet for details
    private val _selectedPet = MutableStateFlow<Pet?>(null)
    val selectedPet: StateFlow<Pet?> = _selectedPet.asStateFlow()
    
    // Show add pet dialog
    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()
    
    init {
        loadPets()
    }
    
    /**
     * Load all pets from database
     */
    private fun loadPets() {
        viewModelScope.launch {
            petRepository.getAllPets()
                .catch { error ->
                    _uiState.value = PetsUiState.Error(error.message ?: "Unknown error")
                }
                .collect { pets ->
                    if (pets.isEmpty()) {
                        _uiState.value = PetsUiState.Empty
                    } else {
                        _uiState.value = PetsUiState.Success(pets)
                    }
                }
        }
    }
    
    /**
     * Add new pet
     */
    fun addPet(pet: Pet) {
        viewModelScope.launch {
            try {
                petRepository.addPet(pet)
                _showAddDialog.value = false
            } catch (e: Exception) {
                _uiState.value = PetsUiState.Error(e.message ?: "Failed to add pet")
            }
        }
    }
    
    /**
     * Update existing pet
     */
    fun updatePet(pet: Pet) {
        viewModelScope.launch {
            try {
                petRepository.updatePet(pet)
            } catch (e: Exception) {
                _uiState.value = PetsUiState.Error(e.message ?: "Failed to update pet")
            }
        }
    }
    
    /**
     * Delete pet
     */
    fun deletePet(pet: Pet) {
        viewModelScope.launch {
            try {
                petRepository.deletePet(pet)
            } catch (e: Exception) {
                _uiState.value = PetsUiState.Error(e.message ?: "Failed to delete pet")
            }
        }
    }
    
    /**
     * Calculate daily ration for a pet
     */
    fun calculateDailyRation(pet: Pet): DailyRation {
        return petRepository.calculateDailyRation(pet)
    }
    
    /**
     * Show add pet dialog
     */
    fun showAddPetDialog() {
        _showAddDialog.value = true
    }
    
    /**
     * Hide add pet dialog
     */
    fun hideAddPetDialog() {
        _showAddDialog.value = false
    }
    
    /**
     * Select pet for details view
     */
    fun selectPet(pet: Pet) {
        _selectedPet.value = pet
    }
    
    /**
     * Clear selected pet
     */
    fun clearSelection() {
        _selectedPet.value = null
    }
}

/**
 * UI state for Pets screen
 */
sealed class PetsUiState {
    data object Loading : PetsUiState()
    data object Empty : PetsUiState()
    data class Success(val pets: List<Pet>) : PetsUiState()
    data class Error(val message: String) : PetsUiState()
}
