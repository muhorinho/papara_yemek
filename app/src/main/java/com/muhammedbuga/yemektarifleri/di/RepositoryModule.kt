package com.muhammedbuga.yemektarifleri.di
import android.content.Context
import com.muhammedbuga.yemektarifleri.data.local.RecipeDao
import com.muhammedbuga.yemektarifleri.data.remote.RecipeApi
import com.muhammedbuga.yemektarifleri.data.repository.RecipeRepositoryImpl
import com.muhammedbuga.yemektarifleri.domain.repository.RecipeRepository
import com.muhammedbuga.yemektarifleri.util.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNetworkUtil(@ApplicationContext context: Context): NetworkUtil {
        return NetworkUtil(context)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(
        recipeDao: RecipeDao,
        recipeApi: RecipeApi,
        networkUtil: NetworkUtil
    ): RecipeRepository {
        return RecipeRepositoryImpl(recipeDao, recipeApi, networkUtil)
    }
}
