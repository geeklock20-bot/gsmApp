package com.example.pokeappi.ui.theme

import androidx.compose.ui.graphics.Color

val Poison = Color(0xFFA33EA1)
val Grass = Color(0xFF7AC74C)
val Fire = Color(0xFFEE8130)
val Water = Color(0xFF6390F0)
val Electric = Color(0xFFF7D02C)
val Ice = Color(0xFF96D9D6)
val Fighting = Color(0xFFC22E28)
val Ground = Color(0xFFE2BF65)
val Flying = Color(0xFFA98FF3)
val Psychic = Color(0xFFF95587)
val Bug = Color(0xFFA6B91A)
val Rock = Color(0xFFB6A136)
val Ghost = Color(0xFF735797)
val Dragon = Color(0xFF6F35FC)
val Dark = Color(0xFF705746)
val Steel = Color(0xFFB7B7CE)
val Fairy = Color(0xFFD685AD)
val Normal = Color(0xFFA8A77A)

fun getPokemonColor(type: String?): Color {
    return when (type?.lowercase()) {
        "grass", "planta" -> Grass
        "fire", "fuego" -> Fire
        "water", "agua" -> Water
        "bug", "bicho" -> Bug
        "normal" -> Normal
        "poison", "veneno" -> Poison
        "electric", "eléctrico" -> Electric
        "ground", "tierra" -> Ground
        "fairy", "hada" -> Fairy
        "fighting", "lucha" -> Fighting
        "psychic", "psíquico" -> Psychic
        "rock", "roca" -> Rock
        "ghost", "fantasma" -> Ghost
        "ice", "hielo" -> Ice
        "dragon", "dragón" -> Dragon
        "dark", "siniestro" -> Dark
        "steel", "acero" -> Steel
        "flying", "volador" -> Flying
        else -> Color.Gray
    }
}
