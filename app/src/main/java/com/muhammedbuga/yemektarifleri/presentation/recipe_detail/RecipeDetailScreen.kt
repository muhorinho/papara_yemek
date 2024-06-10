package com.muhammedbuga.yemektarifleri.presentation.recipe_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.muhammedbuga.yemektarifleri.domain.model.Recipe
import com.muhammedbuga.yemektarifleri.util.NetworkResult

@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    snackbarHostState: SnackbarHostState,
    onFavoriteClick: (Recipe, Boolean) -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val recipeState by viewModel.recipeStateFlow.collectAsState()
    var isFavorite by remember { mutableStateOf(false) } // Hoisted state
    var showSnackbar by remember { mutableStateOf(false) }

    LaunchedEffect(recipeId) {
        if (recipeId != 0) {
            viewModel.fetchRecipeById(recipeId)
        }
    }

    LaunchedEffect(recipeState) {
        if (recipeState is NetworkResult.Success) {
            isFavorite = recipeState.data?.isFavorite ?: false
        }
    }

    when (recipeState) {
        is NetworkResult.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is NetworkResult.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = (recipeState as NetworkResult.Error).message ?: "Unknown Error")
            }
        }
        is NetworkResult.Success -> {
            val recipe = (recipeState as NetworkResult.Success).data
            if (recipe != null) {
                RecipeDetail(
                    recipe = recipe,
                    isFavorite = isFavorite,
                    onFavoriteButtonClick = {
                        isFavorite = !isFavorite // Update local state immediately
                        showSnackbar = true       // Trigger Snackbar
                        onFavoriteClick(recipe, isFavorite)
                    }
                )

                // LaunchedEffect for Snackbar (only for timing)
                LaunchedEffect(showSnackbar) {
                    if (showSnackbar) {
                        snackbarHostState.showSnackbar(
                            message = if (isFavorite) "Favorilere eklendi" else "Favorilerden çıkarıldı"
                        )
                        showSnackbar = false // Reset the flag
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeDetail(
    recipe: Recipe,
    onFavoriteButtonClick: () -> Unit,
    isFavorite: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(recipe.image),
            contentDescription = recipe.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = recipe.title, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            val summary = if ((recipe.summary?.split(' ')?.size ?: 0) > 50) {
                recipe.summary?.split(' ')?.take(50)?.joinToString(" ") + "..."
            } else {
                recipe.summary ?: "No summary available"
            }
            Text(text = summary)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onFavoriteButtonClick) {
                Text(if (isFavorite) "Favorilerden Çıkar" else "Favorilere Ekle")
            }
        }
    }
}
