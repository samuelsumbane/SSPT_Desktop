package com.samuelsumbane.ssptdesktop.domain.usecase

import com.samuelsumbane.ssptdesktop.kclient.OrderItem
import com.samuelsumbane.ssptdesktop.kclient.OrderItemDraft
import com.samuelsumbane.ssptdesktop.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class GetOrders(private val repo: OrderRepository) {
    operator fun invoke(): Flow<List<OrderItem>> = repo.getOrders()
}

class AddOrder(private val repo: OrderRepository) {
    suspend operator fun invoke(order: OrderItemDraft) = repo.addOrder(order)
}