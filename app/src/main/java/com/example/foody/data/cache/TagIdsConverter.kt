package com.example.foody.data.cache

import androidx.room.TypeConverter

class TagIdsConverter {
    @TypeConverter
    fun fromString(idList: String): List<Int> {
        return if(idList.isEmpty()) emptyList<Int>() else idList.split(",").map { it.toInt() }
    }

    @TypeConverter
    fun toString(idList: List<Int>): String {
        return idList.joinToString(separator = ",")
    }
}