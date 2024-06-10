package com.muhammedbuga.yemektarifleri.di
import com.muhammedbuga.yemektarifleri.data.local.RecipeDao
import com.muhammedbuga.yemektarifleri.data.remote.RecipeApi
import com.muhammedbuga.yemektarifleri.data.repository.RecipeRepositoryImpl
import com.muhammedbuga.yemektarifleri.domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRecipeRepository(
        recipeDao: RecipeDao,
        recipeApi: RecipeApi
    ): RecipeRepository {
        return RecipeRepositoryImpl(recipeDao, recipeApi)
    }
}
