package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.OrderRepository
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.SalesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrdersViewModel(private val repo: OrderRepository) : ViewModel() {
    val _uiState = MutableStateFlow(SalesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            val orders = repo.getOrders()
//            _uiState.update { it.copy(orders = orders) }
        }
    }
}