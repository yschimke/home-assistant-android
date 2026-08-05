package io.homeassistant.companion.android.settings.vehicle.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.homeassistant.companion.android.common.data.integration.Entity
import io.homeassistant.companion.android.common.data.integration.friendlyName
import io.homeassistant.companion.android.database.server.Server
import io.homeassistant.companion.android.settings.vehicle.ManageAndroidAutoViewModel
import io.homeassistant.companion.android.util.compose.FavoriteEntityRow
import io.homeassistant.companion.android.util.compose.HomeAssistantPreviewTheme
import io.homeassistant.companion.android.util.compose.ScreenThemeCatalog
import io.homeassistant.companion.android.util.compose.ServerExposedDropdownMenu
import io.homeassistant.companion.android.util.compose.SingleEntityPicker
import io.homeassistant.companion.android.util.previewEntity1
import io.homeassistant.companion.android.util.previewEntity3
import io.homeassistant.companion.android.util.previewServer
import io.homeassistant.companion.android.util.vehicle.isVehicleDomain
import org.burnoutcrew.reorderable.ItemPosition
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import io.homeassistant.companion.android.common.R as commonR

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AndroidAutoFavoritesSettings(
    androidAutoViewModel: ManageAndroidAutoViewModel,
    serversList: List<Server>,
    defaultServer: Int
) {
    AndroidAutoFavoritesContent(
        favoriteEntities = androidAutoViewModel.favoritesList,
        entities = androidAutoViewModel.sortedEntities,
        servers = serversList,
        defaultServer = defaultServer,
        onMove = androidAutoViewModel::onMove,
        canDragOver = androidAutoViewModel::canDragOver,
        onSaveFavorites = androidAutoViewModel::saveFavorites,
        onServerSelected = androidAutoViewModel::loadEntities,
        onEntitySelected = androidAutoViewModel::onEntitySelected,
        enableReordering = true
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AndroidAutoFavoritesContent(
    favoriteEntities: List<String>,
    entities: List<Entity<*>>,
    servers: List<Server>,
    defaultServer: Int,
    onMove: (ItemPosition, ItemPosition) -> Unit,
    canDragOver: (ItemPosition) -> Boolean,
    onSaveFavorites: () -> Unit,
    onServerSelected: (Int) -> Unit,
    onEntitySelected: (Boolean, String, Int) -> Unit,
    enableReordering: Boolean
) {
    val reorderState = rememberReorderableLazyListState(
        onMove = onMove,
        canDragOver = { draggedOver, _ -> canDragOver(draggedOver) },
        onDragEnd = { _, _ -> onSaveFavorites() }
    )

    var selectedServer by remember { mutableStateOf(defaultServer) }

    val validEntities = entities.filter {
        !favoriteEntities.contains("$selectedServer-${it.entityId}") && isVehicleDomain(it)
    }

    LazyColumn(
        state = reorderState.listState,
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = Modifier
            .reorderable(reorderState)
    ) {
        item {
            Text(
                text = stringResource(commonR.string.aa_set_favorites),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)
            )
        }

        if (servers.size > 1) {
            item {
                ServerExposedDropdownMenu(
                    servers = servers,
                    current = selectedServer,
                    onSelected = {
                        onServerSelected(it)
                        selectedServer = it
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp)
                )
            }
        }
        item {
            SingleEntityPicker(
                entities = validEntities,
                currentEntity = null,
                onEntityCleared = { /* Nothing */ },
                onEntitySelected = {
                    onEntitySelected(true, it, selectedServer)
                    return@SingleEntityPicker false // Clear input
                },
                modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
                label = { Text(stringResource(commonR.string.add_favorite)) }
            )
        }
        if (favoriteEntities.isNotEmpty() && entities.isNotEmpty()) {
            items(favoriteEntities.size, { favoriteEntities[it] }) { index ->
                val favoriteEntity =
                    favoriteEntities[index].split("-")
                entities.firstOrNull { it.entityId == favoriteEntity[1] && favoriteEntity[0].toInt() == selectedServer }?.let {
                    if (enableReordering) {
                        ReorderableItem(
                            reorderableState = reorderState,
                            key = favoriteEntities[index]
                        ) { isDragging ->
                            FavoriteEntityRow(
                                entityName = it.friendlyName,
                                entityId = it.entityId,
                                onClick = {
                                    onEntitySelected(
                                        false,
                                        it.entityId,
                                        selectedServer
                                    )
                                },
                                checked = true,
                                draggable = true,
                                isDragging = isDragging,
                                reorderableState = reorderState
                            )
                        }
                    } else {
                        FavoriteEntityRow(
                            entityName = it.friendlyName,
                            entityId = it.entityId,
                            onClick = {
                                onEntitySelected(
                                    false,
                                    it.entityId,
                                    selectedServer
                                )
                            },
                            checked = true,
                            draggable = false
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ScreenThemeCatalog
@Composable
private fun PreviewAndroidAutoFavorites() {
    HomeAssistantPreviewTheme {
        AndroidAutoFavoritesContent(
            favoriteEntities = listOf("1-${previewEntity1.entityId}"),
            entities = listOf(previewEntity1, previewEntity3),
            servers = listOf(previewServer),
            defaultServer = previewServer.id,
            onMove = { _, _ -> },
            canDragOver = { true },
            onSaveFavorites = {},
            onServerSelected = {},
            onEntitySelected = { _, _, _ -> },
            enableReordering = false
        )
    }
}
