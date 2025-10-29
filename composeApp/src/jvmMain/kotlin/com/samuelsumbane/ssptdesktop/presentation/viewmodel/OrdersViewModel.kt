package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.OrderRepository
import com.samuelsumbane.ssptdesktop.kclient.OrderItem
import com.samuelsumbane.ssptdesktop.kclient.OrderItemsItem
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.OrdersUiState
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.SalesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrdersViewModel(private val repo: OrderRepository) : ViewModel() {
    val _uiState = MutableStateFlow(OrdersUiState())
    val uiState = _uiState.asStateFlow()


    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            val orders = repo.getOrders()
            _uiState.update { it.copy(orders = orders) }
        }
    }

    fun loadOrderItems() {
        viewModelScope.launch {
            val ordersItems = repo.getOrderItems()
            _uiState.update { it.copy(orderItems = ordersItems) }
        }
    }

    fun showOrderItem(orderId: String) {
        viewModelScope.launch {
            val filteredOrderItems = repo.getOrderItems().filter { it.orderId.toString() == orderId }
            _uiState.update {
                it.copy(
                    orderItems = filteredOrderItems,
                    showOrderItemModal = true,
                    orderID = orderId
                )
            }
        }
    }

    fun closeOrderItemModal() {
        _uiState.update {
            it.copy(
                orderItems = emptyList(),
                showOrderItemModal = false,
                orderID = ""
            )
        }
    }

//    orderID?.let { newValue -> _uiState.update { it.copy(orderID = newValue) } }

}