package io.homeassistant.companion.android.util

import android.content.Context
import io.homeassistant.companion.android.common.R as commonR
import io.homeassistant.companion.android.common.data.servers.ServerManager
import io.homeassistant.companion.android.common.sensors.SensorManager
import io.homeassistant.companion.android.common.sensors.SensorRepository
import kotlinx.coroutines.CoroutineScope

/**
 * A [SensorManager] that only carries the metadata `@Preview` composables need. Everything a
 * preview never reaches throws, so a mistake surfaces in the render instead of silently
 * producing an empty screen.
 */
class PreviewSensorManager(
    override val name: Int = commonR.string.sensor_name_battery,
    private val sensors: List<SensorManager.BasicSensor> = emptyList(),
) : SensorManager {
    override val sensorWorkerScope: CoroutineScope get() = error("preview-only")
    override val applicationContext: Context get() = error("preview-only")
    override val sensorRepository: SensorRepository get() = error("preview-only")
    override val serverManager: ServerManager get() = error("preview-only")

    override fun requiredPermissions(sensorId: String): Array<String> = emptyArray()

    override suspend fun requestSensorUpdate() = error("preview-only")

    override suspend fun getAvailableSensors(): List<SensorManager.BasicSensor> = sensors
}
