package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.SupplierItem

data class SupplierUiState(
    val suppliers: List<SupplierItem> = emptyList(),
    val supplierId: Int = 0,
    val supplierName: String = "",
    val supplierPhone: String = "",
    val supplierAddress: String = "",
    val commonUiState: CommonUiState = CommonUiState()
)
