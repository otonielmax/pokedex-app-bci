package com.otoniel.pokedexbci.domain

import com.otoniel.pokedexbci.data.repository.GetPokemonRepository
import com.otoniel.pokedexbci.data.model.BasePokemonResponse
import com.otoniel.pokedexbci.data.model.PokemonResponse
import javax.inject.Inject

class GetPokemonUseCaseImpl @Inject constructor(
    private val repository: GetPokemonRepository
): GetPokemonUseCase {

    override suspend fun invoke(limit: Int): BasePokemonResponse<ArrayList<PokemonResponse>> {
        return repository.getPokemon(limit)
    }

    override suspend fun invoke(name: String): PokemonResponse {
        return repository.getPokemonByName(name)
    }
}