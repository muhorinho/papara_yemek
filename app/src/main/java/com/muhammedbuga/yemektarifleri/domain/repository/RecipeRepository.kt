package com.muhammedbuga.yemektarifleri.domain.repository

import com.muhammedbuga.yemektarifleri.domain.model.Recipe
import com.muhammedbuga.yemektarifleri.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    suspend fun getRecipes(): NetworkResult<List<Recipe>>

    suspend fun getRecipeById(id: Int): NetworkResult<Recipe>

    suspend fun searchRecipes(query: String, number: Int): NetworkResult<List<Recipe>>

    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    fun getFavoriteRecipesFlow(): Flow<List<Recipe>>

}
