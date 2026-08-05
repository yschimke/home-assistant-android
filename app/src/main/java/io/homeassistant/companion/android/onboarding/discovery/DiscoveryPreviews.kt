package io.homeassistant.companion.android.onboarding.discovery

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.homeassistant.companion.android.common.data.HomeAssistantVersion
import java.net.URL

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewDiscovery() {
    DiscoveryContent(
        foundInstances = listOf(
            HomeAssistantInstance("Home", URL("https://home.example.com"), HomeAssistantVersion(2026, 8, 0)),
            HomeAssistantInstance("Cabin", URL("https://cabin.example.com"), HomeAssistantVersion(2026, 8, 0))
        ),
        manualSetupClicked = {},
        instanceClicked = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewDiscoverySearching() {
    DiscoveryContent(
        foundInstances = emptyList(),
        manualSetupClicked = {},
        instanceClicked = {}
    )
}
