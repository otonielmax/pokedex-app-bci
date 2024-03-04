package com.otoniel.pokedexbci.data.model

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    val name: String? = null,
    val url: String? = null,
    val abilities: ArrayList<AbilityResponse>? = null,
    @SerializedName("base_experience")
    val baseExperience: Int? = null,
    val cries: Crying? = null,
    val forms: ArrayList<Form>? = null,
    val height: Int? = null,
    val id: Int? = null,
    val moves: ArrayList<Move>? = null,
    val sprites: Sprite? = null,
    val stats: ArrayList<Stat>? = null,
    val types: ArrayList<Type>? = null,
    val weight: Int? = null
)

data class AbilityResponse(
    val ability: AbilityContentResponse,
    @SerializedName("is_hidden")
    val isHidden: Boolean,
    val slot: Int
)

data class AbilityContentResponse(
    val name: String,
    val url: String
)

data class Crying(
    val latest: String,
    val legacy: String
)

data class Form(
    val name: String,
    val url: String
)

data class Move(
    val move: MoveContent
)

data class MoveContent(
    val name: String,
    val url: String
)

data class Sprite(
    @SerializedName("front_default")
    val frontDefault: String?,
    @SerializedName("back_default")
    val backDefault: String?
)

data class Stat(
    @SerializedName("base_stat")
    val baseStat: Int,
    val effort: String,
    val stat: StatContent
)

data class StatContent(
    val name: String,
    val url: String
)

data class Type(
    val slot: Int,
    val type: TypeContent
)

data class TypeContent(
    val name: String,
    val url: String
)