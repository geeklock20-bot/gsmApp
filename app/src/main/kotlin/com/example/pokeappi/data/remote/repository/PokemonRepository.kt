package com.example.pokeappi.data.remote.repository

import android.content.Context
import com.example.pokeappi.data.local.PokemonDatabase
import com.example.pokeappi.data.local.PokemonEntity
import com.example.pokeappi.data.remote.RetrofitClient
import com.example.pokeappi.domain.model.PokemonDetail
import com.example.pokeappi.domain.model.PokemonItem
import com.example.pokeappi.domain.model.Stat
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PokemonRepository(context: Context) {

    private val api = RetrofitClient.api
    private val dao = PokemonDatabase.getDatabase(context).pokemonDao()

    private val typeTranslations = mapOf(
        "normal" to "Normal", "fire" to "Fuego", "water" to "Agua",
        "electric" to "Eléctrico", "grass" to "Planta", "ice" to "Hielo",
        "fighting" to "Lucha", "poison" to "Veneno", "ground" to "Tierra",
        "flying" to "Volador", "psychic" to "Psíquico", "bug" to "Bicho",
        "rock" to "Roca", "ghost" to "Fantasma", "dragon" to "Dragón",
        "dark" to "Siniestro", "steel" to "Acero", "fairy" to "Hada"
    )

    suspend fun getPokemonList(limit: Int = 20, offset: Int = 0): List<PokemonItem> = coroutineScope {
        val response = api.getPokemonList(limit, offset)
        response.results.map { entry ->
            async {
                val id = entry.url.trimEnd('/').split("/").last().toInt()
                val detail = try { api.getPokemonDetail(id) } catch (e: Exception) { null }
                val mainType = detail?.types?.firstOrNull()?.type?.name
                PokemonItem(
                    id = id,
                    name = entry.name.replaceFirstChar { it.uppercase() },
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
                    mainType = typeTranslations[mainType] ?: mainType
                )
            }
        }.awaitAll()
    }

    suspend fun getPokemonDetail(id: Int): PokemonDetail = coroutineScope {
        val detailDeferred = async { api.getPokemonDetail(id) }
        val speciesDeferred = async { api.getPokemonSpecies(id) }

        val detail = detailDeferred.await()
        val species = speciesDeferred.await()

        val spanishName = species.names.firstOrNull { it.language.name == "es" }?.name
            ?: detail.name.replaceFirstChar { it.uppercase() }

        PokemonDetail(
            id = detail.id,
            name = spanishName,
            height = detail.height,
            weight = detail.weight,
            types = detail.types.map { typeSlot ->
                typeTranslations[typeSlot.type.name] ?: typeSlot.type.name.replaceFirstChar { it.uppercase() }
            },
            imageUrl = detail.sprites.other?.officialArtwork?.frontDefault ?: detail.sprites.frontDefault ?: "",
            stats = detail.stats.map { Stat(it.stat.name, it.baseStat) },
            genus = species.genera.firstOrNull { it.language.name == "es" }?.genus ?: "",
            description = species.flavorTextEntries.lastOrNull { it.language.name == "es" }?.flavorText?.replace("\n", " ") ?: ""
        )
    }

    fun getFavorites(): Flow<List<PokemonItem>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { PokemonItem(it.id, it.name, it.imageUrl, it.mainType) }
        }
    }

    suspend fun toggleFavorite(pokemon: PokemonItem) {
        val entity = PokemonEntity(pokemon.id, pokemon.name, pokemon.imageUrl, pokemon.mainType)
        if (dao.isFavorite(pokemon.id)) {
            dao.deleteFavorite(entity)
        } else {
            dao.insertFavorite(entity)
        }
    }

    suspend fun isFavorite(id: Int) = dao.isFavorite(id)
}
