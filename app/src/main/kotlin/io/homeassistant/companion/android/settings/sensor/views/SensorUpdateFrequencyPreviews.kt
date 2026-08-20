package io.homeassistant.companion.android.settings.sensor.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.homeassistant.companion.android.database.settings.SensorUpdateFrequencySetting

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewSensorUpdateFrequency() {
    SensorUpdateFrequencyView(
        sensorUpdateFrequency = SensorUpdateFrequencySetting.FAST_WHILE_CHARGING,
        onSettingChanged = {},
    )
}
