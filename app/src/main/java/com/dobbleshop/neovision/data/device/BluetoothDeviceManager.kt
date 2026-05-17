package com.dobbleshop.neovision.data.device

import com.dobbleshop.neovision.data.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Bluetooth Low Energy communication interface for device onboarding
 * Corresponds to specification section 10
 */
interface BluetoothDeviceManager {
    
    /**
     * Start scanning for nearby devices in provisioning mode
     * Returns flow of discovered devices
     */
    fun startScanning(): Flow<List<DiscoveredDevice>>
    
    /**
     * Stop scanning for devices
     */
    fun stopScanning()
    
    /**
     * Connect to a discovered device for pairing
     * Corresponds to specification section 10.2 step 3
     */
    suspend fun pairDevice(device: DiscoveredDevice): Result<PairingSession>
    
    /**
     * Send WiFi credentials to device during onboarding
     * Corresponds to specification section 10.2 steps 4-5
     */
    suspend fun provisionWifi(
        session: PairingSession,
        ssid: String,
        password: String
    ): Result<Unit>
    
    /**
     * Complete device registration and receive device token
     * Corresponds to specification section 10.2 step 6
     */
    suspend fun completeOnboarding(
        session: PairingSession,
        timezone: String,
        locale: String,
        ownerToken: String
    ): Result<Device>
    
    /**
     * Disconnect from BLE device
     */
    suspend fun disconnect()
}

/**
 * Discovered BLE device during scanning
 */
data class DiscoveredDevice(
    val address: String,
    val name: String?,
    val deviceId: String,
    val productVariant: DeviceVariant,
    val hwRevision: String,
    val fwVersion: String,
    val rssi: Int
)

/**
 * Active pairing session
 */
data class PairingSession(
    val deviceId: String,
    val sessionToken: String,
    val expiresAt: Long
)
