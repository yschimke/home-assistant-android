package io.homeassistant.companion.android.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewOnboardingHeader() {
    OnboardingHeaderView(
        icon = CommunityMaterial.Icon.cmd_cellphone,
        title = "Connect to Home Assistant"
    )
}
