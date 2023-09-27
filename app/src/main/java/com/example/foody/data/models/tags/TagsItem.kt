package com.example.foody.data.models.tags

data class TagsItem(
    val id: Int,
    val name: String,
    var selected: Boolean = false
)