package com.otoniel.pokedexbci.data.repository

import com.otoniel.pokedexbci.data.model.BasePokemonResponse
import com.otoniel.pokedexbci.data.model.PokemonResponse
import com.otoniel.pokedexbci.data.network.PokemonApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPokemonRepository @Inject constructor(
    val apiClient: PokemonApiClient,
): GetPokemonDataSource {

    override suspend fun getPokemon(limit: Int): BasePokemonResponse<ArrayList<PokemonResponse>> {
        return withContext(Dispatchers.IO) {
            apiClient.getPokemons(limit)
        }
    }

    override suspend fun getPokemonByName(name: String): PokemonResponse {
        return withContext(Dispatchers.IO) {
            apiClient.getPokemonsByName(name)
        }
    }
}