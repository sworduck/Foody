package com.example.foody.data.cache

import com.example.foody.data.cache.dao.CategoriesDao
import com.example.foody.data.cache.dao.ProductsDao
import com.example.foody.data.cache.dao.TagsDao
import com.example.foody.data.cache.entity.CategoryEntity
import com.example.foody.data.cache.entity.ProductEntity
import com.example.foody.data.cache.entity.TagEntity
import com.example.foody.data.models.cart.CartItem
import com.example.foody.data.models.categories.CategoriesItem
import com.example.foody.data.models.products.ProductsItem
import com.example.foody.data.models.tags.TagsItem
import com.example.foody.data.toCategoriesItem
import com.example.foody.data.toProductItem
import com.example.foody.data.toTagsItem

class CacheDataSource(
    private val categoriesDao: CategoriesDao,
    private val productsDao: ProductsDao,
    private val tagsDao: TagsDao,
    ) {

    //Category
    suspend fun fetchCategoryList(): List<CategoriesItem> {
        return categoriesDao.getAllCategories().map { it.toCategoriesItem() }
    }

    fun saveCategory(category: CategoryEntity) {
        categoriesDao.insert(category)
    }

    fun removeCategory(category: CategoryEntity) {
        categoriesDao.delete(category.id)
    }

    fun removeAllCategories() {
        categoriesDao.deleteAll()
    }

    //Tags
    suspend fun fetchTagList(): List<TagsItem> {
        return tagsDao.getAllTags().map { it.toTagsItem() }
    }

    fun saveTag(tag: TagEntity) {
        tagsDao.insert(tag)
    }

    fun removeTag(tag: TagEntity) {
        tagsDao.delete(tag.id)
    }

    fun removeAllTags() {
        tagsDao.deleteAll()
    }

    //Products
    suspend fun fetchProductList(): List<ProductsItem> {
        return productsDao.getAllProducts().map { it.toProductItem() }
    }

    fun saveProduct(product: ProductEntity) {
        productsDao.insert(product)
    }

    fun updateProduct(product: ProductEntity) {
        productsDao.update(product)
    }

    fun saveAllProduct(productList: List<ProductEntity>) {
        productsDao.insertAll(productList)
    }

    fun removeProduct(product: ProductEntity) {
        productsDao.delete(product.id)
    }

    fun removeAllProducts() {
        productsDao.deleteAll()
    }
}
