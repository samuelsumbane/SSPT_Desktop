package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.kclient.CategoryItem
import com.samuelsumbane.ssptdesktop.domain.repository.ProductCategoryRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.apiCategoriesPath
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ProductCategoryRepositoryImpl : ProductCategoryRepository {

    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()

    override suspend fun getProductCategories(): List<CategoryItem> {
        return try {
            kClientRepo.httpClient.get("$apiCategoriesPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch(e: Exception) {
            println("Error fetching data: $e")
            emptyList()
        }
    }

    override suspend fun addProductCategory(category: CategoryItem): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiCategoriesPath/edit", category)
        return StatusAndMessage(status, message)
    }

    override suspend fun removeProductCategory(categoryId: Int): StatusAndMessage {
        val (status, message) = kClientRepo.deleteRequest("$apiCategoriesPath/delete/$categoryId")
        return StatusAndMessage(status, message)
    }

    override suspend fun editProductCategory(category: CategoryItem): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiCategoriesPath/edit", category, "put")
        return StatusAndMessage(status, message)
    }
}