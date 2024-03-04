package com.otoniel.pokedexbci.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.otoniel.pokedexbci.BuildConfig
import com.otoniel.pokedexbci.R
import com.otoniel.pokedexbci.data.model.PokemonResponse
import com.otoniel.pokedexbci.data.model.Routes
import com.otoniel.pokedexbci.getPokemonMocks
import com.otoniel.pokedexbci.ui.theme.PokedexBCITheme
import com.otoniel.pokedexbci.ui.theme.Purple40
import com.otoniel.pokedexbci.ui.theme.Purple80
import com.otoniel.pokedexbci.ui.viewmodel.GetPokemonViewModel

@Preview(
    name = "PokedexView",
    showBackground = true
)
@Composable
fun PokedexViewPreview() {
    PokedexBCITheme {
//        PokedexView()
    }
}

@Composable
fun PokedexView(getPokemonViewModel: GetPokemonViewModel, navHostController: NavHostController) {
    val context = LocalContext.current
    val pokemons: ArrayList<PokemonResponse> by getPokemonViewModel.filterPokemon.observeAsState(initial = arrayListOf())
    PokemonListView(pokemons) { pokemon ->
        pokemon.name?.let { name ->
            navHostController.navigate(Routes.Pokemon.createRoute(name))
        } ?: run {
            Toast.makeText(context, "Nombre de pokemon no valido", Toast.LENGTH_LONG).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexAppBar(
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onActiveChanged: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    getPokemonViewModel: GetPokemonViewModel,
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    Column {
        PokedexTopAppBar(
            onBackClick = onBackClick,
            scrollBehavior = scrollBehavior
        )
        EmbeddedSearchBar(
            onQueryChange = onQueryChange,
            isSearchActive = isSearchActive,
            onActiveChanged = onActiveChanged,
            onSearch = onSearch,
            getPokemonViewModel = getPokemonViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexTopAppBar(
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = { Text("Pokedex") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Purple40,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        scrollBehavior = scrollBehavior,
    )
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EmbeddedSearchBar(
    onQueryChange: (String) -> Unit,
    isSearchActive: Boolean,
    onActiveChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: ((String) -> Unit)? = null,
    getPokemonViewModel: GetPokemonViewModel,
) {
    val pokemons: ArrayList<PokemonResponse> by getPokemonViewModel.filterPokemon.observeAsState(initial = arrayListOf())

    var searchQuery by rememberSaveable { mutableStateOf("") }

    val activeChanged: (Boolean) -> Unit = { active ->
        searchQuery = ""
        onQueryChange("")
        onActiveChanged(active)
    }
    SearchBar(
        query = searchQuery,
        onQueryChange = { query ->
            searchQuery = query
            onQueryChange(query)
        },
        onSearch = onSearch ?: { activeChanged(false) },
        active = isSearchActive,
        onActiveChange = activeChanged,
        modifier = if (isSearchActive) {
            modifier
                .animateContentSize(spring(stiffness = Spring.StiffnessHigh))
        } else {
            modifier
                .padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 12.dp)
                .fillMaxWidth()
                .animateContentSize(spring(stiffness = Spring.StiffnessHigh))
        },
        placeholder = { Text("Buscar", color = Color.White) },
        leadingIcon = {
            if (isSearchActive) {
                IconButton(
                    onClick = { activeChanged(false) },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        },
        trailingIcon = if (isSearchActive && searchQuery.isNotEmpty()) {
            {
                IconButton(
                    onClick = {
                        searchQuery = ""
                        onQueryChange("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                    )
                }
            }
        } else {
            null
        },
        colors = SearchBarDefaults.colors(
            containerColor = Purple40,
            inputFieldColors = TextFieldDefaults.colors(
                cursorColor = Color.White,
                focusedTextColor = Color.White
            )
        ),
        tonalElevation = 0.dp,
        windowInsets = if (isSearchActive) {
            SearchBarDefaults.windowInsets
        } else {
            WindowInsets(0.dp)
        }
    ) {
        // Search suggestions or results
        LazyColumn {
            items(pokemons) { pokemon ->
                Text(
                    text = pokemon.name ?: "",
                    color = Color.White,
                    modifier = Modifier
                        .padding(
                            start = 8.dp,
                            top = 4.dp,
                            end = 8.dp,
                            bottom = 4.dp
                        )
                        .clickable {
                            onActiveChanged(false)
                            if (onSearch != null) {
                                onSearch(pokemon.name ?: "")
                            }
                        }
                )
            }
        }
    }
}

@Preview(
    name = "PokemonListView",
    showBackground = true
)
@Composable
fun PokemonListViewPreview() {
    PokedexBCITheme {
        PokemonListView(getPokemonMocks()) {

        }
    }
}

@Composable
fun PokemonListView(pokemons: ArrayList<PokemonResponse>, function: (pokemonSelected: PokemonResponse) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        itemsIndexed(pokemons) { index, pokemon ->
            PokemonItemView(pokemon = pokemon, position = index) { pokemonSelected, position ->
                function(pokemonSelected)
            }
        }
    }
}

@Composable
fun PokemonItemView(pokemon: PokemonResponse, position: Int = 0, onItemSelected: (PokemonResponse, Int) -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemSelected(pokemon, position) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(Modifier.width(4.dp))
            Text(text = "#${(position + 1)}")
            Spacer(Modifier.width(12.dp))
            pokemon.name?.let { name ->
                Text(text = name)
            } ?: run {
                Text(text = "Pokemon sin nombre")
            }
            Spacer(Modifier.weight(1f))
            PokemonImageView(pokemon = pokemon, position = position)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonImageView(pokemon: PokemonResponse, position: Int) {
    GlideImage(
        model = "${BuildConfig.URL_POKEMON_IMAGE}${(pokemon.url?.split("/")?.get(6)?.toInt()) ?: 1}.png",
        contentDescription = pokemon.name,
        loading = placeholder(R.drawable.ic_pokeball),
        failure = placeholder(R.drawable.ic_pokeball),
        modifier = Modifier
            .size(60.dp)
            .padding(all = 4.dp)
    )
}