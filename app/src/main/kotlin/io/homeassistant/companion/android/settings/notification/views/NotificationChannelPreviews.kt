package io.homeassistant.companion.android.settings.notification.views

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700, apiLevel = 35)
@Composable
private fun PreviewNotificationChannels() {
    NotificationChannelContent(
        channels = listOf(
            NotificationChannel("general", "General", NotificationManager.IMPORTANCE_DEFAULT),
            NotificationChannel("security", "Security alerts", NotificationManager.IMPORTANCE_HIGH),
        ),
        onEdit = {},
        onDelete = {},
        onUndoDelete = {},
    )
}
