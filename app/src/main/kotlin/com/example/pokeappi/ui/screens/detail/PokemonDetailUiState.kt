package com.example.pokeappi.ui.screens.detail

import com.example.pokeappi.domain.model.PokemonDetail

sealed interface PokemonDetailUiState {
    object Loading : PokemonDetailUiState
    data class Success(
        val pokemon: PokemonDetail
    ) : PokemonDetailUiState
    data class Error(val message: String) : PokemonDetailUiState
}