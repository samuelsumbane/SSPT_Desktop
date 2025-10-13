package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.kclient.OrderItem
import com.samuelsumbane.ssptdesktop.kclient.OrderItemDraft
import com.samuelsumbane.ssptdesktop.domain.repository.OrderRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class OrderRepositoryImpl : OrderRepository{
    val kClientRepo = KClientRepository()

    override fun getOrders(): List<OrderItem> {
//        return try {
//            kClientRepo.httpClient.get("$apiO")
//        } catch (e: Exception) {
//            println("Error fetching data: $e")
//            emptyList()
//        }
        return emptyList<OrderItem>()
    }

    override fun addOrder(order: OrderItemDraft) {
//        _state.value += order
    }
}