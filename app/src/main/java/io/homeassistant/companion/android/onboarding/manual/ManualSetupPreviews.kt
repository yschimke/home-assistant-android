package io.homeassistant.companion.android.onboarding.manual

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewManualSetup() {
    ManualSetupContent(
        manualUrl = "https://home.example.com",
        continueEnabled = true,
        onUrlUpdated = {},
        connectedClicked = {}
    )
}
