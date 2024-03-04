package com.otoniel.pokedexbci.data.repository

import com.otoniel.pokedexbci.data.model.BasePokemonResponse
import com.otoniel.pokedexbci.data.model.PokemonResponse

interface GetPokemonDataSource {

    suspend fun getPokemon(
        limit: Int
    ): BasePokemonResponse<ArrayList<PokemonResponse>>

    suspend fun getPokemonByName(
        name: String
    ): PokemonResponse
}