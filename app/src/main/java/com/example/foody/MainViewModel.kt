package com.example.foody

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foody.data.cache.CacheDataSource
import com.example.foody.data.cloud.CloudDataSource
import com.example.foody.data.models.cart.CartItem
import com.example.foody.data.models.categories.Categories
import com.example.foody.data.models.categories.CategoriesItem
import com.example.foody.data.models.products.Products
import com.example.foody.data.models.products.ProductsItem
import com.example.foody.data.models.tags.Tags
import com.example.foody.data.models.tags.TagsItem
import com.example.foody.data.toCategoryEntity
import com.example.foody.data.toProductEntity
import com.example.foody.data.toTagsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cloudDataSource: CloudDataSource,
    private val cacheDataSource: CacheDataSource
) : ViewModel(){
    private val _categoriesFlow = MutableStateFlow(listOf<CategoriesItem>())
    val categoriesFlow = _categoriesFlow.asStateFlow()

    private val _tagFlow = MutableStateFlow(listOf<TagsItem>())
    val tagFlow = _tagFlow.asStateFlow()

    private val _productFlow = MutableStateFlow(listOf<ProductsItem>())
    val productFlow = _productFlow.asStateFlow()

    private val _filteredProductFlow = MutableStateFlow(listOf<ProductsItem>())
    val filteredProductFlow = _filteredProductFlow.asStateFlow()

    private val _selectedCategory = MutableStateFlow(-1)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _currentCartPrice = MutableStateFlow(0)
    val currentCartPrice = _currentCartPrice.asStateFlow()

    fun changeChangedCategoryId(id: Int) {
        _selectedCategory.value = id
        filterFromTagsProducts()
    }

    fun saveInCart(productId: Int, count: Int){
        _productFlow.value.first { it.id == productId }.let {
            it.count = count
            CoroutineScope(Dispatchers.IO).launch {
                cacheDataSource.updateProduct(it.toProductEntity())
            }
        }
        getCartPrice()
    }

    fun getCartPrice(){
        _currentCartPrice.value = _productFlow.value.sumOf { it.price_current * it.count }
    }

    fun changeTag(id: Int, isSelected: Boolean) {
        _tagFlow.value.first { it.id == id }.selected = isSelected
    }

    fun getCountOfSelectedTags(): Int {
        return _tagFlow.value.count { it.selected }
    }

    fun filterFromTagsProducts() {
        _filteredProductFlow.value = _productFlow.value.filter { product ->
            product.tag_ids.containsAll(_tagFlow.value.filter { it.selected }.map { it.id })
                    && if(_selectedCategory.value == -1) true else _selectedCategory.value == product.category_id
        }
    }

    fun filterFromSearchProducts(query: String) {
        _filteredProductFlow.value = _productFlow.value.filter { product ->
            product.name.contains(query, true)
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    var products =
                        cacheDataSource.fetchProductList()//cloudDataSource.fetchProductList()
                    if (products.isEmpty()) {
                        products = cloudDataSource.fetchProductList()
                        val productEntity = products.map { it.toProductEntity() }
                        val uniqueIds:MutableList<Int> = mutableListOf()
                        productEntity.forEach {
                            uniqueIds.add(it.id)
                        }
                        uniqueIds.distinct()
                        Log.i("ALOALOAL", "${uniqueIds.size} --- ${productEntity.size}")
                        cacheDataSource.saveAllProduct(productEntity)
                        _productFlow.value = products
                    } else {
                        _productFlow.value = products
                    }
                    _filteredProductFlow.value = products
                    Log.e("Tag", "$products")
                }
            } catch (e: IOException) {
                _productFlow.value = Products()
            }
        }
    }

    fun getTags() {
        viewModelScope.launch {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    var tags = cacheDataSource.fetchTagList()//cloudDataSource.fetchTagList()
                    if (tags.isEmpty()) {
                        tags = cloudDataSource.fetchTagList()
                        tags.forEach {
                            cacheDataSource.saveTag(it.toTagsEntity())
                        }
                        _tagFlow.value = tags
                    } else {
                        _tagFlow.value = tags
                    }
                    Log.e("Tag", "$tags")
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
                    var categories = cacheDataSource.fetchCategoryList()//cloudDataSource.fetchCategoryList()
                    if(categories.isEmpty()){
                        categories = cloudDataSource.fetchCategoryList()
                        categories.forEach {
                            cacheDataSource.saveCategory(it.toCategoryEntity())
                        }
                        _categoriesFlow.value = categories
                    }
                    else{
                        _categoriesFlow.value = categories
                    }
                    Log.e("Tag","$categories")
                }
            } catch (e: IOException) {
                _categoriesFlow.value = Categories()
            }
        }
    }
}