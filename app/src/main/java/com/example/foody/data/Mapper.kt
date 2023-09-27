package com.example.foody.data

import com.example.foody.data.cache.entity.CategoryEntity
import com.example.foody.data.cache.entity.ProductEntity
import com.example.foody.data.cache.entity.TagEntity
import com.example.foody.data.models.cart.CartItem
import com.example.foody.data.models.categories.CategoriesItem
import com.example.foody.data.models.products.ProductsItem
import com.example.foody.data.models.tags.TagsItem

fun CategoryEntity.toCategoriesItem(): CategoriesItem =
    CategoriesItem(id, name)

fun CategoriesItem.toCategoryEntity(): CategoryEntity =
    CategoryEntity(id, name)

fun TagEntity.toTagsItem(): TagsItem =
    TagsItem(id, name)

fun TagsItem.toTagsEntity(): TagEntity =
    TagEntity(id, name)

fun ProductEntity.toProductItem(): ProductsItem =
    ProductsItem(
        id = id,
        carbohydrates_per_100_grams = carbohydrates_per_100_grams,
        category_id = category_id,
        description = description,
        energy_per_100_grams = energy_per_100_grams,
        fats_per_100_grams = fats_per_100_grams,
        image = image,
        measure = measure,
        measure_unit = measure_unit,
        name = name,
        price_current = price_current,
        price_old = price_old,
        proteins_per_100_grams = proteins_per_100_grams,
        tag_ids = tag_ids,
        count = count
    )

fun ProductsItem.toProductEntity(): ProductEntity =
    ProductEntity(
        id = id,
        carbohydrates_per_100_grams = carbohydrates_per_100_grams,
        category_id = category_id,
        description = description,
        energy_per_100_grams = energy_per_100_grams,
        fats_per_100_grams = fats_per_100_grams,
        image = image,
        measure = measure,
        measure_unit = measure_unit,
        name = name,
        price_current = price_current,
        price_old = price_old,
        proteins_per_100_grams = proteins_per_100_grams,
        tag_ids = tag_ids,
        count = count
    )