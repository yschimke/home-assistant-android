package io.homeassistant.companion.android.home.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.mikepenz.iconics.compose.Image
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import io.homeassistant.companion.android.common.data.integration.Entity
import io.homeassistant.companion.android.common.util.STATE_UNKNOWN
import io.homeassistant.companion.android.database.wear.FavoriteCaches
import io.homeassistant.companion.android.home.MainViewModel
import io.homeassistant.companion.android.theme.WearPreviewTheme
import io.homeassistant.companion.android.theme.WearThemeCatalog
import io.homeassistant.companion.android.theme.getFilledTonalButtonColors
import io.homeassistant.companion.android.theme.getPrimaryButtonColors
import io.homeassistant.companion.android.util.getIcon
import io.homeassistant.companion.android.util.onEntityClickedFeedback
import io.homeassistant.companion.android.util.previewEntity1
import io.homeassistant.companion.android.util.previewEntity2
import io.homeassistant.companion.android.util.previewEntity3
import io.homeassistant.companion.android.views.ExpandableListHeader
import io.homeassistant.companion.android.views.ListHeader
import io.homeassistant.companion.android.views.ThemeLazyColumn
import io.homeassistant.companion.android.common.R as commonR

data class NamedEntitySection(val name: String, val entities: List<Entity<*>>)

data class MainUiState(
    val favoriteEntityIds: List<String>,
    val entities: Map<String, Entity<*>>,
    val cachedFavorites: Map<String, FavoriteCaches>,
    val loadingState: MainViewModel.LoadingState,
    val isFavoritesOnly: Boolean,
    val areaSections: List<NamedEntitySection>,
    val domainSections: List<NamedEntitySection>,
    val allDomainSections: List<NamedEntitySection>
)

@Composable
fun MainView(
    mainViewModel: MainViewModel,
    favoriteEntityIds: List<String>,
    onEntityClicked: (String, String) -> Unit,
    onEntityLongClicked: (String) -> Unit,
    onRetryLoadEntitiesClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onNavigationClicked: (entityLists: Map<String, List<Entity<*>>>, listOrder: List<String>, filter: (Entity<*>) -> Boolean) -> Unit,
    isHapticEnabled: Boolean,
    isToastEnabled: Boolean
) {
    val visibleEntity: (Entity<*>) -> Boolean = {
        mainViewModel.getCategoryForEntity(it.entityId) == null &&
            mainViewModel.getHiddenByForEntity(it.entityId) == null
    }
    val domainEntity: (Entity<*>) -> Boolean = {
        mainViewModel.getAreaForEntity(it.entityId) == null && visibleEntity(it)
    }
    MainContent(
        state = MainUiState(
            favoriteEntityIds = favoriteEntityIds,
            entities = mainViewModel.entities,
            cachedFavorites = favoriteEntityIds.mapNotNull { id -> mainViewModel.getCachedEntity(id)?.let { id to it } }.toMap(),
            loadingState = mainViewModel.loadingState.value,
            isFavoritesOnly = mainViewModel.isFavoritesOnly,
            areaSections = mainViewModel.entitiesByAreaOrder.mapNotNull { id ->
                val entities = mainViewModel.entitiesByArea[id].orEmpty().filter(visibleEntity)
                mainViewModel.areas.firstOrNull { it.areaId == id }?.takeIf { entities.isNotEmpty() }
                    ?.let { NamedEntitySection(it.name, entities) }
            },
            domainSections = mainViewModel.entitiesByDomainOrder.mapNotNull { domain ->
                val entities = mainViewModel.entitiesByDomain[domain].orEmpty().filter(domainEntity)
                mainViewModel.stringForDomain(domain)?.takeIf { entities.isNotEmpty() }
                    ?.let { NamedEntitySection(it, entities) }
            },
            allDomainSections = mainViewModel.entitiesByDomain.mapNotNull { (domain, entities) ->
                mainViewModel.stringForDomain(domain)?.let { NamedEntitySection(it, entities) }
            }
        ),
        onEntityClicked = onEntityClicked,
        onEntityLongClicked = onEntityLongClicked,
        onRetryLoadEntitiesClicked = onRetryLoadEntitiesClicked,
        onSettingsClicked = onSettingsClicked,
        onNavigationClicked = onNavigationClicked,
        isHapticEnabled = isHapticEnabled,
        isToastEnabled = isToastEnabled
    )
}

@Composable
fun MainContent(
    state: MainUiState,
    onEntityClicked: (String, String) -> Unit,
    onEntityLongClicked: (String) -> Unit,
    onRetryLoadEntitiesClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onNavigationClicked: (entityLists: Map<String, List<Entity<*>>>, listOrder: List<String>, filter: (Entity<*>) -> Boolean) -> Unit,
    isHapticEnabled: Boolean,
    isToastEnabled: Boolean
) {
    var expandedFavorites: Boolean by rememberSaveable { mutableStateOf(true) }

    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    ThemeLazyColumn {
        if (state.favoriteEntityIds.isNotEmpty()) {
            item {
                ExpandableListHeader(
                    string = stringResource(commonR.string.favorites),
                    expanded = expandedFavorites,
                    onExpandChanged = { expandedFavorites = it }
                )
            }
            if (expandedFavorites) {
                items(state.favoriteEntityIds.size) { index ->
                    val favoriteEntityID = state.favoriteEntityIds[index].split(",")[0]
                    if (state.entities.isEmpty()) {
                        // when we don't have the state of the entity, create a Chip from cache as we don't have the state yet
                        val cached = state.cachedFavorites[favoriteEntityID]
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            icon = {
                                Image(
                                    asset = getIcon(cached?.icon, favoriteEntityID.split(".")[0], context),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                                )
                            },
                            label = {
                                Text(
                                    text = cached?.friendlyName ?: favoriteEntityID,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            onClick = {
                                onEntityClicked(favoriteEntityID, STATE_UNKNOWN)
                                onEntityClickedFeedback(isToastEnabled, isHapticEnabled, context, favoriteEntityID, haptic)
                            },
                            colors = getFilledTonalButtonColors()
                        )
                    } else {
                        state.entities.values.toList()
                            .firstOrNull { it.entityId == favoriteEntityID }
                            ?.let {
                                EntityUi(
                                    state.entities[favoriteEntityID]!!,
                                    onEntityClicked,
                                    isHapticEnabled,
                                    isToastEnabled
                                ) { entityId -> onEntityLongClicked(entityId) }
                            }
                    }
                }
            }
        }

        if (!state.isFavoritesOnly) {
            when (state.loadingState) {
                MainViewModel.LoadingState.LOADING -> {
                    if (state.favoriteEntityIds.isEmpty()) {
                        // Add a Spacer to prevent settings being pushed to the screen center
                        item { Spacer(modifier = Modifier.fillMaxWidth()) }
                    }
                    item {
                        val minHeight =
                            if (state.favoriteEntityIds.isEmpty()) {
                                LocalConfiguration.current.screenHeightDp - 64
                            } else {
                                0
                            }
                        Column(
                            modifier = Modifier
                                .heightIn(min = minHeight.dp)
                                .fillMaxSize()
                                .padding(vertical = if (state.favoriteEntityIds.isEmpty()) 0.dp else 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ListHeader(id = commonR.string.loading)
                            CircularProgressIndicator()
                        }
                    }
                }
                MainViewModel.LoadingState.ERROR -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ListHeader(id = commonR.string.error_loading_entities)
                            Button(
                                label = {
                                    Text(
                                        text = stringResource(commonR.string.retry),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                onClick = onRetryLoadEntitiesClicked,
                                colors = ButtonDefaults.buttonColors()
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
                MainViewModel.LoadingState.READY -> {
                    if (state.entities.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(commonR.string.no_supported_entities),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 32.dp)
                                )
                                Text(
                                    text = stringResource(commonR.string.no_supported_entities_summary),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                )
                            }
                        }
                    }

                    if (state.areaSections.isNotEmpty()) {
                        item {
                            ListHeader(id = commonR.string.areas)
                        }
                        for (area in state.areaSections) {
                            item {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text(area.name) },
                                    onClick = {
                                        onNavigationClicked(
                                            mapOf(area.name to area.entities),
                                            listOf(area.name)
                                        ) { true }
                                    },
                                    colors = getPrimaryButtonColors()
                                )
                            }
                        }
                    }

                    if (state.domainSections.isNotEmpty()) {
                        item {
                            ListHeader(id = commonR.string.more_entities)
                        }
                    }
                    // Buttons for each existing category
                    for (domain in state.domainSections) {
                        item {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                icon = {
                                    getIcon("", domain.entities.first().entityId.substringBefore('.'), context)
                                        .let { Image(asset = it) }
                                },
                                label = { Text(domain.name) },
                                onClick = {
                                    onNavigationClicked(
                                        mapOf(domain.name to domain.entities),
                                        listOf(domain.name)
                                    ) { true }
                                },
                                colors = getPrimaryButtonColors()
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                    // All entities regardless of area
                    if (state.entities.isNotEmpty()) {
                        item {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                icon = {
                                    Image(
                                        asset = CommunityMaterial.Icon.cmd_animation,
                                        colorFilter = ColorFilter.tint(Color.White)
                                    )
                                },
                                label = {
                                    Text(text = stringResource(commonR.string.all_entities))
                                },
                                onClick = {
                                    onNavigationClicked(
                                        state.allDomainSections.associate { it.name to it.entities },
                                        state.allDomainSections.map { it.name }.sorted()
                                    ) { true }
                                },
                                colors = getFilledTonalButtonColors()
                            )
                        }
                    }
                }
            }
        }

        if (state.isFavoritesOnly) {
            item {
                Spacer(Modifier.padding(32.dp))
            }
        }

        // Settings
        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                icon = {
                    Image(
                        asset = CommunityMaterial.Icon.cmd_cog,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                },
                label = { Text(stringResource(commonR.string.settings)) },
                onClick = onSettingsClicked,
                colors = getFilledTonalButtonColors()
            )
        }
    }
}

@WearThemeCatalog
@Composable
private fun PreviewMain() {
    val entities = listOf(previewEntity1, previewEntity2, previewEntity3)
    WearPreviewTheme {
        MainContent(
            state = MainUiState(
                favoriteEntityIds = listOf(previewEntity1.entityId),
                entities = entities.associateBy { it.entityId },
                cachedFavorites = emptyMap(),
                loadingState = MainViewModel.LoadingState.READY,
                isFavoritesOnly = false,
                areaSections = listOf(NamedEntitySection("Living room", listOf(previewEntity1, previewEntity3))),
                domainSections = listOf(NamedEntitySection("Scenes", listOf(previewEntity2))),
                allDomainSections = listOf(
                    NamedEntitySection("Lights", listOf(previewEntity1)),
                    NamedEntitySection("Scenes", listOf(previewEntity2)),
                    NamedEntitySection("Switches", listOf(previewEntity3))
                )
            ),
            onEntityClicked = { _, _ -> },
            onEntityLongClicked = {},
            onRetryLoadEntitiesClicked = {},
            onSettingsClicked = {},
            onNavigationClicked = { _, _, _ -> },
            isHapticEnabled = false,
            isToastEnabled = false
        )
    }
}
