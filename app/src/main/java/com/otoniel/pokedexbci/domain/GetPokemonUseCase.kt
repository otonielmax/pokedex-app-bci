package com.otoniel.pokedexbci.domain

import com.otoniel.pokedexbci.data.model.BasePokemonResponse
import com.otoniel.pokedexbci.data.model.PokemonResponse

interface GetPokemonUseCase {

    suspend fun invoke(
        limit: Int
    ): BasePokemonResponse<ArrayList<PokemonResponse>>

    suspend fun invoke(
        name: String
    ): PokemonResponse
}