package com.otoniel.pokedexbci.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otoniel.pokedexbci.data.model.PokemonResponse
import com.otoniel.pokedexbci.domain.GetPokemonUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetPokemonViewModel @Inject constructor(
    private val useCase: GetPokemonUseCaseImpl
): ViewModel() {

    companion object {
        const val POKEDEX_NACIONAL = 1000
        const val POKEDEX_KANTO = 151
    }

    private val _response = MutableLiveData<ArrayList<PokemonResponse>>()
    val response: LiveData<ArrayList<PokemonResponse>> get() = _response

    private val _pokemonResponse = MutableLiveData<PokemonResponse?>()
    val pokemonResponse: LiveData<PokemonResponse?> get() = _pokemonResponse

    private val _filterPokemon = MutableLiveData<ArrayList<PokemonResponse>>()
    val filterPokemon: LiveData<ArrayList<PokemonResponse>> get() = _filterPokemon

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    fun getPokemons() {
        viewModelScope.launch {
            val baseResponse = useCase.invoke(POKEDEX_KANTO)
            baseResponse.results.let { results ->
                _response.value = results
                _filterPokemon.value = results
            }
        }
    }

    fun getPokemonByName(name: String) {
        viewModelScope.launch {
            _pokemonResponse.value = useCase.invoke(name)
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        if (text.isBlank()) {
            _filterPokemon.value = _response.value
        } else {
            _filterPokemon.value = response.value?.filter { pokemon ->
                pokemon.name?.uppercase()?.contains(searchText.value.trim().uppercase()) == true
            } as ArrayList<PokemonResponse>?
        }
    }
}