package com.example.foody.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.foody.data.cache.entity.CategoryEntity

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM categoryEntity")
    fun getAllCategories(): List<CategoryEntity>

    @Insert
    fun insert(vararg categoriesEntity: CategoryEntity)

    @Update
    fun update(categoriesEntity: CategoryEntity)

    @Query("DELETE FROM categoryEntity WHERE id = :id")
    fun delete(id: Int)

    @Query("DELETE FROM categoryEntity")
    fun deleteAll()
}