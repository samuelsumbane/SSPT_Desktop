package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.FinanceRepository
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.FinanceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FinanceViewModel(private val repo: FinanceRepository) : ViewModel() {
    val _uiState = MutableStateFlow(FinanceUiState())
    val uistate = _uiState.asStateFlow()

    fun loadPayables() {
        viewModelScope.launch {
            val payables = repo.getPayables()
            _uiState.update { it.copy(payables = payables) }
        }
    }

    fun loadReceivables() {
        viewModelScope.launch {
            val receivables = repo.getReceivables()
            _uiState.update { it.copy(receivables = receivables) }
        }
    }

    fun onResetForm() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    supplier = "",
                    description = "",
                    amount = 0.0,
                    showSecondaryModal = false,
                    commonUiState = it.commonUiState.copy(
                        formErrors = emptyMap(),
                        showFormDialog = false
                    )
                )
            }
        }
    }

    fun fillFormFields(
        supplier: String? = null,
        description: String? = null,
        amount: Double? = null,
        expirationDate: String? = null,
        paymentMethod: String? = null,
        paymentDropdownExpanded: Boolean? = null,
        client: String? = null
    ) {
        supplier?.let { newValue -> _uiState.update { it.copy(supplier = newValue) }}
        description?.let { newValue -> _uiState.update { it.copy(description = newValue) }}
        amount?.let { newValue -> _uiState.update { it.copy(amount = newValue) }}
        expirationDate?.let { newValue -> _uiState.update { it.copy(expirationDate = newValue) }}
        paymentMethod?.let { newValue -> _uiState.update { it.copy(paymentMethod = newValue) }}
        paymentDropdownExpanded?.let { newValue -> _uiState.update { it.copy(paymentDropdownExpanded = newValue) }}
        client?.let { newValue -> _uiState.update { it.copy(client = newValue) }}
//        ?.let { newValue -> _uiState.update { it.copy( = newValue) }}
    }

    fun openFormDialog(setOpen: Boolean, title: String = "") {
        _uiState.update {
            it.copy(
                commonUiState = it.commonUiState.copy(
                    showFormDialog = setOpen,
                    formDialogTitle = title
                )
            )
        }
    }

    fun onAddPayableSubmit() {

    }
}

