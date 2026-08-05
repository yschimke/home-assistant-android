package io.homeassistant.companion.android.nfc.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewNfcWelcomeEnabled() {
    NfcWelcomeView(isNfcEnabled = true, onReadClicked = {}, onWriteClicked = {})
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewNfcWelcomeDisabled() {
    NfcWelcomeView(isNfcEnabled = false, onReadClicked = {}, onWriteClicked = {})
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewNfcRead() {
    NfcReadView()
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewNfcWrite() {
    NfcWriteView(isNfcEnabled = true, identifier = "home-assistant-tag", onSetIdentifier = {})
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewNfcEdit() {
    NfcEditView(
        identifier = "home-assistant-tag",
        showDeviceSample = true,
        onDuplicateClicked = {},
        onFireEventClicked = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 700)
@Composable
private fun PreviewTagReader() {
    TagReaderView()
}
