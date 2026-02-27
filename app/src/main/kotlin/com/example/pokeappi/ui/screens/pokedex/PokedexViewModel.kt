package com.example.pokeappi.ui.screens.pokedex

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeappi.data.remote.repository.PokemonRepository
import com.example.pokeappi.domain.model.PokemonItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PokedexViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PokemonRepository(application)
    
    private val _allPokemon = MutableStateFlow<List<PokemonItem>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _uiState = MutableStateFlow<PokedexUiState>(PokedexUiState.Loading)
    val uiState: StateFlow<PokedexUiState> = _uiState

    private var offset = 0
    private val limit = 20
    private var isFetching = false
    private var isLastPage = false

    init {
        loadPokemonList()
        
        // Búsqueda avanzada: combina la lista total con el query de búsqueda
        viewModelScope.launch {
            combine(_allPokemon, _searchQuery) { list, query ->
                if (query.isBlank()) {
                    list
                } else {
                    list.filter { pokemon ->
                        pokemon.name.contains(query, ignoreCase = true) ||
                        pokemon.id.toString() == query ||
                        pokemon.mainType?.contains(query, ignoreCase = true) == true
                    }
                }
            }.collect { filteredList ->
                // Evitamos volver a Loading si ya tenemos datos durante la búsqueda
                _uiState.value = PokedexUiState.Success(filteredList)
            }
        }
    }

    fun loadPokemonList() {
        if (isFetching || isLastPage) return
        
        viewModelScope.launch {
            isFetching = true
            
            // Solo mostramos Loading total en la primera carga absoluta
            if (offset == 0 && _allPokemon.value.isEmpty()) {
                _uiState.value = PokedexUiState.Loading
            }
            
            try {
                val newItems = repository.getPokemonList(limit, offset)
                if (newItems.isEmpty()) {
                    isLastPage = true
                } else {
                    // Actualizamos la lista acumulada
                    _allPokemon.value = _allPokemon.value + newItems
                    offset += limit
                }
            } catch (e: Exception) {
                if (offset == 0) {
                    _uiState.value = PokedexUiState.Error(e.message ?: "Error de conexión")
                }
            } finally {
                isFetching = false
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
}
