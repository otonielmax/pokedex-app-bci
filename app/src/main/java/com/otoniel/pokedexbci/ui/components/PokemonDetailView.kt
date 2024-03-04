package com.otoniel.pokedexbci.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.otoniel.pokedexbci.BuildConfig
import com.otoniel.pokedexbci.R
import com.otoniel.pokedexbci.data.model.AbilityResponse
import com.otoniel.pokedexbci.data.model.Move
import com.otoniel.pokedexbci.data.model.PokemonResponse
import com.otoniel.pokedexbci.data.model.Stat
import com.otoniel.pokedexbci.ui.viewmodel.GetPokemonViewModel

@Composable
fun PokemonDetailView(
    getPokemonViewModel: GetPokemonViewModel,
    navHostController: NavHostController
) {
    val pokemon: PokemonResponse? by getPokemonViewModel.pokemonResponse.observeAsState(initial = null)
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        PokemonDetailImageView(pokemon = pokemon)
        pokemon?.let { pokemonCurrent ->
            Text(text = pokemonCurrent.name ?: "", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text(text = "Habilidades", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            AbilitiesHorizontalView(pokemonCurrent.abilities ?: arrayListOf())
            Text(text = "Movimientos", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            MovesHorizontalView(pokemonCurrent.moves ?: arrayListOf())
            Text(text = "Estadisticas bases", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            StatsHorizontalView(pokemonCurrent.stats ?: arrayListOf())
            Text(text = "Sprites", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            PokemonSpriteImageView(pokemonCurrent)

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonDetailImageView(pokemon: PokemonResponse?) {
    pokemon?.let { item ->
        GlideImage(
            model = "${BuildConfig.URL_POKEMON_IMAGE}${item.id}.png",
            contentDescription = item.name,
            loading = placeholder(R.drawable.ic_pokeball),
            failure = placeholder(R.drawable.ic_pokeball),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonSpriteImageView(pokemon: PokemonResponse?) {
    pokemon?.let { item ->
        Row {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Frontal")
                GlideImage(
                    model = item.sprites?.frontDefault,
                    contentDescription = item.name,
                    loading = placeholder(R.drawable.ic_pokeball),
                    failure = placeholder(R.drawable.ic_pokeball),
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Trasera")
                GlideImage(
                    model = item.sprites?.backDefault,
                    contentDescription = item.name,
                    loading = placeholder(R.drawable.ic_pokeball),
                    failure = placeholder(R.drawable.ic_pokeball),
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
            }
        }
    }
}

@Composable
fun AbilitiesHorizontalView(abilities: ArrayList<AbilityResponse>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        itemsIndexed(abilities) { index, ability ->
            AbilityItemView(ability = ability, position = index)
        }
    }
}

@Composable
fun AbilityItemView(ability: AbilityResponse, position: Int = 0) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier
            .padding(12.dp)
    ) {
        Row() {
            Spacer(Modifier.width(4.dp))
            Text(text = ability.ability.name)
            Spacer(Modifier.width(4.dp))
        }
    }
}

@Composable
fun MovesHorizontalView(moves: ArrayList<Move>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        itemsIndexed(moves) { index, move ->
            MoveItemView(move = move, position = index)
        }
    }
}

@Composable
fun MoveItemView(move: Move, position: Int = 0) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier
            .padding(12.dp)
    ) {
        Row() {
            Spacer(Modifier.width(4.dp))
            Text(text = move.move.name)
            Spacer(Modifier.width(4.dp))
        }
    }
}

@Composable
fun StatsHorizontalView(stats: ArrayList<Stat>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        itemsIndexed(stats) { index, stat ->
            StatItemView(stat = stat, position = index)
        }
    }
}

@Composable
fun StatItemView(stat: Stat, position: Int = 0) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier
            .padding(12.dp)
    ) {
        Row() {
            Spacer(Modifier.width(4.dp))
            Text(text = "${stat.baseStat} ${stat.stat.name}")
            Spacer(Modifier.width(4.dp))
        }
    }
}