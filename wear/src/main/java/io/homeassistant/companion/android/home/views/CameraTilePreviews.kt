package io.homeassistant.companion.android.home.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.homeassistant.companion.android.database.wear.CameraTile
import io.homeassistant.companion.android.util.previewEntity1

@Preview(device = "id:wearos_large_round", showSystemUi = true)
@Composable
private fun PreviewSetCameraTile() {
    SetCameraTileView(
        tile = CameraTile(
            id = 1,
            entityId = previewEntity1.entityId,
            refreshInterval = 300
        ),
        entities = listOf(previewEntity1),
        onSelectEntity = {},
        onSelectRefreshInterval = {}
    )
}

@Preview(device = "id:wearos_large_round", showSystemUi = true)
@Composable
private fun PreviewSetCameraTileEmpty() {
    SetCameraTileView(
        tile = null,
        entities = emptyList(),
        onSelectEntity = {},
        onSelectRefreshInterval = {}
    )
}
