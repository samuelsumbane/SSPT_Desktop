package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.OrderItem
import com.samuelsumbane.ssptdesktop.kclient.OrderItemsItem

data class OrdersUiState(
  val orders: List<OrderItem> = emptyList(),
  val orderItems: List<OrderItemsItem> = emptyList(),
  val orderID: String = "",
  val showOrderItemModal: Boolean = false,
)