package com.example.spotify.presentation.playlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.spotify.R
import com.example.spotify.domain.models.ArtistInfo
import com.example.spotify.domain.models.TrackInfo
import com.example.spotify.presentation.components.AppBar
import com.example.spotify.presentation.models.CustomPlaylistScreenState
import com.example.spotify.presentation.models.GenreModel
import com.example.spotify.presentation.models.Search
import com.example.spotify.presentation.models.Selected

@Composable
fun PlaylistSuccessScreen(
    state: CustomPlaylistScreenState.Success,
    onBackClick: () -> Unit,
    onCreatePlaylist: () -> Unit
) {
    val selectedSize = state.genres.selected.list.value.size +
            state.artists.selected.list.value.size +
            state.tracks.selected.list.value.size

    Scaffold(
        topBar = {
            AppBar(title = stringResource(id = R.string.custom_playlist)) { onBackClick.invoke() }
        },
        floatingActionButton = {
            Button(
                onClick = { onCreatePlaylist.invoke() },
                enabled = selectedSize != 0
            ) {
                Text("Create Playlist")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Choose your preferences to create custom playlist",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            SelectedList(
                artists = state.artists.selected,
                tracks = state.tracks.selected,
                genres = state.genres.selected,
            )

            if (selectedSize < 5) {
                SelectionOptions(
                    selectedOption = state.selectionButton.type.value,
                    onSelect = { state.selectionButton.onChange.invoke(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                when (state.selectionButton.type.value) {
                    SelectionOption.GENRE -> {
                        GenreSelection(state.genres) { genre ->
                            state.genres.selected.onSelectedChanged(genre)
                        }
                    }

                    SelectionOption.ARTIST -> {
                        ArtistSearchField(
                            selected = state.artists.selected,
                            search = state.artists.search
                        )
                    }

                    SelectionOption.TRACK -> {
                        TrackSearchField(
                            selected = state.tracks.selected,
                            search = state.tracks.search
                        )
                    }

                    null -> {}
                }
            } else {
                Text(
                    "Maximum of 5 selections reached.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun TrackSearchField(
    search: Search<TrackInfo>,
    selected: Selected<TrackInfo>
) {
    SearchField(
        label = "Search Tracks",
        query = search.query.value,
        onQueryChange = { search.onSearch.invoke(it) },
        onDelete = { search.onSearch.invoke("") }
    )
    TrackSelection(
        selectedTracks = search.searchResults.value
            ?: search.placeholder,
        selected = selected.list.value.map { it.id }
    ) { track ->
        selected.onSelectedChanged(track)
    }
}


@Composable
private fun ArtistSearchField(
    search: Search<ArtistInfo>,
    selected: Selected<ArtistInfo>
) {
    SearchField(
        label = "Search Artists",
        query = search.query.value,
        onQueryChange = { search.onSearch.invoke(it) },
        onDelete = { search.onSearch.invoke("") }
    )
    ArtistSelection(
        selectedArtists = search.searchResults.value
            ?: search.placeholder,
        selected = selected.list.value.map { it.id }
    ) { artist ->
       selected.onSelectedChanged(artist)
    }
}


@Composable
private fun SelectedList(
    tracks: Selected<TrackInfo>,
    artists: Selected<ArtistInfo>,
    genres: Selected<String>
) {
    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        genres.list.value.takeIf { it.isNotEmpty() }?.let { genreList ->
            Text(
                text = "genres",
                style = MaterialTheme.typography.bodyMedium
            )
            genreList.forEach { genre ->
                SelectedItem(label = genre) {
                    genres.onSelectedChanged(genre)
                }
            }
        }
        artists.list.value.takeIf { it.isNotEmpty() }?.let { artistList ->
            Text(
                text = "artists",
                style = MaterialTheme.typography.bodyMedium
            )
            artistList.forEach { artist ->
                SelectedItem(label = artist.name) {
                    artists.onSelectedChanged(artist)
                }

            }
        }
        tracks.list.value.takeIf { it.isNotEmpty() }?.let { tracksList ->
            Text(
                text = "tracks",
                style = MaterialTheme.typography.bodyMedium
            )
            tracksList.forEach { track ->
                SelectedItem(label = track.name) {
                    tracks.onSelectedChanged(track)
                }

            }
        }
    }
}

@Composable
private fun SelectedItem(label: String, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(8.5f),
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(
                modifier = Modifier.weight(1.5f),
                onClick = { onRemove() }
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove"
                )
            }
        }
    }
}

@Composable
private fun SelectionOptions(
    selectedOption: SelectionOption?,
    onSelect: (SelectionOption) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        SelectionOption.entries.forEach { option ->
            Button(
                onClick = { onSelect(option) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedOption == option) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    contentColor = if (selectedOption == option) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(option.label)
            }
        }
    }
}

@Composable
private fun SearchField(
    label: String,
    query: String,
    onQueryChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = { onQueryChange(it) },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onDelete) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    )
}

@Composable
private fun GenreSelection(
    selectedGenres: GenreModel,
    onToggle: (String) -> Unit
) {
    LazyColumn {
        item {
            Text(
                text = "top",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        items(selectedGenres.top) { genre ->
            Item(genre, selectedGenres.selected.list.value.contains(genre)) {
                onToggle(genre)
            }
        }
        item {
            Text(
                text = "a-z",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        items(selectedGenres.rest) { genre ->
            Item(genre, selectedGenres.selected.list.value.contains(genre)) {
                onToggle(genre)
            }
        }
    }
}

@Composable
private fun ArtistSelection(
    selectedArtists: List<ArtistInfo>,
    selected: List<String>,
    onToggle: (ArtistInfo) -> Unit
) {
    if (selectedArtists.isNotEmpty()) {
        LazyColumn {
            items(selectedArtists) { artist ->
                Item(artist.name, selected.contains(artist.id)) {
                    onToggle.invoke(artist)
                }
            }
        }
    } else {
        Text("Ничего не найдено")
    }
}

@Composable
private fun TrackSelection(
    selectedTracks: List<TrackInfo>,
    selected: List<String>,
    onToggle: (TrackInfo) -> Unit
) {
    if (selectedTracks.isNotEmpty()) {
        LazyColumn {
            items(selectedTracks) { track ->
                Item(track.name, selected.contains(track.id)) {
                    onToggle.invoke(track)
                }
            }
        }
    } else {
        Text("Ничего не найдено")
    }
}

@Composable
private fun Item(name: String, isSelected: Boolean, onToggle: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onToggle() },
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

enum class SelectionOption(val label: String) {
    GENRE("Genre"),
    ARTIST("Artist"),
    TRACK("Track")
}
