package com.example.foody.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foody.data.cache.dao.CategoriesDao
import com.example.foody.data.cache.dao.ProductsDao
import com.example.foody.data.cache.dao.TagsDao
import com.example.foody.data.cache.entity.CategoryEntity
import com.example.foody.data.cache.entity.ProductEntity
import com.example.foody.data.cache.entity.TagEntity

@TypeConverters(TagIdsConverter::class)
@Database(entities = [CategoryEntity::class, ProductEntity::class, TagEntity::class],
    version = 6,
    exportSchema = false)
abstract class FoodyDataBase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "foody_db"
    }

    abstract fun categoriesDatabaseDao(): CategoriesDao
    abstract fun productsDatabaseDao(): ProductsDao
    abstract fun tagsDatabaseDao(): TagsDao
}
