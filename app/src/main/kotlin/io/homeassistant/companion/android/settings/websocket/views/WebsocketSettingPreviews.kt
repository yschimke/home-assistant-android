package io.homeassistant.companion.android.settings.websocket.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.homeassistant.companion.android.database.settings.WebsocketSetting

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewWebsocketSetting() {
    WebsocketSettingView(
        websocketSetting = WebsocketSetting.ALWAYS,
        unrestrictedBackgroundAccess = false,
        hasWifi = true,
        onSettingChanged = {},
        onBackgroundAccessTapped = {},
    )
}
