package com.example.foody.data.cache

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.foody.R
import com.example.foody.data.cache.dao.CategoriesDao
import com.example.foody.data.cache.dao.ProductsDao
import com.example.foody.data.cache.dao.TagsDao
import com.example.foody.data.cache.entity.CategoryEntity
import com.example.foody.data.cache.entity.ProductEntity
import com.example.foody.data.cache.entity.TagEntity
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class FoodyDataBaseTest {

    private lateinit var categoriesDao: CategoriesDao
    private lateinit var productsDao: ProductsDao
    private lateinit var tagsDao: TagsDao
    private lateinit var db: FoodyDataBase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, FoodyDataBase::class.java).build()
        categoriesDao = db.categoriesDatabaseDao()
        productsDao = db.productsDatabaseDao()
        tagsDao = db.tagsDatabaseDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeCategoryAndReadInList() {
        val category = CategoryEntity(0, "george")
        categoriesDao.insert(category)
        val byName = categoriesDao.getAllCategories().first{ it.name == "george" }
        assertEquals(category, byName)
    }

    @Test
    @Throws(Exception::class)
    fun writeProductsAndReadInList() {
        val product = ProductEntity(
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
        productsDao.insert(product)
        val byName = productsDao.getAllProducts().first{ it.name == "george" }
        assertEquals(product, byName)
    }

    @Test
    @Throws(Exception::class)
    fun writeTagsAndReadInList() {
        val tag = TagEntity(0, "george")
        tagsDao.insert(tag)
        val byName = tagsDao.getAllTags().first{ it.name == "george" }
        assertEquals(tag, byName)
    }
}