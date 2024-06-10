package com.muhammedbuga.yemektarifleri

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.muhammedbuga.yemektarifleri.presentation.recipe_detail.RecipeDetailScreen
import com.muhammedbuga.yemektarifleri.presentation.recipe_detail.RecipeDetailViewModel
import com.muhammedbuga.yemektarifleri.presentation.recipe_fav.FavoriteRecipesScreen
import com.muhammedbuga.yemektarifleri.presentation.recipe_list.RecipeListScreen
import com.muhammedbuga.yemektarifleri.ui.BottomNavigationBar
import com.muhammedbuga.yemektarifleri.ui.theme.YemekTarifleriTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YemekTarifleriTheme {
                val navController = rememberNavController()
                val viewModel: RecipeDetailViewModel = hiltViewModel() // Get the ViewModel instance
                MainScreen(navController, viewModel) // Pass viewModel to MainScreen
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController, viewModel: RecipeDetailViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        NavHost(navController, startDestination = "recipeList") {
            composable("recipeList") {
                RecipeListScreen(
                    onRecipeClick = { recipeId ->
                        navController.navigate("recipeDetail/$recipeId")
                    }
                )
            }
            composable(
                "recipeDetail/{recipeId}",
                arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
                RecipeDetailScreen(
                    recipeId = recipeId,
                    snackbarHostState = snackbarHostState,
                    onFavoriteClick = { recipe, isFavorite ->
                        viewModel.updateFavoriteStatus(recipe, isFavorite)
                        // Show Snackbar here or pass snackbarHostState to the RecipeDetailScreen
                    }
                )
            }
            composable("favorites") {
                FavoriteRecipesScreen(
                    onRecipeClick = { recipeId ->
                        navController.navigate("recipeDetail/$recipeId")
                    }
                )
            }
        }
    }
}
