package io.homeassistant.companion.android.home.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.ToggleButton
import com.mikepenz.iconics.compose.Image
import io.homeassistant.companion.android.common.data.integration.Entity
import io.homeassistant.companion.android.common.data.integration.getIcon
import io.homeassistant.companion.android.home.MainViewModel
import io.homeassistant.companion.android.theme.WearPreviewTheme
import io.homeassistant.companion.android.theme.WearThemeCatalog
import io.homeassistant.companion.android.theme.getToggleButtonColors
import io.homeassistant.companion.android.util.ToggleSwitch
import io.homeassistant.companion.android.util.previewEntity1
import io.homeassistant.companion.android.util.previewEntity2
import io.homeassistant.companion.android.util.previewEntity3
import io.homeassistant.companion.android.views.ExpandableListHeader
import io.homeassistant.companion.android.views.ListHeader
import io.homeassistant.companion.android.views.ThemeLazyColumn
import io.homeassistant.companion.android.views.rememberExpandedStates
import io.homeassistant.companion.android.common.R as commonR

@Composable
fun SetFavoritesView(
    mainViewModel: MainViewModel,
    favoriteEntityIds: List<String>,
    onFavoriteSelected: (entityId: String, isSelected: Boolean) -> Unit
) {
    SetFavoritesContent(
        sections = mainViewModel.entitiesByDomainOrder.mapNotNull { domain ->
            val title = mainViewModel.stringForDomain(domain)
            title?.let { FavoriteSection(domain, it, mainViewModel.entitiesByDomain[domain].orEmpty()) }
        },
        favoriteEntityIds = favoriteEntityIds,
        onFavoriteSelected = onFavoriteSelected
    )
}

data class FavoriteSection(val id: String, val title: String, val entities: List<Entity<*>>)

@Composable
fun SetFavoritesContent(
    sections: List<FavoriteSection>,
    favoriteEntityIds: List<String>,
    onFavoriteSelected: (entityId: String, isSelected: Boolean) -> Unit
) {
    // Remember expanded state of each header
    val expandedStates = rememberExpandedStates(sections.map { it.id })

    ThemeLazyColumn {
        item {
            ListHeader(id = commonR.string.set_favorite)
        }
        for ((domain, title, entities) in sections) {
            if (entities.isNotEmpty()) {
                item {
                    ExpandableListHeader(
                        string = title,
                        key = domain,
                        expandedStates = expandedStates
                    )
                }
                if (expandedStates[domain] == true) {
                    items(entities, key = { it.entityId }) { entity ->
                        FavoriteToggleChip(
                            entity = entity,
                            favoriteEntityIds = favoriteEntityIds,
                            onFavoriteSelected = onFavoriteSelected
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteToggleChip(
    entity: Entity<*>,
    favoriteEntityIds: List<String>,
    onFavoriteSelected: (entityId: String, isSelected: Boolean) -> Unit
) {
    val attributes = entity.attributes as Map<*, *>
    val iconBitmap = entity.getIcon(LocalContext.current)

    val entityId = entity.entityId
    val checked = favoriteEntityIds.contains(entityId)
    ToggleButton(
        checked = checked,
        onCheckedChange = {
            onFavoriteSelected(entityId, it)
        },
        modifier = Modifier
            .fillMaxWidth(),
        icon = {
            Image(
                asset = iconBitmap,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            )
        },
        label = {
            Text(
                text = attributes["friendly_name"].toString(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        selectionControl = { ToggleSwitch(checked) },
        colors = getToggleButtonColors()
    )
}

@WearThemeCatalog
@Composable
private fun PreviewSetFavorites() {
    WearPreviewTheme {
        SetFavoritesContent(
            sections = listOf(
                FavoriteSection("light", "Lights", listOf(previewEntity1)),
                FavoriteSection("switch", "Switches", listOf(previewEntity2, previewEntity3))
            ),
            favoriteEntityIds = listOf(previewEntity1.entityId),
            onFavoriteSelected = { _, _ -> }
        )
    }
}
