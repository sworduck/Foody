package com.example.foody.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.foody.data.cache.entity.TagEntity

@Dao
interface TagsDao {
    @Query("SELECT * FROM tagEntity")
    suspend fun getAllTags(): List<TagEntity>

    @Insert
    fun insert(vararg tagEntity: TagEntity)

    @Update
    fun update(tagsEntity: TagEntity)

    @Query("DELETE FROM tagEntity WHERE id = :id")
    fun delete(id: Int)

    @Query("DELETE FROM tagEntity")
    fun deleteAll()
}