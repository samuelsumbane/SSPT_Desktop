package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.OrderItem
import com.samuelsumbane.ssptdesktop.kclient.OrderItemDraft
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun getOrders(): List<OrderItem>
}