package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.OrderItem
import com.samuelsumbane.ssptdesktop.OrderItemDraft
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrders(): Flow<List<OrderItem>>
    fun addOrder(order: OrderItemDraft)
}