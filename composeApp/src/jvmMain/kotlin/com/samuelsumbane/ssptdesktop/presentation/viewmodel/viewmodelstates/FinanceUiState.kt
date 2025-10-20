package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.PayableItem
import com.samuelsumbane.ssptdesktop.kclient.ReceivableItem

data class FinanceUiState(
    val payables: List<PayableItem> = emptyList(),
    val receivables: List<ReceivableItem> = emptyList(),
    val supplier: String = "",
    val client: String = "",
    val description: String = "",
    val amount: Double = 0.0,
    val paymentMethod: String = "",
    val expirationDate: String = "",
    val showSecondaryModal: Boolean = false,
    val paymentDropdownExpanded: Boolean = false,
    val commonUiState: CommonUiState = CommonUiState()
)
