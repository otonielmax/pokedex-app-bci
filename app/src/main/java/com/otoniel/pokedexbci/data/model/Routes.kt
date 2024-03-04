package com.otoniel.pokedexbci.data.model

sealed class Routes(val route: String) {
    object Pokedex: Routes("pokedex")
    object Pokemon: Routes("pokemon/{name}") {
        fun createRoute(name: String) = "pokemon/$name"
    }
}