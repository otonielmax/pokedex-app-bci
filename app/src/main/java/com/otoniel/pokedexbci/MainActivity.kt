package com.otoniel.pokedexbci

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.otoniel.pokedexbci.data.model.PokemonResponse
import com.otoniel.pokedexbci.data.model.Routes
import com.otoniel.pokedexbci.ui.components.PokedexAppBar
import com.otoniel.pokedexbci.ui.components.PokedexView
import com.otoniel.pokedexbci.ui.components.PokemonDetailView
import com.otoniel.pokedexbci.ui.theme.PokedexBCITheme
import com.otoniel.pokedexbci.ui.viewmodel.GetPokemonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val getPokemonViewModel: GetPokemonViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexBCITheme {

                var isTopBarActive by rememberSaveable { mutableStateOf(true) }
                var isSearchActive by rememberSaveable { mutableStateOf(false) }
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

                Scaffold (
                    topBar = {
                        if (isTopBarActive) {
                            PokedexAppBar(
                                onQueryChange = getPokemonViewModel::onSearchTextChange,
                                isSearchActive = isSearchActive,
                                onActiveChanged = { isSearchActive = it },
                                onSearch = getPokemonViewModel::onSearchTextChange,
                                getPokemonViewModel = getPokemonViewModel,
                                onBackClick = {},
                                scrollBehavior = scrollBehavior
                            )
                        }
                    }
                ) { padding ->
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navigationController = rememberNavController()
                        NavHost(navController = navigationController, startDestination = Routes.Pokedex.route) {
                            composable(Routes.Pokedex.route) {
                                isTopBarActive = true
                                PokedexView(getPokemonViewModel, navigationController)
                            }
                            composable(Routes.Pokemon.route, arguments = listOf(
                                navArgument("name") { type = NavType.StringType }
                            )) { backStackEntry ->
                                val name = backStackEntry.arguments?.getString("name")
                                name?.let {
                                    isTopBarActive = false
                                    getPokemonByName(name)
                                    PokemonDetailView(getPokemonViewModel, navigationController)
                                } ?: run {
                                    Toast.makeText(applicationContext, "No se ha pasado un pokemon valido", Toast.LENGTH_LONG).show()
                                    navigationController.navigate(Routes.Pokedex.route)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getPokemons()
    }

    private fun getPokemons() {
        getPokemonViewModel.getPokemons()
    }

    private fun getPokemonByName(name: String) {
        getPokemonViewModel.getPokemonByName(name)
    }
}

fun getPokemonMocks(): ArrayList<PokemonResponse> {
    return arrayListOf(
        PokemonResponse("Bulbasaur", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"),
        PokemonResponse("Bulbasaur", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"),
        PokemonResponse("Bulbasaur", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png")
    )
}

