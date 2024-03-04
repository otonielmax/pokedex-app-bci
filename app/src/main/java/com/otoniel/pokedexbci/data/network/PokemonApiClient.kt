package com.otoniel.pokedexbci.data.network

import com.otoniel.pokedexbci.data.model.BasePokemonResponse
import com.otoniel.pokedexbci.data.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApiClient {

    @GET("pokemon")
    suspend fun getPokemons(
        @Query("limit") limit: Int = 0,
        @Query("offset") offset: Int = 0,
    ): BasePokemonResponse<ArrayList<PokemonResponse>>

    @GET("pokemon/{pokemonName}")
    suspend fun getPokemonsByName(
        @Path(value = "pokemonName", encoded = false) pokemonName: String
    ): PokemonResponse
}