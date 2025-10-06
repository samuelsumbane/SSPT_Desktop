package com.samuelsumbane.ssptdesktop.domain.usecase

import com.samuelsumbane.ssptdesktop.CategoryItem
import com.samuelsumbane.ssptdesktop.domain.repository.ProductCategoryRepository
import kotlinx.coroutines.flow.Flow


class GetProductCategoriesUseCase(private val repository: ProductCategoryRepository) {
    operator fun invoke(): Flow<List<CategoryItem>> = repository.getProductCategories()
}

class AddProductCategoryUseCase(private val repository: ProductCategoryRepository) {
    suspend operator fun invoke(productCategory: CategoryItem) {
        repository.addProductCategory(productCategory)
    }
}

class EditProductCategoryUseCase(private val repository: ProductCategoryRepository) {
    suspend operator fun invoke(productCategory: CategoryItem) {
        repository.editProductCategory(productCategory)
    }
}

class RemoveProductCategoryUseCase(private val repository: ProductCategoryRepository) {
    suspend operator fun invoke(productCategoryId: Int) {
        repository.removeProductCategory(productCategoryId)
    }
}


