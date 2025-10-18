package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.UserRepository
import com.samuelsumbane.ssptdesktop.kclient.ChangeStatusDC
import com.samuelsumbane.ssptdesktop.kclient.UserItem
import com.samuelsumbane.ssptdesktop.kclient.UserItemDraft
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.UserUiState
import com.samuelsumbane.ssptdesktop.ui.utils.AlertType
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(private val userRepo: UserRepository) : ViewModel() {
    val _uiState = MutableStateFlow(UserUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            val users = userRepo.getUsers()
            _uiState.update { it.copy(users = users) }
        }
    }

    fun changeUserStatus(userStatus: ChangeStatusDC) {
        viewModelScope.launch {
            val (status, message) = userRepo.changeUserStatus(userStatus)
        }
    }

    fun removeUser(userId: Int) {
        viewModelScope.launch {
            val (status, message) = userRepo.removeUser(userId)
        }
    }

    fun fillUserForm(
        name: String? = null,
        email: String? = null,
        role: String? = null,
        roleDropdownExpanded: Boolean? = null,
    ) {
        name?.let { newValue -> _uiState.update { it.copy(userName = newValue) } }
        email?.let { newValue -> _uiState.update { it.copy(userEmail = newValue) } }
        role?.let { newValue -> _uiState.update { it.copy(userRole = newValue) } }
        roleDropdownExpanded?.let { newValue -> _uiState.update { it.copy(roleDropdownExpanded = newValue) } }
    }

    fun onUserFormSubmit() {
        viewModelScope.launch {
            if (uiState.value.userName.isBlank()) {
                setFormError(FormInputName.Name, "O nome é obrigatório")
                return@launch
            }

            if (uiState.value.userEmail.isBlank()) {
                setFormError(FormInputName.Email, "O email é obrigatório")
                return@launch
            }

            val (status, message) = userRepo.addUser(UserItemDraft(
                name = uiState.value.userName,
                email = uiState.value.userEmail,
                role = uiState.value.userRole
            ))

            val alertTitle = when (status) {
                201 -> "Usuário adicionado"
                else -> ""
            }

            resetForm()
            openAlertDialog(true)
            showAlert(alertTitle, message) { openAlertDialog(false) }

        }
    }

    fun resetForm() {
        _uiState.update {
            it.copy(
                userName = "",
                userEmail = "",
                userRole = "",
            )
        }
        openFormDialog(false)
        /** Clear all errors */
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(formErrors = emptyMap()))
        }
        loadUsers()
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

    fun setFormError(field: FormInputName, error: String) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(
                formErrors = it.commonUiState.formErrors.toMutableMap().apply { put(field, error) }
            ))
        }
    }

    fun openAlertDialog(setOpen: Boolean) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(showAlertDialog = setOpen))
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