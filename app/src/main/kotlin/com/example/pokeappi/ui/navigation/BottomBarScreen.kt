package com.example.pokeappi.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Pokedex : BottomBarScreen(
        route = "pokedex",
        title = "Pokedex",
        icon = Icons.Default.List
    )

    object Favorites : BottomBarScreen(
        route = "favorites",
        title = "Favoritos",
        icon = Icons.Default.Favorite
    )
}
