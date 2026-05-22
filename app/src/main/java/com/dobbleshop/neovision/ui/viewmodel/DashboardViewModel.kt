package com.dobbleshop.neovision.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dobbleshop.neovision.data.api.SecurityMode
import com.dobbleshop.neovision.data.connectivity.ConnectionState
import com.dobbleshop.neovision.data.model.Device
import com.dobbleshop.neovision.data.model.DeviceStatus
import com.dobbleshop.neovision.data.model.Pet
import com.dobbleshop.neovision.data.repository.DeviceRepository
import com.dobbleshop.neovision.data.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Dashboard (Home) screen
 * Manages device status, connection state, and quick actions
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val petRepository: PetRepository
) : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    
    // Active pet
    private val _activePet = MutableStateFlow<Pet?>(null)
    val activePet: StateFlow<Pet?> = _activePet.asStateFlow()
    
    // Connection state
    val connectionState: StateFlow<ConnectionState> = deviceRepository.connectionState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConnectionState.Disconnected
        )
    
    // Network availability
    val isNetworkAvailable: StateFlow<Boolean> = deviceRepository.isNetworkAvailable
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    
    init {
        loadActivePet()
    }
    
    /**
     * Load the first pet as active pet
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
     * Connect to device
     */
    fun connectToDevice(deviceId: String, preferBluetooth: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isConnecting = true, errorMessage = null) }
            
            // Create a mock device for now
            val device = Device(
                id = deviceId,
                serialNumber = "NEO-001",
                model = com.dobbleshop.neovision.data.model.DeviceModel.NEO_VISION,
                variant = com.dobbleshop.neovision.data.model.DeviceVariant.CAT,
                hardwareRevision = "1.0",
                esp32FirmwareVersion = "1.0.0",
                rpiFirmwareVersion = "1.0.0",
                connectivityStatus = com.dobbleshop.neovision.data.model.ConnectivityStatus.WIFI_CONNECTED,
                batteryStatus = com.dobbleshop.neovision.data.model.BatteryStatus(
                    percentage = 85,
                    isCharging = false,
                    voltage = 3.7f
                ),
                lastSeenAt = System.currentTimeMillis(),
                timezone = "Europe/Paris",
                isOnline = true
            )
            
            val result = deviceRepository.connectToDevice(device, preferBluetooth)
            
            _uiState.update {
                when {
                    result.isSuccess -> it.copy(
                        isConnecting = false,
                        isConnected = true,
                        deviceId = deviceId
                    )
                    result.isFailure -> it.copy(
                        isConnecting = false,
                        isConnected = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                    else -> it
                }
            }
            
            // Start observing device status if connected
            if (result.isSuccess) {
                observeDeviceStatus(deviceId)
            }
        }
    }
    
    /**
     * Observe device status in real-time
     */
    private fun observeDeviceStatus(deviceId: String) {
        viewModelScope.launch {
            deviceRepository.observeDeviceStatus(deviceId)
                .catch { error ->
                    _uiState.update { it.copy(errorMessage = error.message) }
                }
                .collect { status ->
                    _uiState.update { it.copy(deviceStatus = status) }
                }
        }
    }
    
    /**
     * Disconnect from device
     */
    fun disconnect() {
        viewModelScope.launch {
            deviceRepository.disconnect()
            _uiState.update {
                it.copy(
                    isConnected = false,
                    deviceId = null,
                    deviceStatus = null
                )
            }
        }
    }
    
    /**
     * Quick feed action from dashboard
     */
    fun quickFeed(grams: Int) {
        val deviceId = _uiState.value.deviceId ?: return
        val petId = _activePet.value?.id ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = deviceRepository.dispenseFood(
                deviceId = deviceId,
                petId = petId,
                grams = grams,
                confirmationRequired = false
            )
            
            _uiState.update {
                when {
                    result.isSuccess -> it.copy(
                        isLoading = false,
                        showSuccess = true,
                        successMessage = "Distribué ${grams}g de nourriture"
                    )
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
     * Ping device to check latency
     */
    fun pingDevice() {
        viewModelScope.launch {
            val result = deviceRepository.pingDevice()
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(latencyMs = result.getOrNull())
                }
            }
        }
    }
    
    /**
     * Clear success message
     */
    fun clearSuccessMessage() {
        _uiState.update { it.copy(showSuccess = false, successMessage = null) }
    }
    
    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

/**
 * UI state for Dashboard screen
 */
data class DashboardUiState(
    val isConnecting: Boolean = false,
    val isConnected: Boolean = false,
    val isLoading: Boolean = false,
    val deviceId: String? = null,
    val deviceStatus: DeviceStatus? = null,
    val latencyMs: Long? = null,
    val showSuccess: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
