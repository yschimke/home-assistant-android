package io.homeassistant.companion.android.util.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import io.homeassistant.companion.android.util.previewEntity1
import io.homeassistant.companion.android.util.previewEntity2
import io.homeassistant.companion.android.common.R as commonR

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewFormComponents() {
    Column(Modifier.padding(16.dp)) {
        ExposedDropdownMenu(
            label = "Server",
            keys = listOf("Home", "Cabin"),
            currentIndex = 0,
            onSelected = {}
        )
        SingleEntityPicker(
            entities = listOf(previewEntity1, previewEntity2),
            currentEntity = previewEntity1.entityId,
            onEntityCleared = {},
            onEntitySelected = { true }
        )
        RadioButtonRow(text = "Selected option", selected = true, onClick = {})
        RadioButtonRow(text = "Other option", selected = false, onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewEntityComponents() {
    Column(Modifier.padding(vertical = 8.dp)) {
        FavoriteEntityRow(
            entityName = "Living room light",
            entityId = "light.living_room",
            checked = true,
            draggable = true,
            onClick = {}
        )
        TransparentChip(
            text = "Living room",
            icon = CommunityMaterial.Icon.cmd_cellphone,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewAlerts() {
    Column(Modifier.padding(16.dp)) {
        HaAlertWarning(
            message = "Background access is required to keep this connection active.",
            action = "Allow",
            onActionClicked = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, apiLevel = 35)
@Composable
private fun PreviewNotificationInfo() {
    Column(Modifier.padding(16.dp)) {
        InfoNotification(
            infoString = commonR.string.websocket_persistent_notification,
            channelId = "websocket",
            buttonString = commonR.string.websocket_notification_channel
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewModalBottomSheet() {
    ModalBottomSheet(title = "Choose an option") {
        Text("Bottom sheet content", Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewMdcAlertDialog() {
    MdcAlertDialog(
        onDismissRequest = {},
        title = { Text("Confirm change") },
        content = { Text("This is the dialog content.") },
        onCancel = {},
        onSave = {}
    )
}
