package com.example.foody

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foody.data.cache.CacheDataSource
import com.example.foody.data.cloud.CloudDataSource
import com.example.foody.data.models.categories.Categories
import com.example.foody.data.models.products.Products
import com.example.foody.data.models.tags.Tags
import com.example.foody.data.models.tags.TagsItem
import com.example.foody.data.toCategoryEntity
import com.example.foody.data.toProductEntity
import com.example.foody.data.toTagsEntity
import com.example.foody.ui.screens.catalog.CatalogUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource,
) : ViewModel() {

    private val _catalogUiState = MutableStateFlow(CatalogUiState())
    val catalogUiState = _catalogUiState

    private val _tagFlow = MutableStateFlow(listOf<TagsItem>())
    val tagFlow = _tagFlow

    fun changeChangedCategoryId(id: Int) {
        _catalogUiState.update {
            it.copy(selectedCategoryId = id)
        }
        filterFromTagsProducts()
    }

    fun clearChangedCategoryId() {
        _catalogUiState.update {
            it.copy(selectedCategoryId = -1)
        }
        filterFromTagsProducts()

    }

    fun saveInCart(productId: Int, count: Int) {
        _catalogUiState.update {
            it.copy(
                productList = it.productList.toMutableList().let { productList ->
                    val index = productList.indexOfFirst {item -> item.id == productId }
                    productList.set(
                        index,
                        element = productList[index].copy(count = count)
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        cacheDataSource.updateProduct(productList[index].toProductEntity())
                    }

                    productList
                }
            )
        }
        getCartPrice()
    }

    fun getCartPrice() {
        _catalogUiState.update {
            it.copy(cartPrice = it.productList.sumOf { productItem -> productItem.price_current * productItem.count })
        }
    }

    fun changeTag(id: Int, isSelected: Boolean) {
        _tagFlow.value.first { it.id == id }.selected = isSelected
    }

    fun getCountOfSelectedTags(): Int {
        return _tagFlow.value.count { it.selected }
    }

    fun filterFromTagsProducts() {
        _catalogUiState.update {
            it.copy(filteredProductList = _catalogUiState.value.productList.filter { product ->
                product.tag_ids.containsAll(_tagFlow.value
                    .filter { tagItem -> tagItem.selected }
                    .map { tagItemInMap -> tagItemInMap.id })
                        && if (_catalogUiState.value.selectedCategoryId == -1) true
                else _catalogUiState.value.selectedCategoryId == product.category_id
            })
        }
    }

    fun filterFromSearchProducts(query: String) {
        _catalogUiState.update {
            it.copy(filteredProductList = _catalogUiState.value.productList.filter { product ->
                product.name.contains(query, true)
            })
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    var products =
                        cacheDataSource.fetchProductList()
                    if (products.isEmpty()) {
                        products = cloudDataSource.fetchProductList()
                        val productEntity = products.map { it.toProductEntity() }
                        val uniqueIds: MutableList<Int> = mutableListOf()
                        productEntity.forEach {
                            uniqueIds.add(it.id)
                        }
                        uniqueIds.distinct()
                        cacheDataSource.saveAllProduct(productEntity)
                    }
                    _catalogUiState.update {
                        it.copy(productList = products)
                    }
                    _catalogUiState.update {
                        it.copy(filteredProductList = products)
                    }
                }
            } catch (e: IOException) {
                _catalogUiState.update {
                    it.copy(productList = Products())
                }
            }
        }
    }

    fun getTags() {
        viewModelScope.launch {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    var tags = cacheDataSource.fetchTagList()
                    if (tags.isEmpty()) {
                        tags = cloudDataSource.fetchTagList()
                        tags.forEach {
                            cacheDataSource.saveTag(it.toTagsEntity())
                        }
                        _tagFlow.value = tags
                    } else {
                        _tagFlow.value = tags
                    }
                }
            } catch (e: IOException) {
                _tagFlow.value = Tags()
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    var categories =
                        cacheDataSource.fetchCategoryList()
                    if (categories.isEmpty()) {
                        categories = cloudDataSource.fetchCategoryList()
                        categories.forEach {
                            cacheDataSource.saveCategory(it.toCategoryEntity())
                        }
                    }
                    _catalogUiState.update {
                        it.copy(categories = categories)
                    }
                }
            } catch (e: IOException) {
                _catalogUiState.update {
                    it.copy(categories = Categories())
                }
            }
        }
    }
}