package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.kclient.OrderItem
import com.samuelsumbane.ssptdesktop.kclient.OrderItemDraft
import com.samuelsumbane.ssptdesktop.domain.repository.OrderRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.OrderItemsItem
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.apiPath
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class OrderRepositoryImpl : OrderRepository{
    val kClientRepo = KClientRepository()
    val token = Session.jwtToken ?: ""

    override suspend fun getOrders(): List<OrderItem> {
        return try {
            kClientRepo.httpClient.get("$apiPath/orders") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching data: $e")
            emptyList()
        }
    }

    override suspend fun getOrderItems(): List<OrderItemsItem> {
        return try {
            kClientRepo.httpClient.get("$apiPath/orders-items") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching orderItems data: $e")
            emptyList()
        }
    }

}