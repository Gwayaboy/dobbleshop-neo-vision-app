package com.dobbleshop.neovision.di

import android.content.Context
import com.dobbleshop.neovision.data.local.AppDatabase
import com.dobbleshop.neovision.data.local.PetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for database dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getInstance(context)
    }
    
    @Provides
    fun providePetDao(database: AppDatabase): PetDao {
        return database.petDao()
    }
}
