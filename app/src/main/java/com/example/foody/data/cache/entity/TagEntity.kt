package com.example.foody.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagEntity (
    @PrimaryKey
    val id: Int,
    val name: String
)