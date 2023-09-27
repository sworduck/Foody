package com.example.foody.di

import android.content.Context
import androidx.room.Room
import com.example.foody.App
import com.example.foody.data.cache.CacheDataSource
import com.example.foody.data.cache.FoodyDataBase
import com.example.foody.data.cache.dao.CategoriesDao
import com.example.foody.data.cache.dao.ProductsDao
import com.example.foody.data.cache.dao.TagsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAppContext() = App.appContext

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): FoodyDataBase {
        return Room.databaseBuilder(
            context,
            FoodyDataBase::class.java,
            FoodyDataBase.DATABASE_NAME
        )
            //.fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCategoriesDao(db: FoodyDataBase): CategoriesDao {
        return db.categoriesDatabaseDao()
    }

    @Provides
    @Singleton
    fun provideProductsDao(db: FoodyDataBase): ProductsDao {
        return db.productsDatabaseDao()
    }

    @Provides
    @Singleton
    fun provideTagsDao(db: FoodyDataBase): TagsDao {
        return db.tagsDatabaseDao()
    }

    @Provides
    @Singleton
    fun provideCacheDataSource(
        categoriesDao: CategoriesDao,
        productsDao: ProductsDao,
        tagsDao: TagsDao
    ): CacheDataSource {
        return CacheDataSource(categoriesDao,productsDao,tagsDao)
    }
}