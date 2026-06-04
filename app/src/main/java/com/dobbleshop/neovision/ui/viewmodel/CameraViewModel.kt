package com.dobbleshop.neovision.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dobbleshop.neovision.data.api.*
import com.dobbleshop.neovision.data.repository.DeviceRepository
import com.dobbleshop.neovision.ui.camera.CameraPlugin
import com.dobbleshop.neovision.ui.camera.CameraPluginDescriptor
import com.dobbleshop.neovision.ui.camera.CameraPluginType
import com.dobbleshop.neovision.ui.camera.FeederCameraPlugin
import com.dobbleshop.neovision.ui.camera.PhoneCameraPlugin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Camera screen
 * Manages camera streaming, audio controls, and security settings
 */
@HiltViewModel
class CameraViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    private val plugins: List<CameraPlugin> = listOf(
        FeederCameraPlugin(deviceRepository),
        PhoneCameraPlugin()
    )

    private fun pluginFor(type: CameraPluginType): CameraPlugin {
        return plugins.first { it.descriptor.type == type }
    }
    
    // UI State
    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()
    
    // Camera stream session
    private val _streamSession = MutableStateFlow<CameraStreamSession?>(null)
    val streamSession: StateFlow<CameraStreamSession?> = _streamSession.asStateFlow()
    
    // Audio state
    private val _isMicEnabled = MutableStateFlow(false)
    val isMicEnabled: StateFlow<Boolean> = _isMicEnabled.asStateFlow()
    
    private val _isSpeakerEnabled = MutableStateFlow(false)
    val isSpeakerEnabled: StateFlow<Boolean> = _isSpeakerEnabled.asStateFlow()
    
    // Security mode
    private val _securityMode = MutableStateFlow(SecurityMode.OFF)
    val securityMode: StateFlow<SecurityMode> = _securityMode.asStateFlow()

    private val _selectedPlugin = MutableStateFlow(CameraPluginType.FEEDER)
    val selectedPlugin: StateFlow<CameraPluginType> = _selectedPlugin.asStateFlow()

    private val _availablePlugins = MutableStateFlow(plugins.map { it.descriptor })
    val availablePlugins: StateFlow<List<CameraPluginDescriptor>> = _availablePlugins.asStateFlow()

    fun selectPlugin(type: CameraPluginType, deviceId: String = "dev_001") {
        if (_selectedPlugin.value == type) return

        val current = _selectedPlugin.value
        _selectedPlugin.value = type

        viewModelScope.launch {
            pluginFor(current).stopStream(deviceId)
            _streamSession.value = null
            _uiState.update { it.copy(isStreaming = false) }
            startCameraStream(deviceId)
        }
    }
    
    /**
     * Start camera stream
     */
    fun startCameraStream(deviceId: String, quality: StreamQuality = StreamQuality.AUTO) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingStream = true, errorMessage = null) }

            val activePlugin = pluginFor(_selectedPlugin.value)
            val result = activePlugin.startStream(deviceId, quality)
            
            _uiState.update {
                when {
                    result.isSuccess -> {
                        _streamSession.value = result.getOrNull()
                        it.copy(
                            isLoadingStream = false,
                            isStreaming = true
                        )
                    }
                    result.isFailure -> it.copy(
                        isLoadingStream = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                    else -> it
                }
            }
        }
    }
    
    /**
     * Stop camera stream
     */
    fun stopCameraStream(deviceId: String) {
        viewModelScope.launch {
            val result = pluginFor(_selectedPlugin.value).stopStream(deviceId)
            
            if (result.isSuccess) {
                _streamSession.value = null
                _uiState.update { it.copy(isStreaming = false) }
            }
        }
    }
    
    /**
     * Capture snapshot from camera
     */
    fun captureSnapshot(deviceId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCapturing = true, errorMessage = null) }
            
            val result = deviceRepository.captureSnapshot(deviceId)
            
            _uiState.update {
                when {
                    result.isSuccess -> it.copy(
                        isCapturing = false,
                        lastSnapshot = result.getOrNull(),
                        showSnapshotSuccess = true
                    )
                    result.isFailure -> it.copy(
                        isCapturing = false,
                        errorMessage = result.exceptionOrNull()?.message
                    )
                    else -> it
                }
            }
        }
    }
    
    /**
     * Toggle microphone (listen to environment)
     */
    fun toggleMicrophone(deviceId: String) {
        val newState = !_isMicEnabled.value
        
        viewModelScope.launch {
            val result = deviceRepository.setMicrophoneEnabled(deviceId, newState)
            
            if (result.isSuccess) {
                _isMicEnabled.value = newState
            } else {
                _uiState.update {
                    it.copy(errorMessage = result.exceptionOrNull()?.message)
                }
            }
        }
    }
    
    /**
     * Toggle speaker (speak to pet)
     */
    fun toggleSpeaker(deviceId: String) {
        val newState = !_isSpeakerEnabled.value
        
        viewModelScope.launch {
            val result = deviceRepository.setSpeakerEnabled(deviceId, newState)
            
            if (result.isSuccess) {
                _isSpeakerEnabled.value = newState
            } else {
                _uiState.update {
                    it.copy(errorMessage = result.exceptionOrNull()?.message)
                }
            }
        }
    }
    
    /**
     * Set security mode
     */
    fun setSecurityMode(deviceId: String, mode: SecurityMode) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = deviceRepository.setSecurityMode(deviceId, mode)
            
            _uiState.update {
                when {
                    result.isSuccess -> {
                        _securityMode.value = mode
                        it.copy(
                            isLoading = false,
                            successMessage = "Mode sécurité: ${mode.name}"
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
     * Load security events
     */
    fun loadSecurityEvents(deviceId: String, fromTimestamp: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = deviceRepository.getSecurityEvents(deviceId, fromTimestamp)
            
            _uiState.update {
                when {
                    result.isSuccess -> it.copy(
                        isLoading = false,
                        securityEvents = result.getOrNull() ?: emptyList()
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
     * Clear success message
     */
    fun clearSuccessMessage() {
        _uiState.update { it.copy(showSnapshotSuccess = false, successMessage = null) }
    }
    
    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
    
    /**
     * Cleanup when leaving screen
     */
    override fun onCleared() {
        super.onCleared()
        // Stream will be stopped by screen's DisposableEffect
    }
}

/**
 * UI state for Camera screen
 */
data class CameraUiState(
    val isLoadingStream: Boolean = false,
    val isStreaming: Boolean = false,
    val isCapturing: Boolean = false,
    val isLoading: Boolean = false,
    val lastSnapshot: SnapshotResponse? = null,
    val showSnapshotSuccess: Boolean = false,
    val securityEvents: List<SecurityEvent> = emptyList(),
    val successMessage: String? = null,
    val errorMessage: String? = null
)
