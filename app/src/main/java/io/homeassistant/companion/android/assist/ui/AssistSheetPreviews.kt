package io.homeassistant.companion.android.assist.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.homeassistant.companion.android.common.assist.AssistViewModelBase

private val previewAssistPipeline = AssistUiPipeline(
    serverId = 1,
    serverName = "Home",
    id = "home-assistant",
    name = "Home Assistant"
)

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewAssistSheet() {
    AssistSheetView(
        conversation = listOf(
            AssistMessage("Turn on the living room lights", isInput = true),
            AssistMessage("Done", isInput = false)
        ),
        pipelines = listOf(previewAssistPipeline),
        inputMode = AssistViewModelBase.AssistInputMode.TEXT,
        currentPipeline = previewAssistPipeline,
        fromFrontend = false,
        onSelectPipeline = { _, _ -> },
        onManagePipelines = {},
        onChangeInput = {},
        onTextInput = {},
        onMicrophoneInput = {},
        onHide = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewAssistSpeechBubbles() {
    androidx.compose.foundation.layout.Column {
        SpeechBubble("Turn on the lights", isResponse = false)
        SpeechBubble("Done", isResponse = true)
    }
}
