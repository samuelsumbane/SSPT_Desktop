package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.UserRepository
import com.samuelsumbane.ssptdesktop.kclient.PasswordDraft
import com.samuelsumbane.ssptdesktop.kclient.UpdateUserPersonalData
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.UserProfileUiState
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel(private val repo: UserRepository) : ViewModel() {

    val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun fillFields(
        userName: String? = null,
        userEmail: String? = null,
        showUserPersonalDataModal: Boolean? = false,
        formModalTitle: String? = null,
        actualPassword: String? = null,
        newPassword: String? = null,
        confirmationPassword: String? = null,
        snackbarMessage: String? = null,
        showSnackbar: Boolean? = null
    ) {
        userName?.let { newValue -> _uiState.update { it.copy(userName = newValue) }}
        userEmail?.let { newValue -> _uiState.update { it.copy(userEmail = newValue) }}
        showUserPersonalDataModal?.let { newValue -> _uiState.update { it.copy(showUserPersonalDataModal = showUserPersonalDataModal) }}
        actualPassword?.let { newValue -> _uiState.update { it.copy(actualPassword = newValue) }}
        newPassword?.let { newValue -> _uiState.update { it.copy(newPassword = newValue) }}
        confirmationPassword?.let { newValue -> _uiState.update { it.copy(confirmationPassword = newValue) }}

        formModalTitle?.let { newValue ->
            _uiState.update { it.copy(commonUiState = it.commonUiState.copy(formDialogTitle = formModalTitle)) }
        }
    }

    fun displaySnackbar(showSnackbar: Boolean, snackbarMessae: String? = null) {
            _uiState.update {
                it.copy(
                    commonUiState = it.commonUiState.copy(
                        showSnackbar = showSnackbar,
                        snackbarMessage = snackbarMessae ?: ""
                    )
                )
            }
    }

    fun onSubmitUserDataForm() {
        viewModelScope.launch {
            if (uiState.value.userName.isBlank()) {
                setFormError(FormInputName.Name, "O nome é obrigatório")
                return@launch
            } else {
                setFormError(FormInputName.Name, "")
            }

            if (uiState.value.userEmail.isBlank()) {
                setFormError(FormInputName.Email, "O email é obrigatório")
                return@launch
            } else {
                setFormError(FormInputName.Email, "")
            }

            val (status, message) = repo.editUserPersonalData(
                UpdateUserPersonalData(
                    userId = uiState.value.userId,
                    userName = uiState.value.userName,
                    userEmail = uiState.value.userEmail
                )
            )

            if (status == 201) {
                displaySnackbar(true, message)
            }

        }
    }

    fun onSubmitUserPassword() {
        viewModelScope.launch {
            if (uiState.value.actualPassword.isBlank()) {
                setFormError(FormInputName.CurrentPassword, "A senha é obrigatória")
                return@launch
            } else {
                setFormError(FormInputName.CurrentPassword, "")
            }

            if (uiState.value.newPassword.isBlank()) {
                setFormError(FormInputName.NewPassword, "Nova senha é obrigatória")
                return@launch
            } else {
                setFormError(FormInputName.NewPassword, "")
            }

            if (uiState.value.confirmationPassword.isBlank()) {
                setFormError(FormInputName.ConfirmationPassword, "Por favor, confirme a senha")
                return@launch
            } else {
                setFormError(FormInputName.ConfirmationPassword, "")
            }

            val (status, message) = repo.editUserPassword(
                PasswordDraft(
                    userId = uiState.value.userId,
                    hashedPassword = null,
                    newPassword = uiState.value.newPassword
                )
            )

            if (status == 201) {
                displaySnackbar(true, message)
            }
        }
    }

    fun resetForm() {
        _uiState.update { it.copy(userName = "", userEmail = "") }
        openFormDialog(false)
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