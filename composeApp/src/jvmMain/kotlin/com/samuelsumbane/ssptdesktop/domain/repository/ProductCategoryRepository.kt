package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.CategoryItem
import kotlinx.coroutines.flow.Flow


interface ProductCategoryRepository {
    fun getProductCategories(): Flow<List<CategoryItem>>
    fun addProductCategory(category: CategoryItem)
    fun removeProductCategory(categoryId: Int)
    fun editProductCategory(category: CategoryItem)
}