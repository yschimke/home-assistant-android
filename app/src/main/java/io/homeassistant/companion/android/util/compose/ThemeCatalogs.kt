package io.homeassistant.companion.android.util.compose

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.themeadapter.material.MdcTheme

@Preview(name = "Light", group = "Theme catalog", uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", group = "Theme catalog", uiMode = UI_MODE_NIGHT_YES)
annotation class ThemeCatalog

@Preview(
    name = "Light",
    group = "Theme catalog",
    widthDp = 400,
    heightDp = 700,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark",
    group = "Theme catalog",
    widthDp = 400,
    heightDp = 700,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
annotation class ScreenThemeCatalog

@Composable
fun HomeAssistantPreviewTheme(content: @Composable () -> Unit) {
    MdcTheme(content = content)
}
