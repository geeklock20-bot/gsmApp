package com.example.pokeappi.ui.screens.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeappi.data.remote.repository.PokemonRepository
import com.example.pokeappi.domain.model.PokemonDetail
import com.example.pokeappi.domain.model.PokemonItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PokemonRepository(application)
    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun loadPokemon(id: Int) {
        viewModelScope.launch {
            _uiState.value = PokemonDetailUiState.Loading
            try {
                val detail = repository.getPokemonDetail(id)
                _uiState.value = PokemonDetailUiState.Success(detail)
                _isFavorite.value = repository.isFavorite(id)
            } catch (e: Exception) {
                _uiState.value = PokemonDetailUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun toggleFavorite(detail: PokemonDetail) {
        viewModelScope.launch {
            val pokemonItem = PokemonItem(detail.id, detail.name, detail.imageUrl, detail.types.firstOrNull())
            repository.toggleFavorite(pokemonItem)
            _isFavorite.value = !_isFavorite.value
        }
    }
}
