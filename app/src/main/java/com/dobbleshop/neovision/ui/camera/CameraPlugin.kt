package com.dobbleshop.neovision.ui.camera

import com.dobbleshop.neovision.data.api.CameraStreamSession
import com.dobbleshop.neovision.data.api.StreamProtocol
import com.dobbleshop.neovision.data.api.StreamQuality
import com.dobbleshop.neovision.data.repository.DeviceRepository

enum class CameraPluginType {
    FEEDER,
    PHONE
}

data class CameraPluginDescriptor(
    val type: CameraPluginType,
    val label: String,
    val subtitle: String
)

interface CameraPlugin {
    val descriptor: CameraPluginDescriptor

    suspend fun startStream(deviceId: String, quality: StreamQuality): Result<CameraStreamSession>

    suspend fun stopStream(deviceId: String): Result<Unit>
}

class FeederCameraPlugin(
    private val repository: DeviceRepository
) : CameraPlugin {
    override val descriptor: CameraPluginDescriptor = CameraPluginDescriptor(
        type = CameraPluginType.FEEDER,
        label = "Caméra feeder",
        subtitle = "Flux réseau du distributeur"
    )

    override suspend fun startStream(deviceId: String, quality: StreamQuality): Result<CameraStreamSession> {
        return repository.startCameraStream(deviceId, quality)
    }

    override suspend fun stopStream(deviceId: String): Result<Unit> {
        return repository.stopCameraStream(deviceId)
    }
}

class PhoneCameraPlugin : CameraPlugin {
    override val descriptor: CameraPluginDescriptor = CameraPluginDescriptor(
        type = CameraPluginType.PHONE,
        label = "Caméra téléphone",
        subtitle = "Source locale de ce smartphone"
    )

    override suspend fun startStream(deviceId: String, quality: StreamQuality): Result<CameraStreamSession> {
        return Result.success(
            CameraStreamSession(
                sessionId = "phone-local",
                streamUrl = "phone://local-camera",
                protocol = StreamProtocol.WEBRTC,
                expiresAt = System.currentTimeMillis() + 3600_000
            )
        )
    }

    override suspend fun stopStream(deviceId: String): Result<Unit> {
        return Result.success(Unit)
    }
}
