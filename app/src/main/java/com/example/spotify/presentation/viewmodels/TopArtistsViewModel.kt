package com.example.spotify.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.SpotifyStatsRepository
import com.example.spotify.models.data.ArtistInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopArtistsViewModel @Inject constructor(
    private val statsRepository: SpotifyStatsRepository
) : ViewModel() {

    private val _topArtists = mutableStateOf<List<ArtistInfo>>(emptyList())
    val topArtists: State<List<ArtistInfo>>
        get() = _topArtists

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean>
        get() = _isLoading

    private var nextPageUrl: String? = null

    fun fetchTopArtists() = viewModelScope.launch {
        _isLoading.value = true
        val info = statsRepository.getTopArtists("short_term", 50)
        _isLoading.value = false
        if (info != null) {
            _topArtists.value = info.items
            nextPageUrl = info.next
        }
    }

    fun fetchNextPage() = viewModelScope.launch {
        nextPageUrl?.let {
            val info = statsRepository.getTopArtistsNextPage(it)
            _topArtists.value = (_topArtists.value) + info?.items.orEmpty()
            nextPageUrl = info?.next
        }
    }
}