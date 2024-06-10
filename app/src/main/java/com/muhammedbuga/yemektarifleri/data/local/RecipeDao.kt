package com.muhammedbuga.yemektarifleri.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.muhammedbuga.yemektarifleri.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipesFlow(): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRecipes(recipes: List<Recipe>)

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipeById(id: Int): Recipe?

    @Query("UPDATE recipes SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    fun getFavoriteRecipesFlow(): Flow<List<Recipe>>

    @Query("UPDATE recipes SET summary = :summary, instructions = :instructions WHERE id = :id")
    suspend fun updateRecipeDetails(id: Int, summary: String?, instructions: String?)
}

