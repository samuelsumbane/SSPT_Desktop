package com.samuelsumbane.ssptdesktop.domain.usecase

import com.samuelsumbane.ssptdesktop.ChangeProductPriceDraft
import com.samuelsumbane.ssptdesktop.ProductItem
import com.samuelsumbane.ssptdesktop.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow


class GetProductsUseCase(private val repo: ProductRepository) {
    operator fun invoke(): Flow<List<ProductItem>> = repo.getProducts()
}

class AddProductsUseCase(private val repo: ProductRepository) {
    suspend operator fun invoke(productItem: ProductItem) {

    }
}

class EditProductUseCase(private val repo: ProductRepository) {
    suspend operator fun invoke(productItem: ProductItem) {

    }
}

class UpdateProductPriceUseCase(private val repo: ProductRepository) {
    suspend operator fun invoke(product: ChangeProductPriceDraft) {

    }
}
