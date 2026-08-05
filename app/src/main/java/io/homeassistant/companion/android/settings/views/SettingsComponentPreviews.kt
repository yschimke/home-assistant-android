package io.homeassistant.companion.android.settings.views

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewSettingsRows() {
    Column {
        SettingsRow(
            primaryText = "Location",
            secondaryText = "Manage background location access",
            mdiIcon = CommunityMaterial.Icon.cmd_cog,
            enabled = true,
            onClicked = {}
        )
        SettingsRow(
            primaryText = "Unavailable setting",
            secondaryText = "This feature is disabled",
            mdiIcon = null,
            enabled = false,
            onClicked = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewEmptyState() {
    EmptyState(
        icon = CommunityMaterial.Icon.cmd_delete,
        title = "Nothing here yet",
        subtitle = "Items will appear here after they are added."
    )
}
