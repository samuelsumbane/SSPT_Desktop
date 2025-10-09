package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.ChangeProductPriceDraft
import com.samuelsumbane.ssptdesktop.ProductItem
import com.samuelsumbane.ssptdesktop.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.collections.emptyList

class ProductRepositoryImpl : ProductRepository {
    val _state = MutableStateFlow(emptyList<ProductItem>())

    override fun getProducts(): Flow<List<ProductItem>> = _state

    override suspend fun getProductById(productId: Int): ProductItem? {
        return _state.value.firstOrNull { it.id == productId }
    }

    override suspend fun addProduct(product: ProductItem) {
        _state.value += product
    }

    override suspend fun editProduct(product: ProductItem) {
        _state.value = _state.value.map {
            if (it.id == product.id) product else it
        }
    }

    override suspend fun changeProductPrice(product: ChangeProductPriceDraft) {
        //
    }

    override suspend fun removeProduct(productId: Int) {
        _state.value = _state.value.filterNot { it.id == productId }
    }

}