package com.muhammedbuga.yemektarifleri.domain.usecase
import com.muhammedbuga.yemektarifleri.domain.model.Recipe
import com.muhammedbuga.yemektarifleri.domain.repository.RecipeRepository
import com.muhammedbuga.yemektarifleri.util.NetworkResult
import javax.inject.Inject

class GetRecipeDetailUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id: Int): NetworkResult<Recipe> {
        return repository.getRecipeById(id)
    }
}