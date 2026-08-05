package io.homeassistant.companion.android.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.homeassistant.companion.android.theme.WearAppTheme

@Preview(device = "id:wearos_large_round", showSystemUi = true)
@Composable
private fun PreviewToggleControls() {
    WearAppTheme {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ToggleSwitch(isChecked = true)
                ToggleSwitch(isChecked = false)
                ToggleCheckbox(isChecked = true)
                ToggleCheckbox(isChecked = false)
            }
        }
    }
}
