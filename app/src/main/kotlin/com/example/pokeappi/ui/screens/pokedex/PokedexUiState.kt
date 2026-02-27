package com.example.pokeappi.ui.screens.pokedex
import com.example.pokeappi.domain.model.PokemonItem
sealed interface PokedexUiState {
    data object Loading : PokedexUiState
    data class Success(
        val pokemonList: List<PokemonItem>
    ) : PokedexUiState
    data class Error(val message: String) : PokedexUiState
}