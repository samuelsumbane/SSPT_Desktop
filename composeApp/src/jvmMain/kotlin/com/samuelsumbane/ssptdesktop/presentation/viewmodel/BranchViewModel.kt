package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.BranchRepository
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.BranchUiState
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BranchViewModel(
    private val repo: BranchRepository
) : ViewModel() {
    val _uiState = MutableStateFlow(BranchUiState())
    val uiState = _uiState.asStateFlow()

    init { loadBranches() }

    fun loadBranches() {
        viewModelScope.launch {
            val branches = repo.getBranchs()
            _uiState.update { it.copy(branches = branches) }
        }
    }

    fun removeBranch(branchId: Int) {
        viewModelScope.launch {
            val (status, message) = repo.removeBranch(branchId)

        }
    }

    fun fillFormFields(
        branchId: Int? = null,
        branchName: String? = null,
        branchAddress: String? = null
    ) {
        branchId?.let { newValue -> _uiState.update { it.copy(branchId = newValue) } }
        branchName?.let { newValue -> _uiState.update { it.copy(branchName = newValue) } }
        branchAddress?.let { newValue -> _uiState.update { it.copy(branchAddress = newValue) } }
    }

    fun resetForm() {
        _uiState.update { it.copy(
            branchId = 0,
            branchName = "",
            branchAddress = ""
        ) }
    }

    fun setFormError(field: FormInputName, error: String) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(
                formErrors = it.commonUiState.formErrors.toMutableMap().apply { put(field, error) }
            ))
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

    fun openAlertDialog(setOpen: Boolean) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(showAlertDialog = setOpen))
        }
    }

}