package com.muhammedbuga.yemektarifleri.data.remote

data class RecipeDto(
    val id: Int,
    val title: String,
    val image: String,
    val summary: String?,
    val instructions: String?
)
