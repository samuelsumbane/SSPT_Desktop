package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.ProOwnerRepository
import com.samuelsumbane.ssptdesktop.kclient.OwnerItem
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.ProOwnerUiState
import com.samuelsumbane.ssptdesktop.ui.utils.AlertType
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProOwnerViewModel(
    private val ownerRepo: ProOwnerRepository
) : ViewModel() {
    val _uiState = MutableStateFlow(ProOwnerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProOwners()
    }

    fun loadProOwners() {
        viewModelScope.launch {
            val proOwners = ownerRepo.getProOwners()
            _uiState.update { it.copy(allProOwners = proOwners) }
        }
    }

    fun removeProOwner(proOwnerId: Int) {
        viewModelScope.launch { ownerRepo.removeOwner(proOwnerId) }
    }

    fun setFormError(field: FormInputName, error: String) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(
                formErrors = it.commonUiState.formErrors.toMutableMap().apply { put(field, error) }
            ))
        }
    }

    fun onSubmitForm() {
        viewModelScope.launch {
            if (uiState.value.proOwnerName.isBlank()) {
                setFormError(FormInputName.OwnerName, "O nome do proprietario é obrigatorio")
                return@launch
            } else {
                setFormError(FormInputName.OwnerName, "")
            }

            if (uiState.value.proOwnerTelephone.isBlank()) {
                setFormError(FormInputName.OwnerPhone, "O telefone é obrigatorio")
                return@launch
            } else {
                setFormError(FormInputName.OwnerPhone, "")
            }

            val proOwnerItem = OwnerItem(
                id = uiState.value.proOwnerId,
                name = uiState.value.proOwnerName,
                telephone = uiState.value.proOwnerTelephone,
            )

            val (status, message) = if (uiState.value.proOwnerId != 0) ownerRepo.editOwner(proOwnerItem) else ownerRepo.addOwner(proOwnerItem)
            val alertTitle = when (status) {
                200 -> "Proprietario actualizado"
                201 -> "Proprietario adicionado"
                else -> ""
            }

            resetForm()

            showAlert(alertTitle, message) {
                openAlertDialog(false)
            }
        }
    }

    fun resetForm() {
        openFormDialog(false)
        _uiState.update {
            it.copy(
                proOwnerId = 0,
                proOwnerName = "",
                proOwnerTelephone = "",
                commonUiState = it.commonUiState.copy(formErrors = emptyMap())
            )
        }
        loadProOwners()
    }

    fun fillFormFields(
        id: Int? = null,
        name: String? = null,
        telephone: String? = null
    ) {
        id?.let { _uiState.update { it.copy(proOwnerId = id) } }
        name?.let { _uiState.update { it.copy(proOwnerName = name) } }
        telephone?.let { _uiState.update { it.copy(proOwnerTelephone = telephone) } }
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
            it.copy(
                commonUiState = it.commonUiState.copy(showAlertDialog = setOpen)
            )
        }
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
}