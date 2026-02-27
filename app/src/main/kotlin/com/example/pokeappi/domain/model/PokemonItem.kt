package com.example.pokeappi.domain.model

data class PokemonItem(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val mainType: String? = null
)
