package com.dobbleshop.neovision.data.api

import com.dobbleshop.neovision.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Cloud API service for remote device control via HTTPS
 * Base URL: https://api.dobbleshop.com/v1/
 */
interface DeviceApiService {
    
    // ========== Authentication ==========
    
    @POST("auth/login")
    suspend fun login(
        @Body credentials: LoginRequest
    ): Response<AuthResponse>
    
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<AuthResponse>
    
    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
    
    // ========== Device Management ==========
    
    @GET("devices")
    suspend fun getDevices(): Response<List<Device>>
    
    @GET("devices/{deviceId}")
    suspend fun getDevice(
        @Path("deviceId") deviceId: String
    ): Response<Device>
    
    @GET("devices/{deviceId}/status")
    suspend fun getDeviceStatus(
        @Path("deviceId") deviceId: String
    ): Response<DeviceStatus>
    
    @POST("devices/{deviceId}/ping")
    suspend fun pingDevice(
        @Path("deviceId") deviceId: String
    ): Response<PingResponse>
    
    // ========== Food & Water Control ==========
    
    @POST("devices/{deviceId}/dispense/food")
    suspend fun dispenseFood(
        @Path("deviceId") deviceId: String,
        @Body request: DispenseFoodRequest
    ): Response<DispenseEvent>
    
    @POST("devices/{deviceId}/dispense/water")
    suspend fun dispenseWater(
        @Path("deviceId") deviceId: String,
        @Body request: DispenseWaterRequest
    ): Response<DispenseEvent>
    
    @GET("devices/{deviceId}/dispense/history")
    suspend fun getDispenseHistory(
        @Path("deviceId") deviceId: String,
        @Query("from") fromTimestamp: Long,
        @Query("to") toTimestamp: Long,
        @Query("limit") limit: Int = 100
    ): Response<List<DispenseEvent>>
    
    // ========== Schedules ==========
    
    @GET("devices/{deviceId}/schedules/feeding")
    suspend fun getFeedingSchedules(
        @Path("deviceId") deviceId: String
    ): Response<List<FeedingSchedule>>
    
    @POST("devices/{deviceId}/schedules/feeding")
    suspend fun createFeedingSchedule(
        @Path("deviceId") deviceId: String,
        @Body schedule: FeedingSchedule
    ): Response<FeedingSchedule>
    
    @PUT("devices/{deviceId}/schedules/feeding/{scheduleId}")
    suspend fun updateFeedingSchedule(
        @Path("deviceId") deviceId: String,
        @Path("scheduleId") scheduleId: String,
        @Body schedule: FeedingSchedule
    ): Response<FeedingSchedule>
    
    @DELETE("devices/{deviceId}/schedules/feeding/{scheduleId}")
    suspend fun deleteFeedingSchedule(
        @Path("deviceId") deviceId: String,
        @Path("scheduleId") scheduleId: String
    ): Response<Unit>
    
    @GET("devices/{deviceId}/schedules/water")
    suspend fun getWaterSchedules(
        @Path("deviceId") deviceId: String
    ): Response<List<WaterSchedule>>
    
    @POST("devices/{deviceId}/schedules/water")
    suspend fun createWaterSchedule(
        @Path("deviceId") deviceId: String,
        @Body schedule: WaterSchedule
    ): Response<WaterSchedule>
    
    // ========== Camera & Media ==========
    
    @POST("devices/{deviceId}/camera/stream/start")
    suspend fun startCameraStream(
        @Path("deviceId") deviceId: String,
        @Body request: StreamRequest
    ): Response<CameraStreamSession>
    
    @POST("devices/{deviceId}/camera/stream/stop")
    suspend fun stopCameraStream(
        @Path("deviceId") deviceId: String
    ): Response<Unit>
    
    @POST("devices/{deviceId}/camera/snapshot")
    suspend fun captureSnapshot(
        @Path("deviceId") deviceId: String
    ): Response<SnapshotResponse>
    
    @GET("devices/{deviceId}/snapshots")
    suspend fun getSnapshots(
        @Path("deviceId") deviceId: String,
        @Query("limit") limit: Int = 50
    ): Response<List<SnapshotResponse>>
    
    // ========== Audio Control ==========
    
    @POST("devices/{deviceId}/audio/speaker")
    suspend fun setSpeakerState(
        @Path("deviceId") deviceId: String,
        @Body request: AudioStateRequest
    ): Response<Unit>
    
    @POST("devices/{deviceId}/audio/microphone")
    suspend fun setMicrophoneState(
        @Path("deviceId") deviceId: String,
        @Body request: AudioStateRequest
    ): Response<Unit>
    
    @GET("devices/{deviceId}/audio/webrtc/offer")
    suspend fun getWebRTCOffer(
        @Path("deviceId") deviceId: String
    ): Response<WebRTCOffer>
    
    @POST("devices/{deviceId}/audio/webrtc/answer")
    suspend fun sendWebRTCAnswer(
        @Path("deviceId") deviceId: String,
        @Body answer: WebRTCAnswer
    ): Response<Unit>
    
    // ========== Security & Presence ==========
    
    @POST("devices/{deviceId}/security/mode")
    suspend fun setSecurityMode(
        @Path("deviceId") deviceId: String,
        @Body request: SecurityModeRequest
    ): Response<Unit>
    
    @GET("devices/{deviceId}/security/events")
    suspend fun getSecurityEvents(
        @Path("deviceId") deviceId: String,
        @Query("from") fromTimestamp: Long,
        @Query("limit") limit: Int = 50
    ): Response<List<SecurityEvent>>
    
    // ========== Firmware & Updates ==========
    
    @GET("devices/{deviceId}/firmware/check")
    suspend fun checkFirmwareUpdate(
        @Path("deviceId") deviceId: String
    ): Response<FirmwareInfo>
    
    @POST("devices/{deviceId}/firmware/update")
    suspend fun startFirmwareUpdate(
        @Path("deviceId") deviceId: String,
        @Body request: FirmwareUpdateRequest
    ): Response<FirmwareUpdateStatus>
    
    @GET("devices/{deviceId}/firmware/status")
    suspend fun getFirmwareUpdateStatus(
        @Path("deviceId") deviceId: String
    ): Response<FirmwareUpdateStatus>
}

// ========== Request/Response Models ==========

data class LoginRequest(
    val email: String,
    val password: String,
    val deviceId: String? = null
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val user: User
)

data class RefreshTokenRequest(
    val refreshToken: String
)

data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val timezone: String,
    val locale: String
)

data class PingResponse(
    val deviceId: String,
    val timestamp: Long,
    val latencyMs: Long
)

data class DispenseFoodRequest(
    val petId: String,
    val grams: Int,
    val confirmationRequired: Boolean = true
)

data class DispenseWaterRequest(
    val ml: Int
)

data class DispenseEvent(
    val id: String,
    val deviceId: String,
    val type: DispenseType,
    val petId: String?,
    val amount: Int, // grams for food, ml for water
    val requestedAt: Long,
    val completedAt: Long?,
    val status: DispenseStatus
)

enum class DispenseType {
    FOOD,
    WATER
}

enum class DispenseStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    CANCELLED
}

data class StreamRequest(
    val quality: StreamQuality = StreamQuality.AUTO,
    val protocol: StreamProtocol = StreamProtocol.HLS
)

enum class StreamQuality {
    AUTO,
    LOW,      // 480p
    MEDIUM,   // 720p
    HIGH      // 1080p
}

enum class StreamProtocol {
    HLS,      // HTTP Live Streaming (default)
    WEBRTC    // WebRTC (lower latency)
}

data class CameraStreamSession(
    val sessionId: String,
    val streamUrl: String,
    val protocol: StreamProtocol,
    val expiresAt: Long
)

data class SnapshotResponse(
    val id: String,
    val deviceId: String,
    val imageUrl: String,
    val thumbnailUrl: String,
    val capturedAt: Long,
    val width: Int,
    val height: Int
)

data class AudioStateRequest(
    val enabled: Boolean,
    val volume: Int? = null // 0-100
)

data class WebRTCOffer(
    val sdp: String,
    val type: String = "offer"
)

data class WebRTCAnswer(
    val sdp: String,
    val type: String = "answer"
)

data class SecurityModeRequest(
    val mode: SecurityMode
)

enum class SecurityMode {
    OFF,
    HOME,
    AWAY,
    AUTO
}

data class SecurityEvent(
    val id: String,
    val deviceId: String,
    val type: SecurityEventType,
    val detectedAt: Long,
    val intensity: Float?,
    val snapshotUrl: String?
)

enum class SecurityEventType {
    PRESENCE_DETECTED,
    PRESENCE_CLEARED,
    MOTION_DETECTED,
    UNUSUAL_ACTIVITY
}

data class FirmwareInfo(
    val currentVersion: String,
    val latestVersion: String,
    val updateAvailable: Boolean,
    val releaseNotes: String?,
    val downloadUrl: String?
)

data class FirmwareUpdateRequest(
    val version: String,
    val scheduleAt: Long? = null // null = update now
)

data class FirmwareUpdateStatus(
    val status: UpdateStatus,
    val progress: Int, // 0-100
    val currentVersion: String,
    val targetVersion: String?,
    val startedAt: Long?,
    val estimatedCompletion: Long?
)

enum class UpdateStatus {
    IDLE,
    DOWNLOADING,
    VERIFYING,
    INSTALLING,
    REBOOTING,
    COMPLETED,
    FAILED
}
