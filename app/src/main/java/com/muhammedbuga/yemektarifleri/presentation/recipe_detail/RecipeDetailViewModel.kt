package com.muhammedbuga.yemektarifleri.presentation.recipe_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammedbuga.yemektarifleri.domain.model.Recipe
import com.muhammedbuga.yemektarifleri.domain.repository.RecipeRepository
import com.muhammedbuga.yemektarifleri.domain.usecase.GetRecipeDetailUseCase
import com.muhammedbuga.yemektarifleri.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipeDetailUseCase: GetRecipeDetailUseCase,
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipeStateFlow = MutableStateFlow<NetworkResult<Recipe>>(NetworkResult.Loading())
    val recipeStateFlow: StateFlow<NetworkResult<Recipe>> get() = _recipeStateFlow

    fun fetchRecipeById(id: Int) {
        viewModelScope.launch {
            _recipeStateFlow.value = getRecipeDetailUseCase(id)
        }
    }

    fun updateFavoriteStatus(recipe: Recipe, isFavorite: Boolean) {
        viewModelScope.launch {
            // Update the favorite status in the repository
            repository.updateFavoriteStatus(recipe.id, isFavorite)

        }
    }
}
