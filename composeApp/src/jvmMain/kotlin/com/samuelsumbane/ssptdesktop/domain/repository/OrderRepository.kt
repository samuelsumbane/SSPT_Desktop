package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.OrderItem
import com.samuelsumbane.ssptdesktop.kclient.OrderItemDraft
import com.samuelsumbane.ssptdesktop.kclient.OrderItemsItem
import kotlinx.coroutines.flow.Flow


/** Order and orderItems */
interface OrderRepository {
    suspend fun getOrders(): List<OrderItem>
    suspend fun getOrderItems(): List<OrderItemsItem>
}