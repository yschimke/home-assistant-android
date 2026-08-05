package io.homeassistant.companion.android.settings.server

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.homeassistant.companion.android.util.previewServer
import io.homeassistant.companion.android.util.previewServer2

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewServerChooser() {
    ServerChooserView(
        servers = listOf(previewServer, previewServer2),
        onServerSelected = {}
    )
}
