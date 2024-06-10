package com.muhammedbuga.yemektarifleri.domain.usecase
import com.muhammedbuga.yemektarifleri.domain.model.Recipe
import com.muhammedbuga.yemektarifleri.domain.repository.RecipeRepository
import com.muhammedbuga.yemektarifleri.util.NetworkResult
import javax.inject.Inject

class GetRecipeListUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(query: String, number: Int): NetworkResult<List<Recipe>> {
        return repository.searchRecipes(query, number)
    }
}