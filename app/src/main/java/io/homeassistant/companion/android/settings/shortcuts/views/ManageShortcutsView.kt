package io.homeassistant.companion.android.settings.shortcuts.views

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.iconics.compose.IconicsPainter
import com.mikepenz.iconics.typeface.IIcon
import io.homeassistant.companion.android.common.R
import io.homeassistant.companion.android.settings.shortcuts.ManageShortcutsSettingsFragment
import io.homeassistant.companion.android.settings.shortcuts.ManageShortcutsViewModel
import io.homeassistant.companion.android.util.compose.HomeAssistantPreviewTheme
import io.homeassistant.companion.android.util.compose.ScreenThemeCatalog
import io.homeassistant.companion.android.util.compose.ServerExposedDropdownMenu
import io.homeassistant.companion.android.util.compose.SingleEntityPicker
import io.homeassistant.companion.android.util.previewEntity1
import io.homeassistant.companion.android.util.previewEntity2
import io.homeassistant.companion.android.util.previewServer

data class ManageShortcutsUiState(
    val shortcuts: List<ManageShortcutsViewModel.Shortcut>,
    val canPinShortcuts: Boolean,
    val pinnedShortcutIds: List<String>,
    val dynamicShortcutIds: Set<String>,
    val servers: List<io.homeassistant.companion.android.database.server.Server>,
    val entities: Map<Int, List<io.homeassistant.companion.android.common.data.integration.Entity<*>>>
)

@RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
fun ManageShortcutsView(
    viewModel: ManageShortcutsViewModel,
    showIconDialog: (tag: String) -> Unit
) {
    ManageShortcutsContent(
        state = ManageShortcutsUiState(
            shortcuts = viewModel.shortcuts,
            canPinShortcuts = viewModel.canPinShortcuts,
            pinnedShortcutIds = viewModel.pinnedShortcuts.map { it.id },
            dynamicShortcutIds = viewModel.dynamicShortcuts.map { it.id }.toSet(),
            servers = viewModel.servers,
            entities = viewModel.entities
        ),
        showIconDialog = showIconDialog,
        onPinnedShortcutSelected = viewModel::setPinnedShortcutData,
        onCreateShortcut = viewModel::createShortcut,
        onDeleteShortcut = viewModel::deleteShortcut
    )
}

@RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
fun ManageShortcutsContent(
    state: ManageShortcutsUiState,
    showIconDialog: (tag: String) -> Unit,
    onPinnedShortcutSelected: (String) -> Unit,
    onCreateShortcut: (String, Int, String, String, String, IIcon?) -> Unit,
    onDeleteShortcut: (String) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        item {
            Text(
                text = stringResource(id = R.string.shortcut_instruction_desc),
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Divider()
        }

        val shortcutCount = if (state.canPinShortcuts) {
            ManageShortcutsSettingsFragment.MAX_SHORTCUTS + 1
        } else {
            ManageShortcutsSettingsFragment.MAX_SHORTCUTS
        }

        items(shortcutCount) { i ->
            CreateShortcutView(
                i = i,
                state = state,
                showIconDialog = showIconDialog,
                onPinnedShortcutSelected = onPinnedShortcutSelected,
                onCreateShortcut = onCreateShortcut,
                onDeleteShortcut = onDeleteShortcut
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
private fun CreateShortcutView(
    i: Int,
    state: ManageShortcutsUiState,
    showIconDialog: (tag: String) -> Unit,
    onPinnedShortcutSelected: (String) -> Unit,
    onCreateShortcut: (String, Int, String, String, String, IIcon?) -> Unit,
    onDeleteShortcut: (String) -> Unit
) {
    val context = LocalContext.current
    var expandedPinnedShortcuts by remember { mutableStateOf(false) }

    val index = i + 1
    val shortcut = state.shortcuts[i]
    val shortcutId = ManageShortcutsSettingsFragment.SHORTCUT_PREFIX + "_" + index

    Text(
        text = if (index < 6) {
            stringResource(id = R.string.shortcut) + " $index"
        } else {
            stringResource(
                id = R.string.shortcut_pinned
            )
        },
        fontSize = 20.sp,
        color = colorResource(id = R.color.colorAccent),
        modifier = Modifier.padding(top = 20.dp)
    )

    if (index == 5) {
        Text(
            text = stringResource(id = R.string.shortcut5_note),
            fontSize = 14.sp
        )
    }

    if (index == 6) {
        Text(
            text = stringResource(id = R.string.shortcut_pinned_note),
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        )

        val pinnedShortCutIds = state.pinnedShortcutIds

        if (pinnedShortCutIds.isNotEmpty()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.shortcut_pinned_list),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Box {
                    OutlinedButton(onClick = { expandedPinnedShortcuts = true }) {
                        Text(if (shortcut.id.value in pinnedShortCutIds) shortcut.id.value ?: "" else "")
                    }

                    DropdownMenu(expanded = expandedPinnedShortcuts, onDismissRequest = { expandedPinnedShortcuts = false }) {
                        for (item in pinnedShortCutIds) {
                            DropdownMenuItem(onClick = {
                                onPinnedShortcutSelected(item)
                                expandedPinnedShortcuts = false
                            }) {
                                Text(item)
                            }
                        }
                    }
                }
            }
        }
        TextField(
            value = shortcut.id.value ?: "",
            onValueChange = { shortcut.id.value = it },
            label = {
                Text(stringResource(id = R.string.shortcut_pinned_id))
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.shortcut_icon),
            fontSize = 15.sp,
            modifier = Modifier.padding(end = 10.dp)
        )
        OutlinedButton(onClick = {
            showIconDialog(shortcutId)
        }) {
            val icon = shortcut.selectedIcon.value
            val painter = if (icon != null) {
                remember(icon) { IconicsPainter(icon) }
            } else {
                painterResource(R.drawable.ic_stat_ic_notification_blue)
            }

            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.shortcut_icon),
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(colorResource(R.color.colorAccent))
            )
        }
    }

    TextField(
        value = shortcut.label.value,
        onValueChange = { shortcut.label.value = it },
        label = {
            Text(
                if (index < 6) {
                    "${stringResource(id = R.string.shortcut)} $index ${stringResource(id = R.string.label)}"
                } else {
                    stringResource(id = R.string.shortcut_pinned_label)
                }
            )
        },
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
    )

    TextField(
        value = shortcut.desc.value,
        onValueChange = { shortcut.desc.value = it },
        label = {
            Text(
                if (index < 6) {
                    "${stringResource(id = R.string.shortcut)} $index ${stringResource(id = R.string.description)}"
                } else {
                    stringResource(id = R.string.shortcut_pinned_desc)
                }
            )
        },
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
    )

    if (state.servers.size > 1 || state.servers.none { it.id == shortcut.serverId.value }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ServerExposedDropdownMenu(
                servers = state.servers,
                current = shortcut.serverId.value,
                onSelected = { shortcut.serverId.value = it }
            )
        }
    }

    Text(
        text = stringResource(id = R.string.shortcut_type),
        modifier = Modifier.padding(top = 16.dp)
    )

    Row {
        ShortcutRadioButtonRow(shortcut = shortcut, type = "lovelace")
        ShortcutRadioButtonRow(shortcut = shortcut, type = "entityId")
    }

    if (shortcut.type.value == "lovelace") {
        TextField(
            value = shortcut.path.value,
            onValueChange = { shortcut.path.value = it },
            label = { Text(stringResource(id = R.string.lovelace_view_dashboard)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, autoCorrect = false, keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
    } else {
        SingleEntityPicker(
            entities = state.entities[shortcut.serverId.value].orEmpty(),
            currentEntity = shortcut.path.value.split(":").getOrNull(1),
            onEntityCleared = {
                shortcut.path.value = ""
            },
            onEntitySelected = {
                shortcut.path.value = "entityId:$it"
                return@SingleEntityPicker true
            },
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
    if (shortcutId in state.dynamicShortcutIds) {
        shortcut.delete.value = true
    }
    Button(
        onClick = {
            if (index < 6) {
                if (shortcut.delete.value) {
                    Toast.makeText(context, R.string.shortcut_updated, Toast.LENGTH_SHORT).show()
                }
                shortcut.delete.value = true
            }
            onCreateShortcut(
                if (index < 6) shortcutId else shortcut.id.value!!,
                shortcut.serverId.value,
                shortcut.label.value,
                shortcut.desc.value,
                shortcut.path.value,
                shortcut.selectedIcon.value
            )
        },
        enabled =
        (index < 6 || !shortcut.id.value.isNullOrEmpty()) &&
            shortcut.label.value.isNotEmpty() &&
            shortcut.desc.value.isNotEmpty() &&
            shortcut.path.value.isNotEmpty() &&
            state.servers.any { it.id == shortcut.serverId.value }
    ) {
        Text(
            text = stringResource(
                id =
                if (
                    if (index < 6) {
                        shortcut.delete.value
                    } else {
                        var isCurrentPinned = false
                        if (state.pinnedShortcutIds.isEmpty()) {
                            isCurrentPinned = false
                        } else {
                            for (item in state.pinnedShortcutIds) {
                                isCurrentPinned = when (item) {
                                    state.shortcuts.last().id.value -> true
                                    else -> false
                                }
                            }
                        }
                        isCurrentPinned
                    }
                ) {
                    R.string.update_shortcut
                } else {
                    R.string.add_shortcut
                }
            )
        )
    }

    if (index < 6 && shortcut.delete.value) {
        AddDeleteButton(shortcut = shortcut, shortcutId = shortcutId, onDeleteShortcut = onDeleteShortcut)
        Divider()
    }
}

@RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
private fun ShortcutRadioButtonRow(shortcut: ManageShortcutsViewModel.Shortcut, type: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = shortcut.type.value == type,
            onClick = { shortcut.type.value = type }
        )
        Text(stringResource(id = if (type == "lovelace") R.string.lovelace else R.string.entity))
    }
}

@RequiresApi(Build.VERSION_CODES.N_MR1)
@Composable
private fun AddDeleteButton(
    shortcut: ManageShortcutsViewModel.Shortcut,
    shortcutId: String,
    onDeleteShortcut: (String) -> Unit
) {
    Button(
        onClick = {
            onDeleteShortcut(shortcutId)
            shortcut.delete.value = false
        }
    ) {
        Text(stringResource(id = R.string.delete_shortcut))
    }
}

@RequiresApi(Build.VERSION_CODES.N_MR1)
@ScreenThemeCatalog
@Composable
private fun PreviewManageShortcuts() {
    val shortcuts = List(ManageShortcutsSettingsFragment.MAX_SHORTCUTS) { index ->
        ManageShortcutsViewModel.Shortcut(
            id = mutableStateOf("shortcut_${index + 1}"),
            serverId = mutableStateOf(previewServer.id),
            selectedIcon = mutableStateOf(null),
            label = mutableStateOf(if (index == 0) "Living room" else ""),
            desc = mutableStateOf(if (index == 0) "Open the living room dashboard" else ""),
            path = mutableStateOf(if (index == 0) "/lovelace/living-room" else ""),
            type = mutableStateOf("lovelace"),
            delete = mutableStateOf(index == 0)
        )
    }
    HomeAssistantPreviewTheme {
        ManageShortcutsContent(
            state = ManageShortcutsUiState(
                shortcuts = shortcuts,
                canPinShortcuts = false,
                pinnedShortcutIds = emptyList(),
                dynamicShortcutIds = setOf("shortcut_1"),
                servers = listOf(previewServer),
                entities = mapOf(previewServer.id to listOf(previewEntity1, previewEntity2))
            ),
            showIconDialog = {},
            onPinnedShortcutSelected = {},
            onCreateShortcut = { _, _, _, _, _, _ -> },
            onDeleteShortcut = {}
        )
    }
}
