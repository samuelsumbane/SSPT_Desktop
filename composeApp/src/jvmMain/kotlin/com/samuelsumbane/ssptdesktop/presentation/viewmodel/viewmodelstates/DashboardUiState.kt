package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

data class DashboardUiState(
    val activeUsers: String = "0",
    val allUsersCount: String = "0",
    val allClientsCount: String = "0",
    val allSuppliersCount: String = "0",
    val totalSales: String = "0.0",
    val totalProfits: String = "0.0"
)