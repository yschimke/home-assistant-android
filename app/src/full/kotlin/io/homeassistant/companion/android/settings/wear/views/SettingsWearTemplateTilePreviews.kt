package io.homeassistant.companion.android.settings.wear.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewSettingsWearTemplateTile() {
    SettingsWearTemplateTile(
        template = "The temperature is {{ states('sensor.temperature') }} °C",
        renderedTemplate = "The temperature is <b>21.5 °C</b>",
        refreshInterval = 300,
        onContentChanged = {},
        onRefreshIntervalChanged = {},
        onBackClicked = {},
    )
}
