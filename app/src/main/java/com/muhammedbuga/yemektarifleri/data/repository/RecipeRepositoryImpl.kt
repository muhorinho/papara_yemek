package com.muhammedbuga.yemektarifleri.data.repository

import android.util.Log
import com.muhammedbuga.yemektarifleri.data.local.RecipeDao
import com.muhammedbuga.yemektarifleri.data.remote.RecipeApi
import com.muhammedbuga.yemektarifleri.domain.model.Recipe
import com.muhammedbuga.yemektarifleri.domain.repository.RecipeRepository
import com.muhammedbuga.yemektarifleri.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeApi: RecipeApi
) : RecipeRepository {

    override suspend fun getRecipes(): NetworkResult<List<Recipe>> = withContext(Dispatchers.IO) {
        try {
            // Önbellekteki tarifleri kontrol et
            val cachedRecipes = recipeDao.getAllRecipesFlow().firstOrNull()
            if (cachedRecipes != null && cachedRecipes.isNotEmpty()) {
                Log.d("RecipeRepository", "Recipes fetched from cache")
                return@withContext NetworkResult.Success(cachedRecipes)
            }

            // API'den tarifleri getir
            Log.d("RecipeRepository", "Fetching recipes from API")
            val response = recipeApi.getRecipes()

            if (response.isSuccessful) {
                Log.d("RecipeRepository", "API response successful")
                val recipes = response.body()?.map { dto ->
                    Recipe(
                        id = dto.id,
                        title = dto.title,
                        image = dto.image,
                        summary = dto.summary, // Yeni alan
                        instructions = dto.instructions // Yeni alan
                    )
                } ?: emptyList()

                // Tarifleri veritabanına kaydet
                recipeDao.insertAllRecipes(recipes)
                return@withContext NetworkResult.Success(recipes)
            } else {
                val errorMessage = "API request failed with code: ${response.code()}, message: ${response.message()}"
                Log.e("RecipeRepository", errorMessage)
                return@withContext NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error fetching recipes: ${e.message}", e)
            return@withContext NetworkResult.Error("Error fetching recipes: ${e.message}")
        }
    }

    override suspend fun getRecipeById(id: Int): NetworkResult<Recipe> = withContext(Dispatchers.IO) {
        try {
            // Veritabanından tarifi getirmeye çalış
            val recipe = recipeDao.getRecipeById(id)
            if (recipe != null) {
                Log.d("RecipeRepository", "Recipe with id $id fetched from cache")
                return@withContext NetworkResult.Success(recipe)
            }

            // API'den tarifi getir
            Log.d("RecipeRepository", "Fetching recipe with id $id from API")
            val response = recipeApi.getRecipeInformation(id)

            if (response.isSuccessful) {
                Log.d("RecipeRepository", "API response successful")
                val recipeFromApi = response.body()
                if (recipeFromApi != null) {
                    recipeDao.insertAllRecipes(listOf(recipeFromApi))
                    return@withContext NetworkResult.Success(recipeFromApi)
                } else {
                    val errorMessage = "Recipe with id $id not found in API response"
                    Log.e("RecipeRepository", errorMessage)
                    return@withContext NetworkResult.Error(errorMessage)
                }
            } else {
                val errorMessage = "API request failed with code: ${response.code()}, message: ${response.message()}"
                Log.e("RecipeRepository", errorMessage)
                return@withContext NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error fetching recipe with id $id: ${e.message}", e)
            return@withContext NetworkResult.Error("Error fetching recipe with id $id: ${e.message}")
        }
    }

    override suspend fun searchRecipes(query: String, number: Int): NetworkResult<List<Recipe>> = withContext(Dispatchers.IO) {
        try {
            Log.d("RecipeRepository", "Searching recipes with query: $query")
            val response = recipeApi.searchRecipes(query = query, number = number)

            if (response.results.isNotEmpty()) {
                Log.d("RecipeRepository", "API response successful")
                val recipes = response.results.map { dto ->
                    Recipe(
                        id = dto.id,
                        title = dto.title,
                        image = dto.image,
                        summary = dto.summary, // Yeni alan
                        instructions = dto.instructions // Yeni alan
                    )
                }
                return@withContext NetworkResult.Success(recipes)
            } else {
                val errorMessage = "No recipes found for query: $query"
                Log.e("RecipeRepository", errorMessage)
                return@withContext NetworkResult.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e("RecipeRepository", "Error searching recipes: ${e.message}", e)
            return@withContext NetworkResult.Error("Error searching recipes: ${e.message}")
        }
    }

    override suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean) {
        recipeDao.updateFavoriteStatus(id, isFavorite)
    }

    override fun getFavoriteRecipesFlow(): Flow<List<Recipe>> {
        return recipeDao.getFavoriteRecipesFlow()
    }
}
