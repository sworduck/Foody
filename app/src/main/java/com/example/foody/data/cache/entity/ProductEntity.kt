package com.example.foody.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class ProductEntity (
    @PrimaryKey
    val id: Int,
    val carbohydrates_per_100_grams: Double,
    val category_id: Int,
    val description: String,
    val energy_per_100_grams: Double,
    val fats_per_100_grams: Double,
    val image: String,
    val measure: Int,
    val measure_unit: String,
    val name: String,
    val price_current: Int,
    val price_old: Int,
    val proteins_per_100_grams: Double,
    val tag_ids: List<Int>,
    val count: Int
)