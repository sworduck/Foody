package com.example.foody.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.foody.data.cache.entity.ProductEntity

@Dao
interface ProductsDao {
    @Query("SELECT * FROM productEntity")
    suspend fun getAllProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg productsEntity: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(productsEntity: List<ProductEntity>)

    @Update
    fun update(productsEntity: ProductEntity)

    @Query("DELETE FROM productEntity WHERE id = :id")
    fun delete(id: Int)

    @Query("DELETE FROM productEntity")
    fun deleteAll()
}