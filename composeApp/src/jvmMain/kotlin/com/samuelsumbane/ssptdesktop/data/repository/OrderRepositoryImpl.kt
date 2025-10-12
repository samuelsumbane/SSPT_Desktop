package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.kclient.OrderItem
import com.samuelsumbane.ssptdesktop.kclient.OrderItemDraft
import com.samuelsumbane.ssptdesktop.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class OrderRepositoryImpl : OrderRepository{
    val _state = MutableStateFlow(emptyList<OrderItem>())

    override fun getOrders(): Flow<List<OrderItem>> = _state

    override fun addOrder(order: OrderItemDraft) {
//        _state.value += order
    }
}