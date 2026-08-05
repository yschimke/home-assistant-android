package io.homeassistant.companion.android.settings.wear.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.homeassistant.companion.android.common.data.integration.Entity
import io.homeassistant.companion.android.common.data.integration.friendlyName
import io.homeassistant.companion.android.settings.wear.SettingsWearViewModel
import io.homeassistant.companion.android.util.compose.FavoriteEntityRow
import io.homeassistant.companion.android.util.compose.HomeAssistantPreviewTheme
import io.homeassistant.companion.android.util.compose.ScreenThemeCatalog
import io.homeassistant.companion.android.util.compose.SingleEntityPicker
import io.homeassistant.companion.android.util.previewEntity1
import io.homeassistant.companion.android.util.previewEntity2
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import io.homeassistant.companion.android.common.R as commonR

@Composable
fun LoadWearFavoritesSettings(
    settingsWearViewModel: SettingsWearViewModel,
    onBackClicked: () -> Unit,
    events: SharedFlow<String>
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect("snackbar") {
        events.onEach { message ->
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() // in case of rapid-fire events
            scaffoldState.snackbarHostState.showSnackbar(message)
        }.launchIn(this)
    }

    WearFavoritesContent(
        favoriteEntityIds = settingsWearViewModel.favoriteEntityIds,
        entities = settingsWearViewModel.entities,
        supportedDomains = settingsWearViewModel.supportedDomains,
        scaffoldState = scaffoldState,
        onBackClicked = onBackClicked,
        onMove = settingsWearViewModel::onMove,
        canDragOver = settingsWearViewModel::canDragOver,
        onSaveFavorites = { settingsWearViewModel.sendHomeFavorites(settingsWearViewModel.favoriteEntityIds.toList()) },
        onEntitySelected = settingsWearViewModel::onEntitySelected,
        enableReordering = true
    )
}

@Composable
fun WearFavoritesContent(
    favoriteEntityIds: List<String>,
    entities: Map<String, Entity<*>>,
    supportedDomains: List<String>,
    scaffoldState: androidx.compose.material.ScaffoldState,
    onBackClicked: () -> Unit,
    onMove: (org.burnoutcrew.reorderable.ItemPosition, org.burnoutcrew.reorderable.ItemPosition) -> Unit,
    canDragOver: (org.burnoutcrew.reorderable.ItemPosition) -> Boolean,
    onSaveFavorites: () -> Unit,
    onEntitySelected: (Boolean, String) -> Unit,
    enableReordering: Boolean
) {
    val reorderState = rememberReorderableLazyListState(
        onMove = onMove,
        canDragOver = { draggedOver, _ -> canDragOver(draggedOver) },
        onDragEnd = { _, _ -> onSaveFavorites() }
    )
    val validEntities = entities.values.filter {
        it.entityId !in favoriteEntityIds && it.entityId.substringBefore('.') in supportedDomains
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SettingsWearTopAppBar(
                title = { Text(stringResource(commonR.string.wear_favorite_entities)) },
                onBackClicked = onBackClicked,
                docsLink = WEAR_DOCS_LINK
            )
        }
    ) { contentPadding ->
        LazyColumn(
            state = reorderState.listState,
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier
                .padding(contentPadding)
                .reorderable(reorderState)
        ) {
            item {
                Text(
                    text = stringResource(commonR.string.wear_set_favorites),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                SingleEntityPicker(
                    entities = validEntities,
                    currentEntity = null,
                    onEntityCleared = { /* Nothing */ },
                    onEntitySelected = {
                        onEntitySelected(true, it)
                        return@SingleEntityPicker false // Clear input
                    },
                    modifier = Modifier.padding(all = 16.dp),
                    label = { Text(stringResource(commonR.string.add_favorite)) }
                )
            }
            items(favoriteEntityIds.size, { favoriteEntityIds[it] }) { index ->
                val favoriteEntityID = favoriteEntityIds[index].replace("[", "").replace("]", "")
                entities[favoriteEntityID]?.let {
                    if (enableReordering) {
                        ReorderableItem(
                            reorderableState = reorderState,
                            key = favoriteEntityIds[index]
                        ) { isDragging ->
                            FavoriteEntityRow(
                                entityName = it.friendlyName,
                                entityId = favoriteEntityID,
                                onClick = { onEntitySelected(false, favoriteEntityIds[index]) },
                                checked = true,
                                draggable = true,
                                isDragging = isDragging,
                                reorderableState = reorderState
                            )
                        }
                    } else {
                        FavoriteEntityRow(
                            entityName = it.friendlyName,
                            entityId = favoriteEntityID,
                            onClick = { onEntitySelected(false, favoriteEntityIds[index]) },
                            checked = true,
                            draggable = false
                        )
                    }
                }
            }
        }
    }
}

@ScreenThemeCatalog
@Composable
private fun PreviewWearFavorites() {
    HomeAssistantPreviewTheme {
        WearFavoritesContent(
            favoriteEntityIds = listOf(previewEntity1.entityId),
            entities = listOf(previewEntity1, previewEntity2).associateBy { it.entityId },
            supportedDomains = listOf("light", "scene"),
            scaffoldState = rememberScaffoldState(),
            onBackClicked = {},
            onMove = { _, _ -> },
            canDragOver = { true },
            onSaveFavorites = {},
            onEntitySelected = { _, _ -> },
            enableReordering = false
        )
    }
}
