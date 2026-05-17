package com.dobbleshop.neovision.data.device

import com.dobbleshop.neovision.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fake/Mock implementation of device controller for MVP
 * Simulates device communication without actual hardware
 */
@Singleton
class FakeDeviceController @Inject constructor() : DeviceController {
    
    private val mockDeviceState = mutableMapOf<String, DeviceStatus>()
    
    override fun observeDeviceStatus(deviceId: String): Flow<DeviceStatus> = flow {
        while (true) {
            val status = mockDeviceState.getOrPut(deviceId) {
                DeviceStatus(
                    deviceId = deviceId,
                    bowlWeightGrams = 0f,
                    foodReservoirPercent = 62,
                    foodReservoirGrams = 1240,
                    foodReservoirDaysRemaining = 12,
                    waterReservoirPercent = 78,
                    waterReservoirMl = 1560,
                    waterReservoirDaysRemaining = 15,
                    bowlStatus = BowlStatus.EMPTY,
                    deviceTime = System.currentTimeMillis(),
                    presenceDetected = false,
                    presenceIntensity = null,
                    cameraStatus = CameraStatus.OK,
                    batteryPercent = 74,
                    isPlugged = false,
                    uptime = 86400000, // 1 day
                    wifiRssi = -58,
                    internalTemperature = 42.5f
                )
            }
            emit(status)
            delay(2000) // Update every 2 seconds
        }
    }
    
    override suspend fun dispenseFood(
        deviceId: String,
        petId: String,
        grams: Int,
        confirmationRequired: Boolean
    ): Result<DispenseEvent> {
        delay(3000) // Simulate dispense time
        
        val event = DispenseEvent(
            deviceId = deviceId,
            petId = petId,
            type = DispenseType.FOOD,
            requestedAmount = grams,
            actualAmount = grams,
            result = DispenseResult.SUCCESS,
            reason = DispenseReason.MANUAL,
            bowlWeightBefore = 0f,
            bowlWeightAfter = grams.toFloat()
        )
        
        // Update mock state
        mockDeviceState[deviceId] = mockDeviceState[deviceId]?.copy(
            bowlWeightGrams = grams.toFloat(),
            bowlStatus = BowlStatus.NORMAL,
            foodReservoirGrams = (mockDeviceState[deviceId]?.foodReservoirGrams ?: 1240) - grams
        ) ?: return Result.failure(Exception("Device not found"))
        
        return Result.success(event)
    }
    
    override suspend fun dispenseWater(deviceId: String, ml: Int): Result<DispenseEvent> {
        delay(2000)
        
        val event = DispenseEvent(
            deviceId = deviceId,
            petId = null,
            type = DispenseType.WATER,
            requestedAmount = ml,
            actualAmount = ml,
            result = DispenseResult.SUCCESS,
            reason = DispenseReason.MANUAL,
            bowlWeightBefore = 0f,
            bowlWeightAfter = ml.toFloat()
        )
        
        mockDeviceState[deviceId] = mockDeviceState[deviceId]?.copy(
            waterReservoirMl = (mockDeviceState[deviceId]?.waterReservoirMl ?: 1560) - ml
        ) ?: return Result.failure(Exception("Device not found"))
        
        return Result.success(event)
    }
    
    override suspend fun updateFeedingSchedule(
        deviceId: String,
        schedule: FeedingSchedule
    ): Result<Unit> {
        delay(500)
        return Result.success(Unit)
    }
    
    override suspend fun deleteFeedingSchedule(
        deviceId: String,
        scheduleId: String
    ): Result<Unit> {
        delay(300)
        return Result.success(Unit)
    }
    
    override suspend fun updateWaterSchedule(
        deviceId: String,
        schedule: WaterSchedule
    ): Result<Unit> {
        delay(500)
        return Result.success(Unit)
    }
    
    override suspend fun startCameraStream(deviceId: String): Result<CameraStreamSession> {
        delay(1000)
        return Result.success(
            CameraStreamSession(
                streamUrl = "https://mock-stream.example.com/live.m3u8",
                protocol = "HLS",
                expiresAt = System.currentTimeMillis() + 3600000,
                sessionId = "session_${System.currentTimeMillis()}"
            )
        )
    }
    
    override suspend fun stopCameraStream(deviceId: String): Result<Unit> {
        return Result.success(Unit)
    }
    
    override suspend fun captureSnapshot(deviceId: String): Result<String> {
        delay(500)
        return Result.success("https://mock-image.example.com/snapshot_${System.currentTimeMillis()}.jpg")
    }
    
    override suspend fun enableSpeaker(deviceId: String, enable: Boolean): Result<Unit> {
        return Result.success(Unit)
    }
    
    override suspend fun enableMicrophone(deviceId: String, enable: Boolean): Result<Unit> {
        return Result.success(Unit)
    }
    
    override suspend fun setSecurityMode(deviceId: String, mode: SecurityMode): Result<Unit> {
        delay(300)
        return Result.success(Unit)
    }
    
    override suspend fun startFirmwareUpdate(
        deviceId: String,
        targetVersion: String
    ): Result<FirmwareUpdateProgress> {
        return Result.success(
            FirmwareUpdateProgress(
                deviceId = deviceId,
                currentVersion = "1.4.2",
                targetVersion = targetVersion,
                status = FirmwareUpdateStatus.DOWNLOADING,
                progressPercent = 0,
                message = "Starting download..."
            )
        )
    }
    
    override fun observeFirmwareUpdateProgress(deviceId: String): Flow<FirmwareUpdateProgress> = flow {
        val stages = listOf(
            FirmwareUpdateProgress(deviceId, "1.4.2", "1.5.0", FirmwareUpdateStatus.DOWNLOADING, 25, "Downloading..."),
            FirmwareUpdateProgress(deviceId, "1.4.2", "1.5.0", FirmwareUpdateStatus.DOWNLOADING, 75, "Almost done..."),
            FirmwareUpdateProgress(deviceId, "1.4.2", "1.5.0", FirmwareUpdateStatus.INSTALLING, 30, "Installing..."),
            FirmwareUpdateProgress(deviceId, "1.4.2", "1.5.0", FirmwareUpdateStatus.VERIFYING, 90, "Verifying..."),
            FirmwareUpdateProgress(deviceId, "1.4.2", "1.5.0", FirmwareUpdateStatus.COMPLETED, 100, "Complete!")
        )
        
        for (stage in stages) {
            emit(stage)
            delay(2000)
        }
    }
    
    override suspend fun runDiagnostics(deviceId: String): Result<DiagnosticsReport> {
        delay(3000)
        
        val report = DiagnosticsReport(
            deviceId = deviceId,
            timestamp = System.currentTimeMillis(),
            esp32Status = ComponentStatus("ESP32-S3", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            rpiStatus = ComponentStatus("Raspberry Pi Zero 2W", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            cameraStatus = ComponentStatus("Camera Module 3", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            foodMotorStatus = ComponentStatus("Food Servo Motor", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            waterPumpStatus = ComponentStatus("Water Pump", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            loadCellStatus = ComponentStatus("HX711 Load Cell", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            foodLevelSensorStatus = ComponentStatus("VL53L0X Food Sensor", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            waterLevelSensorStatus = ComponentStatus("Water Level Sensor", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            presenceSensorStatus = ComponentStatus("mmWave Presence", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            microphoneStatus = ComponentStatus("INMP441 Microphone", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            speakerStatus = ComponentStatus("MAX98357A Speaker", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            rtcStatus = ComponentStatus("DS3231 RTC", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            wifiStatus = ComponentStatus("WiFi Connection", HealthStatus.OK, lastTestAt = System.currentTimeMillis()),
            bluetoothStatus = ComponentStatus("Bluetooth LE", HealthStatus.OK, lastTestAt = System.currentTimeMillis())
        )
        
        return Result.success(report)
    }
    
    override suspend fun syncFromCloud(deviceId: String): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }
    
    override suspend fun syncToDevice(deviceId: String): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }
}
