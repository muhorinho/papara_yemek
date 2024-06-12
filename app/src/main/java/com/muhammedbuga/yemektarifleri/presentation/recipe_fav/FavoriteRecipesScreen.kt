package com.muhammedbuga.yemektarifleri.presentation.recipe_fav

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.muhammedbuga.yemektarifleri.presentation.recipe_list.RecipeListViewModel
import com.muhammedbuga.yemektarifleri.presentation.recipe_list.RecipeItem

@Composable
fun FavoriteRecipesScreen(
    onRecipeClick: (Int) -> Unit,
    viewModel: RecipeListViewModel = hiltViewModel()
) {
    val favoritesState by viewModel.favoritesStateFlow.collectAsState(initial = emptyList())

    LazyColumn {
        items(favoritesState) { recipe ->
            RecipeItem(recipe = recipe, onRecipeClick = { onRecipeClick(recipe.id) })
        }
    }
}
