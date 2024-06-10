package com.muhammedbuga.yemektarifleri.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String,
    val isFavorite: Boolean = false,
    val summary: String? = null,
    val instructions: String? = null
)
