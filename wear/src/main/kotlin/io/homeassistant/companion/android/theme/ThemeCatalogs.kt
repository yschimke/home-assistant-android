package io.homeassistant.companion.android.theme

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Colors
import androidx.wear.tooling.preview.devices.WearDevices

@Preview(
    name = "Light",
    group = "Theme catalog",
    device = WearDevices.LARGE_ROUND,
    showBackground = true,
    backgroundColor = 0xFFF7F9FC,
    uiMode = UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Dark",
    group = "Theme catalog",
    device = WearDevices.LARGE_ROUND,
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = UI_MODE_NIGHT_YES,
)
annotation class WearThemeCatalog

@Composable
fun WearPreviewTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val background = if (darkTheme) Color.Black else Color(0xFFF7F9FC)
    val onBackground = if (darkTheme) Color.White else Color(0xFF15202B)
    androidx.wear.compose.material.MaterialTheme(
        colors = Colors(
            background = background,
            surface = background,
            onBackground = onBackground,
            onSurface = onBackground,
            onSurfaceVariant = if (darkTheme) Color.LightGray else Color(0xFF41484D),
        ),
    ) {
        WearAppTheme(
            colorScheme = if (darkTheme) {
                wearColorScheme
            } else {
                wearColorScheme.copy(
                    background = background,
                    surfaceContainer = background,
                    onBackground = onBackground,
                    onSurfaceVariant = Color(0xFF41484D),
                    primary = Color(0xFF0078B4),
                    onPrimary = Color.White,
                )
            },
            content = content,
        )
    }
}
