package io.homeassistant.companion.android.settings.controls.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.homeassistant.companion.android.common.data.integration.ControlsAuthRequiredSetting
import io.homeassistant.companion.android.util.previewEntity1
import io.homeassistant.companion.android.util.previewEntity2
import io.homeassistant.companion.android.util.previewServer

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewManageControls() {
    ManageControlsView(
        panelEnabled = false,
        authSetting = ControlsAuthRequiredSetting.SELECTION,
        authRequiredList = listOf("1.${previewEntity2.entityId}"),
        entitiesLoaded = true,
        entitiesList = mapOf(previewServer.id to listOf(previewEntity1, previewEntity2)),
        panelSetting = null,
        serversList = listOf(previewServer),
        defaultServer = previewServer.id,
        onSetPanelEnabled = {},
        onSelectAll = {},
        onSelectNone = {},
        onSelectEntity = { _, _ -> },
        onSetPanelSetting = { _, _ -> }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewManageControlsComponents() {
    ManageControlsEntity(
        entityName = "Living room light",
        entityDomain = "light",
        selected = true,
        onClick = {}
    )
}
