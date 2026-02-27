package com.example.pokeappi.ui.screens.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.pokeappi.data.remote.repository.PokemonRepository
import com.example.pokeappi.domain.model.PokemonItem
import kotlinx.coroutines.flow.Flow

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PokemonRepository(application)
    val favorites: Flow<List<PokemonItem>> = repository.getFavorites()
}
