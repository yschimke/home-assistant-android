package io.homeassistant.companion.android.settings.qs.views

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.iconics.typeface.IIcon
import io.homeassistant.companion.android.common.R
import io.homeassistant.companion.android.common.data.integration.Entity
import io.homeassistant.companion.android.database.server.Server
import io.homeassistant.companion.android.settings.qs.ManageTilesViewModel
import io.homeassistant.companion.android.settings.qs.TileSlot
import io.homeassistant.companion.android.util.compose.HomeAssistantPreviewTheme
import io.homeassistant.companion.android.util.compose.ScreenThemeCatalog
import io.homeassistant.companion.android.util.compose.ServerExposedDropdownMenu
import io.homeassistant.companion.android.util.compose.SingleEntityPicker
import io.homeassistant.companion.android.util.previewEntity1
import io.homeassistant.companion.android.util.previewEntity3
import io.homeassistant.companion.android.util.previewServer
import io.homeassistant.companion.android.util.previewServer2
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class ManageTilesUiState(
    val slots: List<TileSlot>,
    val selectedTile: TileSlot,
    val tileLabel: String,
    val tileSubtitle: String?,
    val servers: List<Server>,
    val selectedServerId: Int,
    val entities: List<Entity<*>>,
    val selectedEntityId: String,
    val selectedIcon: IIcon?,
    val selectedIconId: String?,
    val shouldVibrate: Boolean,
    val authRequired: Boolean,
    val submitButtonLabel: Int
)

@Composable
fun ManageTilesView(
    viewModel: ManageTilesViewModel,
    onShowIconDialog: (tag: String?) -> Unit
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect("snackbar") {
        viewModel.tileInfoSnackbar.onEach {
            if (it != 0) {
                scaffoldState.snackbarHostState.showSnackbar(context.getString(it))
            }
        }.launchIn(this)
    }

    ManageTilesContent(
        state = ManageTilesUiState(
            slots = viewModel.slots,
            selectedTile = viewModel.selectedTile,
            tileLabel = viewModel.tileLabel,
            tileSubtitle = viewModel.tileSubtitle,
            servers = viewModel.servers,
            selectedServerId = viewModel.selectedServerId,
            entities = viewModel.sortedEntities,
            selectedEntityId = viewModel.selectedEntityId,
            selectedIcon = viewModel.selectedIcon,
            selectedIconId = viewModel.selectedIconId,
            shouldVibrate = viewModel.selectedShouldVibrate,
            authRequired = viewModel.tileAuthRequired,
            submitButtonLabel = viewModel.submitButtonLabel
        ),
        scaffoldState = scaffoldState,
        onSelectTile = viewModel::selectTile,
        onLabelChanged = { viewModel.tileLabel = it },
        onSubtitleChanged = { viewModel.tileSubtitle = it },
        onSelectServer = viewModel::selectServerId,
        onSelectEntity = viewModel::selectEntityId,
        onShowIconDialog = onShowIconDialog,
        onClearIcon = { viewModel.selectIcon(null) },
        onVibrateChanged = { viewModel.selectedShouldVibrate = it },
        onAuthRequiredChanged = { viewModel.tileAuthRequired = it },
        onSubmit = viewModel::addTile
    )
}

@Composable
fun ManageTilesContent(
    state: ManageTilesUiState,
    scaffoldState: androidx.compose.material.ScaffoldState,
    onSelectTile: (Int) -> Unit,
    onLabelChanged: (String) -> Unit,
    onSubtitleChanged: (String) -> Unit,
    onSelectServer: (Int) -> Unit,
    onSelectEntity: (String) -> Unit,
    onShowIconDialog: (String?) -> Unit,
    onClearIcon: () -> Unit,
    onVibrateChanged: (Boolean) -> Unit,
    onAuthRequiredChanged: (Boolean) -> Unit,
    onSubmit: () -> Unit
) {
    val scrollState = rememberScrollState()
    var expandedTile by remember { mutableStateOf(false) }
    Scaffold(scaffoldState = scaffoldState) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(scrollState)
        ) {
            Column(modifier = Modifier.padding(all = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.tile_select),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Box {
                        OutlinedButton(onClick = { expandedTile = true }) {
                            Text(state.selectedTile.name)
                        }

                        DropdownMenu(expanded = expandedTile, onDismissRequest = { expandedTile = false }) {
                            for ((index, slot) in state.slots.withIndex()) {
                                DropdownMenuItem(onClick = {
                                    onSelectTile(index)
                                    expandedTile = false
                                }) {
                                    Text(slot.name)
                                }
                            }
                        }
                    }
                }

                Divider()
                TextField(
                    value = state.tileLabel,
                    onValueChange = onLabelChanged,
                    label = {
                        Text(text = stringResource(id = R.string.tile_label))
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    TextField(
                        value = state.tileSubtitle.orEmpty(),
                        onValueChange = onSubtitleChanged,
                        label = {
                            Text(text = stringResource(id = R.string.tile_subtitle))
                        },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    )
                }

                if (state.servers.size > 1 || state.servers.none { it.id == state.selectedServerId }) {
                    ServerExposedDropdownMenu(
                        servers = state.servers,
                        current = state.selectedServerId,
                        onSelected = onSelectServer,
                        title = R.string.tile_server,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                SingleEntityPicker(
                    entities = state.entities,
                    currentEntity = state.selectedEntityId,
                    onEntityCleared = { onSelectEntity("") },
                    onEntitySelected = {
                        onSelectEntity(it)
                        return@SingleEntityPicker true
                    },
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.tile_entity)) }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.tile_icon),
                        fontSize = 15.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    OutlinedButton(
                        onClick = { onShowIconDialog(state.selectedTile.id) }
                    ) {
                        state.selectedIcon?.let { icon ->
                            com.mikepenz.iconics.compose.Image(
                                icon,
                                contentDescription = stringResource(id = R.string.tile_icon),
                                colorFilter = ColorFilter.tint(colorResource(R.color.colorAccent)),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    if (state.selectedIconId != null && state.selectedEntityId.isNotBlank()) {
                        TextButton(
                            modifier = Modifier.padding(start = 4.dp),
                            onClick = onClearIcon
                        ) {
                            Text(text = stringResource(R.string.tile_icon_original))
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.tile_vibrate),
                        fontSize = 15.sp
                    )
                    Switch(
                        checked = state.shouldVibrate,
                        onCheckedChange = onVibrateChanged,
                        colors = SwitchDefaults.colors(uncheckedThumbColor = colorResource(R.color.colorSwitchUncheckedThumb))
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.tile_auth_required),
                        fontSize = 15.sp
                    )
                    Switch(
                        checked = state.authRequired,
                        onCheckedChange = onAuthRequiredChanged,
                        colors = SwitchDefaults.colors(uncheckedThumbColor = colorResource(R.color.colorSwitchUncheckedThumb))
                    )
                }

                Button(
                    onClick = onSubmit,
                    enabled = state.tileLabel.isNotBlank() &&
                        state.selectedServerId in state.servers.map { it.id } &&
                        state.selectedEntityId in state.entities.map { it.entityId }
                ) {
                    Text(stringResource(state.submitButtonLabel))
                }
            }
        }
    }
}

@ScreenThemeCatalog
@Composable
private fun PreviewManageTiles() {
    val scaffoldState = rememberScaffoldState()
    HomeAssistantPreviewTheme {
        ManageTilesContent(
            state = ManageTilesUiState(
                slots = listOf(TileSlot("tile_1", "Tile 1"), TileSlot("tile_2", "Tile 2")),
                selectedTile = TileSlot("tile_1", "Tile 1"),
                tileLabel = "Living room light",
                tileSubtitle = "Tap to toggle",
                servers = listOf(previewServer, previewServer2),
                selectedServerId = previewServer.id,
                entities = listOf(previewEntity1, previewEntity3),
                selectedEntityId = previewEntity1.entityId,
                selectedIcon = null,
                selectedIconId = null,
                shouldVibrate = true,
                authRequired = false,
                submitButtonLabel = R.string.tile_save
            ),
            scaffoldState = scaffoldState,
            onSelectTile = {},
            onLabelChanged = {},
            onSubtitleChanged = {},
            onSelectServer = {},
            onSelectEntity = {},
            onShowIconDialog = {},
            onClearIcon = {},
            onVibrateChanged = {},
            onAuthRequiredChanged = {},
            onSubmit = {}
        )
    }
}
