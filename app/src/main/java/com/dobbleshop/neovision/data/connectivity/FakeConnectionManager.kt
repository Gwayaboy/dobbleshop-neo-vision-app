package com.dobbleshop.neovision.data.connectivity

import com.dobbleshop.neovision.data.model.Device
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Temporary stub implementation of ConnectionManager
 * Reports as cloud-connected for development
 */
@Singleton
class FakeConnectionManager @Inject constructor() : ConnectionManager {
    
    private val _connectionState = MutableStateFlow<ConnectionState>(
        ConnectionState.ConnectedCloud(
            deviceId = "dev_001",
            latencyMs = 50L,
            serverRegion = "us-east"
        )
    )
    
    private val _preferredMode = MutableStateFlow(ConnectionMode.CLOUD)
    
    override val connectionState: Flow<ConnectionState> = _connectionState
    
    override val preferredMode: Flow<ConnectionMode> = _preferredMode
    
    override suspend fun connect(
        device: Device,
        preferBluetooth: Boolean,
        forceMode: ConnectionMode?
    ): Result<ConnectionState> {
        val state = ConnectionState.ConnectedCloud(
            deviceId = device.id,
            latencyMs = 50L,
            serverRegion = "us-east"
        )
        _connectionState.value = state
        return Result.success(state)
    }
    
    override suspend fun disconnect(): Result<Unit> {
        _connectionState.value = ConnectionState.Disconnected
        return Result.success(Unit)
    }
    
    override suspend fun switchToCloud(): Result<Unit> {
        _connectionState.value = ConnectionState.ConnectedCloud(
            deviceId = "dev_001",
            latencyMs = 50L,
            serverRegion = "us-east"
        )
        return Result.success(Unit)
    }
    
    override suspend fun switchToBluetooth(): Result<Unit> {
        return Result.failure(Exception("Bluetooth not implemented yet"))
    }
    
    override suspend fun pingDevice(): Result<Long> {
        return Result.success(50L) // Fake 50ms latency
    }
    
    override suspend fun isCloudReachable(deviceId: String): Boolean {
        return true
    }
    
    override suspend fun isBluetoothReachable(deviceId: String): Boolean {
        return false
    }

    override suspend fun getRecommendedMode(deviceId: String): ConnectionMode {
        return ConnectionMode.CLOUD
    }
}
