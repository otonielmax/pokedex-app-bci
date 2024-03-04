package com.otoniel.pokedexbci.data.model

data class BasePokemonResponse<T>(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: T
)
