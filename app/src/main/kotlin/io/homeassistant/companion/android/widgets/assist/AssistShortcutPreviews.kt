package io.homeassistant.companion.android.widgets.assist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.homeassistant.companion.android.common.data.websocket.impl.entities.AssistPipelineListResponse
import io.homeassistant.companion.android.common.data.websocket.impl.entities.AssistPipelineResponse
import io.homeassistant.companion.android.util.previewServer1
import io.homeassistant.companion.android.util.previewServer2

private val previewPipeline = AssistPipelineResponse(
    id = "home-assistant",
    language = "en",
    name = "Home Assistant",
    conversationEngine = "homeassistant",
    conversationLanguage = "en",
    sttEngine = "cloud",
    sttLanguage = "en",
    ttsEngine = "cloud",
    ttsLanguage = "en",
    ttsVoice = "default",
)

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewAssistShortcut() {
    AssistShortcutView(
        selectedServerId = previewServer1.id,
        servers = listOf(previewServer1, previewServer2),
        supported = true,
        pipelines = AssistPipelineListResponse(
            pipelines = listOf(previewPipeline),
            preferredPipeline = previewPipeline.id,
        ),
        onSetServer = {},
        onSubmit = { _, _, _, _ -> },
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewAssistShortcutUnsupported() {
    AssistShortcutView(
        selectedServerId = previewServer1.id,
        servers = listOf(previewServer1),
        supported = false,
        pipelines = null,
        onSetServer = {},
        onSubmit = { _, _, _, _ -> },
    )
}
