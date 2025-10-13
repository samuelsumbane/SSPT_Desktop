package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.CategoryItem
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import kotlinx.coroutines.flow.Flow


interface ProductCategoryRepository {
    suspend fun getProductCategories(): List<CategoryItem>
    suspend fun addProductCategory(category: CategoryItem): StatusAndMessage
    suspend fun removeProductCategory(categoryId: Int): StatusAndMessage
    suspend fun editProductCategory(category: CategoryItem): StatusAndMessage
}