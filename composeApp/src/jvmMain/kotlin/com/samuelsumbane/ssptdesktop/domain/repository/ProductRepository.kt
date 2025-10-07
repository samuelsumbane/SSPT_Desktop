package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.ChangeProductPriceDraft
import com.samuelsumbane.ssptdesktop.ProductItem
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<ProductItem>>
    suspend fun getProductById(productId: Int): ProductItem?
    suspend fun addProduct(product: ProductItem)
    suspend fun editProduct(product: ProductItem)
    suspend fun changeProductPrice(product: ChangeProductPriceDraft)
}