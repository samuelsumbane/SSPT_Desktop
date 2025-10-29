package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.SaleReportRepository
import com.samuelsumbane.ssptdesktop.domain.repository.UserRepository
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.DashboardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val usersRepo: UserRepository,
    private val reportsRepo: SaleReportRepository
) : ViewModel() {
    val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getUsersStatus()
        getTotalClientAndSuppliers()
        getTotalProfitAndSales()
    }

    fun getUsersStatus() {
        viewModelScope.launch {
            val (activeUsersCount, allUsersCount) = usersRepo.getUsersStatus()
            _uiState.update { it.copy(
                activeUsers = activeUsersCount.toString(),
                allUsersCount = allUsersCount.toString()
            ) }
        }
    }

    fun getTotalProfitAndSales() {
        viewModelScope.launch {
            val (profit, sales) = reportsRepo.getTotalProfitAndSales()
            _uiState.update { it.copy(
                totalProfits = profit.toString(),
                totalSales = sales.toString()
            ) }
        }
    }

    fun getTotalClientAndSuppliers() {
        viewModelScope.launch {
            val (clients, suppliers) = reportsRepo.getTotalClientAndSuppliers()
            _uiState.update { it.copy(
                allClientsCount = clients.toString(),
                allSuppliersCount = suppliers.toString()
            ) }
        }
    }

//    fun getUsersTotalSales() {
//        viewModelScope.launch {
//            val us = reportsRepo.getUsersTotalSales()
//            _uiState.update { it.copy(
//                allClientsCount = clients,
//                allSuppliersCount = suppliers
//            ) }
//        }
//    }

    fun getTotalQuantitiesByMonthAndYear() {
        viewModelScope.launch {
            val totalQtByMonthAndYear = reportsRepo.getTotalQuantitiesByMonthAndYear()

        }
    }

    fun fetchDateTimeSales(
        initialDate: String,
        initialTime: String,
        finalDate: String,
        finalTime: String,
        ownerId: String,
    ) {
        viewModelScope.launch {
            val dateTimeSales = reportsRepo.fetchDateTimeSales(initialDate, initialTime, finalDate, finalTime, ownerId)

        }
    }



    fun getProductsMostBoughts() {
        viewModelScope.launch {
            val productsMostBoughts = reportsRepo.getProductsMostBoughts()
        }
    }


}