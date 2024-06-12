package com.muhammedbuga.yemektarifleri.domain.usecase
import com.muhammedbuga.yemektarifleri.domain.model.Recipe
import com.muhammedbuga.yemektarifleri.domain.repository.RecipeRepository
import com.muhammedbuga.yemektarifleri.util.NetworkResult
import javax.inject.Inject

class GetRecipeDetailUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(id: Int): NetworkResult<Recipe> {
        return when (val result = repository.getRecipeById(id)) {
            is NetworkResult.Success -> {
                val recipe = result.data
                if (recipe != null) {
                    val cleanSummary = recipe.summary?.replace(Regex("<[^>]*>"), "")
                    val cleanInstructions = recipe.instructions?.replace(Regex("<[^>]*>"), "")
                    NetworkResult.Success(
                        recipe.copy(
                            summary = cleanSummary,
                            instructions = cleanInstructions
                        )
                    )
                } else {
                    result
                }
            }
            else -> result
        }
    }
}
