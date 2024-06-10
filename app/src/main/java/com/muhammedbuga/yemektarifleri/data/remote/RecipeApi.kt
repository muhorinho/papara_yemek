package com.muhammedbuga.yemektarifleri.data.remote

import com.muhammedbuga.yemektarifleri.domain.model.Recipe
import com.muhammedbuga.yemektarifleri.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String = Constants.API_KEY,
        @Query("query") query: String,
        @Query("number") number: Int
    ): RecipeSearchResponse

    @GET("recipes")
    suspend fun getRecipes(): Response<List<RecipeDto>>

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<Recipe>





}