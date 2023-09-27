package com.example.foody.data.cloud

import android.content.Context
import com.example.foody.data.models.categories.Categories
import com.example.foody.data.models.categories.CategoriesItem
import com.example.foody.data.models.products.Products
import com.example.foody.data.models.products.ProductsItem
import com.example.foody.data.models.tags.Tags
import com.example.foody.data.models.tags.TagsItem
import kotlinx.coroutines.delay
import javax.inject.Inject

const val CategoriesJsonFile = "categories.json"
const val ProductsJsonFile = "products.json"
const val TagsJsonFile = "tags.json"

class CloudDataSource @Inject constructor(
    private val context: Context
) {
    suspend fun fetchCategoryList(): List<CategoriesItem> {
        delay(2000)
        return getResponse(
            context = context,
            jsonName = CategoriesJsonFile,
            Categories::class.java
        ).toList()
    }
    suspend fun fetchProductList(): List<ProductsItem> {
        delay(2000)
        return getResponse(
            context = context,
            jsonName = ProductsJsonFile,
            Products::class.java
        ).toList()
    }
    suspend fun fetchTagList(): List<TagsItem> {
        delay(2000)
        return getResponse(
            context = context,
            jsonName = TagsJsonFile,
            Tags::class.java
        ).toList()
    }
}