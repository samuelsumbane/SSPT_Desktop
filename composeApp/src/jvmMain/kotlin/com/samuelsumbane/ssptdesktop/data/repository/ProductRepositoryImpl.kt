package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.kclient.ChangeProductPriceDraft
import com.samuelsumbane.ssptdesktop.kclient.ProductItem
import com.samuelsumbane.ssptdesktop.domain.repository.ProductRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.apiOwnersPath
import com.samuelsumbane.ssptdesktop.kclient.apiProductsPath
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.collections.emptyList

class ProductRepositoryImpl : ProductRepository {
    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()

    override suspend fun getProducts(): List<ProductItem> {
        return try {
            kClientRepo.httpClient.get("$apiProductsPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch(e: Exception) {
            println("Error fetching data: $e")
            emptyList()
        }
    }

    override suspend fun getProductById(productId: Int): ProductItem? {
        return try {
            kClientRepo.httpClient.get("$apiProductsPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch(e: Exception) {
            println("Error fetching product: $e")
            null
        }
    }

    override suspend fun addProduct(product: ProductItem): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiProductsPath/create", product)
        return StatusAndMessage(status, message)
    }

    override suspend fun editProduct(product: ProductItem): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiProductsPath/edit", product, "put")
        return StatusAndMessage(status, message)
    }

    override suspend fun changeProductPrice(product: ChangeProductPriceDraft): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiProductsPath/changePrice", product, "put")
        return StatusAndMessage(status, message)
    }

    override suspend fun removeProduct(productId: Int): StatusAndMessage {
        val (status, message) = kClientRepo.deleteRequest("$apiProductsPath/delete/$productId")
        return StatusAndMessage(status, message)
    }

}