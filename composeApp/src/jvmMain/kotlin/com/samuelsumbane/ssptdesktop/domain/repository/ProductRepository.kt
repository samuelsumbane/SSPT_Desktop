package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.ChangeProductPriceDraft
import com.samuelsumbane.ssptdesktop.kclient.ProductItem
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): List<ProductItem>
    suspend fun getProductById(productId: Int): ProductItem?
    suspend fun addProduct(product: ProductItem): StatusAndMessage
    suspend fun editProduct(product: ProductItem): StatusAndMessage
    suspend fun changeProductPrice(product: ChangeProductPriceDraft): StatusAndMessage
    suspend fun removeProduct(productId: Int): StatusAndMessage
}