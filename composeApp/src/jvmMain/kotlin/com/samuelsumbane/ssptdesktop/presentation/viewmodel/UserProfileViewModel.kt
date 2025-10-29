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
    val loggedUserId = 1

    init {
        getUserData(loggedUserId)
    }
    fun getUserData(userId: Int) {
        viewModelScope.launch {
            val userItem = repo.getUserById(userId)
            userItem?.let {
                _uiState.update { it.copy(userData = userItem) }
            }
        }
    }

    fun fillFields(
        userId: Int? = null,
        userName: String? = null,
        userEmail: String? = null,
        actualPassword: String? = null,
        newPassword: String? = null,
        confirmationPassword: String? = null,
        snackbarMessage: String? = null,
        showSnackbar: Boolean? = null
    ) {
        userId?.let { newValue -> _uiState.update { it.copy(userId = newValue) } }
        userName?.let { newValue -> _uiState.update { it.copy(newUserName = newValue) } }
        userEmail?.let { newValue -> _uiState.update { it.copy(newUserEmail = newValue) }}
        actualPassword?.let { newValue -> _uiState.update { it.copy(actualPassword = newValue) }}
        newPassword?.let { newValue -> _uiState.update { it.copy(newPassword = newValue) }}
        confirmationPassword?.let { newValue -> _uiState.update { it.copy(confirmationPassword = newValue) }}
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

    private fun onSubmitUserDataForm() {
        viewModelScope.launch {
            if (uiState.value.newUserName.isBlank()) {
                setFormError(FormInputName.Name, "O nome é obrigatório")
                return@launch
            } else {
                setFormError(FormInputName.Name, "")
            }

            if (uiState.value.newUserEmail.isBlank()) {
                setFormError(FormInputName.Email, "O email é obrigatório")
                return@launch
            } else {
                setFormError(FormInputName.Email, "")
            }

            val (status, message) = repo.editUserPersonalData(
                UpdateUserPersonalData(
                    userId = uiState.value.userId,
                    userName = uiState.value.newUserName,
                    userEmail = uiState.value.newUserEmail
                )
            )

            if (status == 201) {
                displaySnackbar(true, message)
                getUserData(loggedUserId)
            }

        }
    }

    private fun onSubmitUserPassword() {
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

    fun onSubmitForm() = if (uiState.value.showUserPersonalDataModal) onSubmitUserDataForm() else onSubmitUserPassword()

    fun resetForm() {
        _uiState.update {
            it.copy(userData = it.userData.copy(
                name = "",
                email = ""
            ))
        }
        openFormDialog(false)
    }

    fun setFormError(field: FormInputName, error: String) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(
                formErrors = it.commonUiState.formErrors.toMutableMap().apply { put(field, error) }
            ))
        }
    }

    fun openFormDialog(
        setOpen: Boolean,
        title: String = "",
        showUserPersonalDataForm: Boolean = true
    ) {
        _uiState.update {
            it.copy(
                showUserPersonalDataModal = showUserPersonalDataForm,
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