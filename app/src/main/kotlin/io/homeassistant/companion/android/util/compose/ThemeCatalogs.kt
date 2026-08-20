package io.homeassistant.companion.android.util.compose

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Light", group = "Theme catalog", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", group = "Theme catalog", uiMode = UI_MODE_NIGHT_YES)
annotation class ThemeCatalog

@Preview(
    name = "Light",
    group = "Theme catalog",
    widthDp = 400,
    heightDp = 700,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
)
@Preview(
    name = "Dark",
    group = "Theme catalog",
    widthDp = 400,
    heightDp = 700,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
)
annotation class ScreenThemeCatalog

/**
 * Theme wrapper for the catalog previews.
 *
 * The previewed screens are still Material 2, so this mirrors what they get at runtime via
 * [HomeAssistantAppTheme]. Switch to `HATheme` per screen as screens migrate to Material 3.
 */
@Suppress("DEPRECATION")
@Composable
fun HomeAssistantPreviewTheme(content: @Composable () -> Unit) {
    HomeAssistantAppTheme(content = content)
}
