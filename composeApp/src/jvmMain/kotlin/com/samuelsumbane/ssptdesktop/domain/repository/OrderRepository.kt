package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.OrderItem
import com.samuelsumbane.ssptdesktop.kclient.OrderItemDraft
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrders(): List<OrderItem>
    fun addOrder(order: OrderItemDraft)
}