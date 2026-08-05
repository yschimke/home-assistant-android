package io.homeassistant.companion.android.onboarding.integration

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewMobileAppIntegration() {
    MobileAppIntegrationContent(
        deviceIsWatch = false,
        deviceName = "Pixel 10",
        locationTrackingPossible = true,
        locationTrackingEnabled = true,
        onDeviceNameUpdated = {},
        openPrivacyPolicy = {},
        onLocationTrackingChanged = {},
        onFinishClicked = {}
    )
}
