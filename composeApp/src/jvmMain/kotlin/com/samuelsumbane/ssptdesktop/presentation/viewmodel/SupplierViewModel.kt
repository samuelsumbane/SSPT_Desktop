package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.SupplierRepository
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.SupplierItem
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.SupplierUiState
import com.samuelsumbane.ssptdesktop.ui.utils.AlertType
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SupplierViewModel(private val repo: SupplierRepository) : ViewModel() {
    val _uiState = MutableStateFlow(SupplierUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadSuppliers()
    }

    fun loadSuppliers() {
        viewModelScope.launch {
            val suppliers = repo.getSuppliers()
            _uiState.update { it.copy(suppliers = suppliers) }
        }
    }

    fun removeSupplier(supplierId: Int) {
        viewModelScope.launch {
            val (status, message) = repo.removeSupplier(supplierId)
        }
    }

    fun fillSupplierForm(
        id: Int? = null,
        name: String? = null,
        phone: String? = null,
        address: String? = null
    ) {
        id?.let{ newValue -> _uiState.update { it.copy(supplierId = newValue) } }
        name?.let{ newValue -> _uiState.update { it.copy(supplierName = newValue) } }
        phone?.let{ newValue -> _uiState.update { it.copy(supplierPhone = newValue) } }
        address?.let{ newValue -> _uiState.update { it.copy(supplierAddress = newValue) } }
    }

    fun onSupplierSubmit() {
        viewModelScope.launch {
            if (uiState.value.supplierName.isBlank()) {
                setFormError(FormInputName.Name, "O nome é obrigatório")
                return@launch
            }

            val (status, message) = repo.addSupplier(
                SupplierItem(
                    id = 0,
                    name = uiState.value.supplierName,
                    contact = uiState.value.supplierPhone,
                    address = uiState.value.supplierAddress
                )
            )

            val alertTitle = when (status) {
                201 -> "Fornecedor adicionado"
                else -> ""
            }
            resetForm()

            openAlertDialog(true)
            showAlert(alertTitle, message) { openAlertDialog(false) }
        }
    }

    fun setFormError(field: FormInputName, error: String) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(
                formErrors = it.commonUiState.formErrors.toMutableMap().apply { put(field, error) }
            ))
        }
    }

    fun resetForm() {
        _uiState.update {
            it.copy(
                suppliers = emptyList(),
                supplierId = 0,
                supplierName = "",
                supplierPhone = "",
                supplierAddress = ""
            )
        }
        openFormDialog(false)

        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(formErrors = emptyMap()))
        }
        loadSuppliers()
    }

    fun showAlert(
        title: String,
        text: String,
        alertType: AlertType = AlertType.SUCCESS,
        onAccept: () -> Unit
    ) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(
                alertTitle = title,
                alertText = text,
                alertType = alertType,
                showAlertDialog = true,
                alertOnAccept = onAccept
            ))
        }
    }

    fun openAlertDialog(setOpen: Boolean) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(showAlertDialog = setOpen))
        }
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


}