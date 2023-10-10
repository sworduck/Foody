package com.example.foody.ui.screens.catalog

import com.example.foody.data.models.categories.CategoriesItem
import com.example.foody.data.models.products.ProductsItem

data class CatalogUiState (
    val categories: List<CategoriesItem> = listOf(),
    val filteredProductList: List<ProductsItem> = listOf(),
    val productList: List<ProductsItem> = listOf(),
    val selectedCategoryId: Int = -1,
    val cartPrice: Int = 0,
)