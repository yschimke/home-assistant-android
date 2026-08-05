package io.homeassistant.companion.android.onboarding.manual

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import io.homeassistant.companion.android.onboarding.OnboardingHeaderView
import io.homeassistant.companion.android.onboarding.OnboardingViewModel
import io.homeassistant.companion.android.common.R as commonR

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ManualSetupView(
    onboardingViewModel: OnboardingViewModel,
    connectedClicked: () -> Unit
) {
    ManualSetupContent(
        manualUrl = onboardingViewModel.manualUrl.value,
        continueEnabled = onboardingViewModel.manualContinueEnabled,
        onUrlUpdated = onboardingViewModel::onManualUrlUpdated,
        connectedClicked = connectedClicked
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ManualSetupContent(
    manualUrl: String,
    continueEnabled: Boolean,
    onUrlUpdated: (String) -> Unit,
    connectedClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OnboardingHeaderView(
            icon = CommunityMaterial.Icon3.cmd_web,
            title = stringResource(id = commonR.string.manual_title)
        )

        Text(
            text = stringResource(id = commonR.string.manual_desc),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        TextField(
            value = manualUrl,
            onValueChange = onUrlUpdated,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            label = { Text(stringResource(id = commonR.string.input_url)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, autoCorrect = false, keyboardType = KeyboardType.Uri),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    connectedClicked()
                }
            )
        )

        Button(
            enabled = continueEnabled,
            onClick = connectedClicked,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text(stringResource(commonR.string.connect))
        }
    }
}
