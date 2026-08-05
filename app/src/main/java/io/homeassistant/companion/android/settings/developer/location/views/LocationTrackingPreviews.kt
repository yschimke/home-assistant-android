package io.homeassistant.companion.android.settings.developer.location.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import io.homeassistant.companion.android.database.location.LocationHistoryItem
import io.homeassistant.companion.android.util.previewServer
import kotlinx.coroutines.flow.flowOf

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewLocationTrackingDisabled() {
    LocationTrackingView(
        useHistory = false,
        onSetHistory = {},
        history = flowOf(PagingData.empty<LocationHistoryItem>()),
        serversList = listOf(previewServer)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewLocationReadOnlyRows() {
    androidx.compose.foundation.layout.Column {
        ReadOnlyRow("Server", "Home")
        ReadOnlyRow("Result", "Location sent")
    }
}
