package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.ClientRepository
import com.samuelsumbane.ssptdesktop.kclient.ClientItem
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.ClientUIStates
import com.samuelsumbane.ssptdesktop.ui.utils.AlertType
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClientViewModel(
    private val repo: ClientRepository
) : ViewModel() {

    val _uiStates = MutableStateFlow(ClientUIStates())
    val uiStates = _uiStates.asStateFlow()

    init {
        loadClients()
    }

    fun loadClients() {
        viewModelScope.launch {
            val newClientList = repo.getClients()
            _uiStates.update { it.copy(clients = newClientList) }
        }
    }

//    fun addTheClient(client: ClientItem) = viewModelScope.launch { repo.addClient(client) }
//    fun editTheClient(client: ClientItem) = viewModelScope.launch { repo.editClient(client) }

    fun onSubmitClientForm() {
        viewModelScope.launch {

            if (uiStates.value.clientName.isBlank()) {
                setFormError(FormInputName.ClientName, "O nome é obrigatório")
                return@launch
            } else {
                setFormError(FormInputName.ClientName, "")
            }

            if (uiStates.value.clientPhone.isBlank()) {
                setFormError(FormInputName.ClientPhone, "O telefone é obrigatório")
                return@launch
            } else {
                setFormError(FormInputName.ClientPhone, "")
            }

            val clientItem = ClientItem(
                id = if (uiStates.value.clientId != 0) uiStates.value.clientId else 0,
                name = uiStates.value.clientName,
                telephone = uiStates.value.clientPhone
            )


            val (status, message) = if (uiStates.value.clientId != 0) repo.AddClient(clientItem) else repo.AddClient(clientItem)

            val title = when (status) {
                200 -> "Sucesso"
                else -> ""
            }

            resetForm()

            showAlert(title, message) {
                openAlertDialog(false)
            }
        }
    }

    fun setFormError(field: FormInputName, error: String) {
        _uiStates.update {
            it.copy(common = it.common.copy(
                formErrors = it.common.formErrors.toMutableMap().apply { put(field, error) }
            ))
        }
    }

    fun openFormDialog(setOpen: Boolean, title: String = "") {
        _uiStates.update {
            it.copy(
                common = it.common.copy(
                    showFormDialog = setOpen,
                    formDialogTitle = title
                )
            )
        }
    }

    fun openAlertDialog(setOpen: Boolean) {
        _uiStates.update {
            it.copy(common = it.common.copy(showAlertDialog = setOpen))
        }
    }

    fun setClientIdData(id: Int) {
        _uiStates.update { it.copy(clientId = id) }
    }

    fun setClientNameData(name: String) {
        _uiStates.update { it.copy(clientName = name) }
    }

    fun setClientPhoneData(phone: String) {
        _uiStates.update { it.copy(clientPhone = phone) }
    }

    fun fillAllForm(
        id: Int,
        name: String,
        phone: String
    ) {
        _uiStates.update {
            it.copy(
                clientId = id,
                clientName = name,
                clientPhone = phone
            )
        }
    }

    fun resetForm() {
        _uiStates.update {
            it.copy(
                clientId = 0,
                clientName = "",
                clientPhone = ""
            )
        }
        openFormDialog(false)
        /** Clear all erros */
        _uiStates.update {
            it.copy(common = it.common.copy(
                formErrors = emptyMap()
            ))
        }
        loadClients()
    }

    fun showAlert(
        title: String,
        text: String,
        alertType: AlertType = AlertType.SUCCESS,
        onAccept: () -> Unit
    ) {
        _uiStates.update {
            it.copy(common = it.common.copy(
                alertTitle = title,
                alertText = text,
                alertType = alertType,
                showAlertDialog = true,
                alertOnAccept = onAccept
            ))
        }
    }


}