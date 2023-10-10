package com.example.foody

import android.content.Context
import androidx.room.Room
import com.example.foody.data.cache.FoodyDataBase
import com.example.foody.data.cache.entity.CategoryEntity
import com.example.foody.data.cache.entity.ProductEntity
import com.example.foody.data.cache.entity.TagEntity
import com.example.foody.data.models.categories.CategoriesItem
import com.example.foody.data.models.products.ProductsItem
import com.example.foody.data.models.tags.TagsItem
import com.example.foody.data.toCategoriesItem
import com.example.foody.data.toCategoryEntity
import com.example.foody.data.toProductEntity
import com.example.foody.data.toProductItem
import com.example.foody.data.toTagsEntity
import com.example.foody.data.toTagsItem
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MapperUnitTest {

    private lateinit var categoryEntity: CategoryEntity
    private lateinit var categoriesItem: CategoriesItem
    private lateinit var tagEntity: TagEntity
    private lateinit var tagsItem: TagsItem
    private lateinit var productEntity: ProductEntity
    private lateinit var productsItem: ProductsItem

    @Before
    fun createDataForMapping() {
        categoryEntity = CategoryEntity(0, "george")
        categoriesItem = CategoriesItem(0, "george")
        tagEntity = TagEntity(0, "george")
        tagsItem = TagsItem(0, "george")
        productEntity = ProductEntity(
            id = 1,
            name = "george",
            measure = 1,
            measure_unit = "кг",
            price_current = 100,
            price_old = 100,
            count = 1,
            carbohydrates_per_100_grams = 100.0,
            fats_per_100_grams = 100.0,
            proteins_per_100_grams = 100.0,
            energy_per_100_grams = 100.0,
            category_id = 1,
            image = "",
            description = "",
            tag_ids = listOf(1)
        )
        productsItem = ProductsItem(
            id = 1,
            name = "george",
            measure = 1,
            measure_unit = "кг",
            price_current = 100,
            price_old = 100,
            count = 1,
            carbohydrates_per_100_grams = 100.0,
            fats_per_100_grams = 100.0,
            proteins_per_100_grams = 100.0,
            energy_per_100_grams = 100.0,
            category_id = 1,
            image = "",
            description = "",
            tag_ids = listOf(1)
        )
    }

    @Test
    fun categoryEntityToCategoriesItem(){
        assertEquals(categoriesItem, categoryEntity.toCategoriesItem())
    }

    @Test
    fun categoriesItemToCategoryEntity(){
        assertEquals(categoryEntity, categoriesItem.toCategoryEntity())
    }

    @Test
    fun tagEntityToTagsItem(){
        assertEquals(tagsItem, tagEntity.toTagsItem())
    }

    @Test
    fun tagsItemToTagEntity(){
        assertEquals(tagEntity, tagsItem.toTagsEntity())
    }

    @Test
    fun productEntityToProductItem(){
        assertEquals(productsItem, productEntity.toProductItem())
    }

    @Test
    fun productsItemToProductEntity(){
        assertEquals(productEntity, productsItem.toProductEntity())
    }

}