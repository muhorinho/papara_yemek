package com.muhammedbuga.yemektarifleri.presentation.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammedbuga.yemektarifleri.domain.model.Recipe
import com.muhammedbuga.yemektarifleri.domain.repository.RecipeRepository
import com.muhammedbuga.yemektarifleri.domain.usecase.GetRecipeListUseCase
import com.muhammedbuga.yemektarifleri.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val getRecipeListUseCase: GetRecipeListUseCase
) : ViewModel() {

    private val _recipesStateFlow = MutableStateFlow<NetworkResult<List<Recipe>>>(NetworkResult.Loading())
    val recipesStateFlow: StateFlow<NetworkResult<List<Recipe>>> get() = _recipesStateFlow

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _favoritesStateFlow = MutableStateFlow<List<Recipe>>(emptyList())
    val favoritesStateFlow: StateFlow<List<Recipe>> = _favoritesStateFlow

    init {
        fetchRecipes()
        searchRecipes("")
    }

    private fun fetchRecipes() {
        viewModelScope.launch {
            _recipesStateFlow.value = repository.getRecipes()
            fetchFavoriteRecipes()
        }
    }


    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchRecipes(query)
    }

    private fun searchRecipes(query: String) {
        viewModelScope.launch {
            _recipesStateFlow.value = getRecipeListUseCase(query, 100)
        }
    }

    private suspend fun fetchFavoriteRecipes() {
        repository.getFavoriteRecipesFlow().collect { favorites ->
            _favoritesStateFlow.value = favorites
        }
    }

}
