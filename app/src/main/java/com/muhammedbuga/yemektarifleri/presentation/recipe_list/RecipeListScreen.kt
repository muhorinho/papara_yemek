package com.muhammedbuga.yemektarifleri.presentation.recipe_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter

import com.muhammedbuga.yemektarifleri.domain.model.Recipe
import com.muhammedbuga.yemektarifleri.util.NetworkResult

@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    onRecipeClick: (Int) -> Unit
) {
    val recipesState by viewModel.recipesStateFlow.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column {
        TextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Arama yapın...") }
        )

        when (recipesState) {
            is NetworkResult.Loading<*> -> {
                CircularProgressIndicator()
            }
            is NetworkResult.Error<*> -> {
                (recipesState as NetworkResult.Error<*>).message?.let { Text(text = it) }
            }
            is NetworkResult.Success<*> -> {
                RecipeList(
                    recipes = (recipesState as NetworkResult.Success<*>).data as List<Recipe>,
                    onRecipeClick = onRecipeClick
                )
            }

            else -> {}
        }
    }
}

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    onRecipeClick: (Int) -> Unit
) {
    LazyColumn {
        items(recipes) { recipe ->
            RecipeItem(recipe, onRecipeClick)
        }
    }
}

@Composable
fun RecipeItem(
    recipe: Recipe,
    onRecipeClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (recipe.id != 0) {
                    onRecipeClick(recipe.id)
                }
            }
            .padding(8.dp)
    ) {
        Image(
            painter = rememberImagePainter(recipe.image),
            contentDescription = recipe.title,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}