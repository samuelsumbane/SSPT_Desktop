package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.kclient.CategoryItem
import com.samuelsumbane.ssptdesktop.domain.repository.ProductCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ProductCategoryRepositoryImpl : ProductCategoryRepository {
    private val _state = MutableStateFlow<List<CategoryItem>>(
        listOf(
            CategoryItem(id = 1, "Alimentos e Bebidas", true),
            CategoryItem(id = 2, "Higiene e Limpeza", true),
            CategoryItem(id = 3, "Cuidados Infantis", true),
            CategoryItem(id = 4, "Productos para Casa", true),
            CategoryItem(id = 5, "Roupas", true),
            CategoryItem(id = 6, "Electrônicos", true),
        )
    )

    override fun getProductCategories(): Flow<List<CategoryItem>> = _state

    override fun addProductCategory(category: CategoryItem) {
        _state.value += category
    }

    override fun removeProductCategory(categoryId: Int) {
        _state.value.filterNot { it.id == categoryId }
    }

    override fun editProductCategory(category: CategoryItem) {
        _state.value = _state.value.map {
            if (it.id == category.id) category else it
        }
    }
}