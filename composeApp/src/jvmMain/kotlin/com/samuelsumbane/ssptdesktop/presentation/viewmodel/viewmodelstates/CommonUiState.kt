package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.ui.utils.AlertType
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName

data class CommonUiState(
    val showFormDialog: Boolean = false,
    val formDialogTitle: String = "",
    val submitButtonText: String = "",
    val showAlertDialog: Boolean = false,
    val alertTitle: String = "",
    val alertText: String = "",
    val alertType: AlertType = AlertType.SUCCESS,
    val alertOnAccept: () -> Unit = {},
    val formErrors: Map<FormInputName, String> = emptyMap(),
    val isLoading: Boolean = false
)
