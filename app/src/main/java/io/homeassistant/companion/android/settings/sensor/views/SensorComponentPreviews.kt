package io.homeassistant.companion.android.settings.sensor.views

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.homeassistant.companion.android.common.sensors.BatterySensorManager
import io.homeassistant.companion.android.database.sensor.Sensor

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewSensorRows() {
    Column {
        SensorRow(
            basicSensor = BatterySensorManager.isChargingState,
            dbSensor = Sensor(
                id = BatterySensorManager.isChargingState.id,
                serverId = 1,
                enabled = true,
                state = "Charging",
                icon = "mdi:power-plug"
            ),
            onSensorClicked = {}
        )
        SensorRow(
            basicSensor = BatterySensorManager.isChargingState,
            dbSensor = null,
            onSensorClicked = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewSensorDetailRows() {
    Column {
        SensorDetailHeader("Settings")
        SensorDetailRow(
            title = "Update frequency",
            summary = "Every minute",
            onClick = {}
        )
        SensorDetailRow(
            title = "Enabled",
            switch = true,
            onClick = {}
        )
        SensorDetailSettingRow(
            label = "Include location history",
            checked = true,
            multiple = false,
            onClick = {}
        )
    }
}
