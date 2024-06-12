package com.muhammedbuga.yemektarifleri.presentation.recipe_detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.muhammedbuga.yemektarifleri.R
import com.muhammedbuga.yemektarifleri.domain.model.Recipe
import com.muhammedbuga.yemektarifleri.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            RecipeDetailScreen(
                recipeId = intent.getIntExtra("recipeId", 0),
                onFavoriteClick = { recipe, isFavorite -> /* Handle favorite click */ },
                snackbarHostState = remember { SnackbarHostState() }
            )
        }
    }
}

@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    onFavoriteClick: (Recipe, Boolean) -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val recipeState by viewModel.recipeStateFlow.collectAsState()
    var isFavorite by remember { mutableStateOf(false) }
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
                    snackbarHostState = snackbarHostState,
                    onFavoriteButtonClick = {
                        isFavorite = !isFavorite
                        onFavoriteClick(recipe, isFavorite)
                        showSnackbar = true
                    }
                )

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
    isFavorite: Boolean,
    snackbarHostState: SnackbarHostState,
    onFavoriteButtonClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp)
                )
                IconButton(
                    onClick = onFavoriteButtonClick
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }
            Image(
                painter = rememberAsyncImagePainter(recipe.image),
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = recipe.summary?.replace(Regex("<[^>]*>"), "") ?: "No summary available")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = recipe.instructions?.replace(Regex("<[^>]*>"), "") ?: "No instructions available")
        }
    }
}
