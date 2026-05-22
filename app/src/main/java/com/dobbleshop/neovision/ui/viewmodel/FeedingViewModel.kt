package com.dobbleshop.neovision.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dobbleshop.neovision.data.api.DispenseEvent
import com.dobbleshop.neovision.data.model.FeedingSchedule
import com.dobbleshop.neovision.data.model.Pet
import com.dobbleshop.neovision.data.repository.DeviceRepository
import com.dobbleshop.neovision.data.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Feeding screen
 * Manages food/water dispensing and schedules
 */
@HiltViewModel
class FeedingViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val petRepository: PetRepository
) : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow(FeedingUiState())
    val uiState: StateFlow<FeedingUiState> = _uiState.asStateFlow()
    
    // Active pet
    private val _activePet = MutableStateFlow<Pet?>(null)
    val activePet: StateFlow<Pet?> = _activePet.asStateFlow()
    
    // Feeding schedules
    private val _feedingSchedules = MutableStateFlow<List<FeedingSchedule>>(emptyList())
    val feedingSchedules: StateFlow<List<FeedingSchedule>> = _feedingSchedules.asStateFlow()
    
    init {
        loadActivePet()
    }
    
    /**
     * Load active pet
     */
    private fun loadActivePet() {
        viewModelScope.launch {
            petRepository.getAllPets()
                .catch { error ->
                    _uiState.update { it.copy(errorMessage = error.message) }
                }
                .collect { pets ->
                    _activePet.value = pets.firstOrNull()
                }
        }
    }
    
    /**
     * Load feeding schedules for device
     */
    fun loadFeedingSchedules(deviceId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = deviceRepository.getFeedingSchedules(deviceId)
            
            _uiState.update {
                when {
                    result.isSuccess -> {
                        _feedingSchedules.value = result.getOrNull() ?: emptyList()
                        it.copy(isLoading = false)
                    }
                    result.isFailure -> it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                    else -> it
                }
            }
        }
    }
    
    /**
     * Dispense food manually
     */
    fun dispenseFood(deviceId: String, grams: Int, confirmationRequired: Boolean = true) {
        val petId = _activePet.value?.id
        if (petId == null) {
            _uiState.update { it.copy(errorMessage = "Aucun animal sélectionné") }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isDispensing = true, errorMessage = null) }
            
            val result = deviceRepository.dispenseFood(
                deviceId = deviceId,
                petId = petId,
                grams = grams,
                confirmationRequired = confirmationRequired
            )
            
            _uiState.update {
                when {
                    result.isSuccess -> it.copy(
                        isDispensing = false,
                        dispenseSuccess = true,
                        lastDispenseEvent = result.getOrNull(),
                        successMessage = "Distribué ${grams}g de croquettes"
                    )
                    result.isFailure -> it.copy(
                        isDispensing = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                    else -> it
                }
            }
        }
    }
    
    /**
     * Dispense water manually
     */
    fun dispenseWater(deviceId: String, ml: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDispensing = true, errorMessage = null) }
            
            val result = deviceRepository.dispenseWater(deviceId, ml)
            
            _uiState.update {
                when {
                    result.isSuccess -> it.copy(
                        isDispensing = false,
                        dispenseSuccess = true,
                        lastDispenseEvent = result.getOrNull(),
                        successMessage = "Distribué ${ml}ml d'eau"
                    )
                    result.isFailure -> it.copy(
                        isDispensing = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                    else -> it
                }
            }
        }
    }
    
    /**
     * Create new feeding schedule
     */
    fun createSchedule(deviceId: String, schedule: FeedingSchedule) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = deviceRepository.createFeedingSchedule(deviceId, schedule)
            
            _uiState.update {
                when {
                    result.isSuccess -> {
                        loadFeedingSchedules(deviceId)
                        it.copy(
                            isLoading = false,
                            successMessage = "Horaire créé"
                        )
                    }
                    result.isFailure -> it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                    else -> it
                }
            }
        }
    }
    
    /**
     * Update feeding schedule
     */
    fun updateSchedule(deviceId: String, scheduleId: String, schedule: FeedingSchedule) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = deviceRepository.updateFeedingSchedule(deviceId, scheduleId, schedule)
            
            _uiState.update {
                when {
                    result.isSuccess -> {
                        loadFeedingSchedules(deviceId)
                        it.copy(
                            isLoading = false,
                            successMessage = "Horaire mis à jour"
                        )
                    }
                    result.isFailure -> it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                    else -> it
                }
            }
        }
    }
    
    /**
     * Delete feeding schedule
     */
    fun deleteSchedule(deviceId: String, scheduleId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = deviceRepository.deleteFeedingSchedule(deviceId, scheduleId)
            
            _uiState.update {
                when {
                    result.isSuccess -> {
                        loadFeedingSchedules(deviceId)
                        it.copy(
                            isLoading = false,
                            successMessage = "Horaire supprimé"
                        )
                    }
                    result.isFailure -> it.copy(
                        isLoading = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                    else -> it
                }
            }
        }
    }
    
    /**
     * Clear success message
     */
    fun clearSuccessMessage() {
        _uiState.update { it.copy(dispenseSuccess = false, successMessage = null) }
    }
    
    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

/**
 * UI state for Feeding screen
 */
data class FeedingUiState(
    val isLoading: Boolean = false,
    val isDispensing: Boolean = false,
    val dispenseSuccess: Boolean = false,
    val lastDispenseEvent: DispenseEvent? = null,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
