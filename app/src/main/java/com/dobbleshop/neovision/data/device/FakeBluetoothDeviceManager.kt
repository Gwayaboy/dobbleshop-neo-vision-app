package com.dobbleshop.neovision.data.device

import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.os.ParcelUuid
import com.dobbleshop.neovision.data.model.DeviceVariant
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Fake/Mock implementation of Bluetooth device manager for MVP
 * This provides the interface and structure without requiring actual BLE hardware
 * 
 * In production, this would implement actual BLE communication using
 * Android BluetoothManager and BluetoothGatt APIs
 */
@Singleton
class FakeBluetoothDeviceManager @Inject constructor(
    @ApplicationContext private val context: Context
) : BluetoothDeviceManager {
    
    companion object {
        // Service UUIDs for DOBBLESHOP NEO VISION
        val SERVICE_UUID: UUID = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb")
        val WIFI_CHAR_UUID: UUID = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb")
        val TOKEN_CHAR_UUID: UUID = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb")
    }
    
    private var isScanning = false
    private val discoveredDevices = mutableListOf<DiscoveredDevice>()
    
    override fun startScanning(): Flow<List<DiscoveredDevice>> = callbackFlow {
        isScanning = true
        
        // Simulate discovering mock devices
        val mockDevices = listOf(
            DiscoveredDevice(
                address = "AA:BB:CC:DD:EE:FF",
                name = "DOBBLESHOP_CAT_001",
                deviceId = "dev_001",
                productVariant = DeviceVariant.CAT,
                hwRevision = "1.0",
                fwVersion = "1.4.2",
                rssi = -65
            ),
            DiscoveredDevice(
                address = "11:22:33:44:55:66",
                name = "DOBBLESHOP_DOG_002",
                deviceId = "dev_002",
                productVariant = DeviceVariant.DOG,
                hwRevision = "1.0",
                fwVersion = "1.4.2",
                rssi = -58
            )
        )
        
        discoveredDevices.addAll(mockDevices)
        trySend(mockDevices)
        
        awaitClose { 
            isScanning = false
        }
    }
    
    override fun stopScanning() {
        isScanning = false
    }
    
    override suspend fun pairDevice(device: DiscoveredDevice): Result<PairingSession> {
        // Simulate BLE pairing with challenge/response
        return suspendCancellableCoroutine { continuation ->
            // Fake pairing delay
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                val session = PairingSession(
                    deviceId = device.deviceId,
                    sessionToken = "session_${System.currentTimeMillis()}",
                    expiresAt = System.currentTimeMillis() + 300_000 // 5 minutes
                )
                continuation.resume(Result.success(session))
            }, 1000)
        }
    }
    
    override suspend fun provisionWifi(
        session: PairingSession,
        ssid: String,
        password: String
    ): Result<Unit> {
        // Simulate sending WiFi credentials via BLE
        return suspendCancellableCoroutine { continuation ->
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                // In real implementation, would write to WIFI_CHAR_UUID
                continuation.resume(Result.success(Unit))
            }, 2000)
        }
    }
    
    override suspend fun completeOnboarding(
        session: PairingSession,
        timezone: String,
        locale: String,
        ownerToken: String
    ): Result<com.dobbleshop.neovision.data.model.Device> {
        // Simulate completing onboarding and receiving device registration
        return suspendCancellableCoroutine { continuation ->
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                val device = com.dobbleshop.neovision.data.model.Device(
                    id = session.deviceId,
                    serialNumber = "SN${System.currentTimeMillis()}",
                    model = com.dobbleshop.neovision.data.model.DeviceModel.NEO_VISION,
                    variant = com.dobbleshop.neovision.data.model.DeviceVariant.CAT,
                    hardwareRevision = "1.0",
                    esp32FirmwareVersion = "1.4.2",
                    rpiFirmwareVersion = "1.2.0",
                    connectivityStatus = com.dobbleshop.neovision.data.model.ConnectivityStatus.WIFI_CONNECTED,
                    batteryStatus = com.dobbleshop.neovision.data.model.BatteryStatus(
                        percentage = 85,
                        isCharging = false,
                        voltage = 3.7f
                    ),
                    lastSeenAt = System.currentTimeMillis(),
                    timezone = timezone,
                    isOnline = true
                )
                continuation.resume(Result.success(device))
            }, 1500)
        }
    }
    
    override suspend fun disconnect() {
        // Simulate disconnection
        isScanning = false
    }
}
