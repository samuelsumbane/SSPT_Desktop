package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import cafe.adriel.voyager.core.screen.Screen
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName

data class LoginUserUiState(
    val userEmail: String = "",
    val userPassword: String = "",
    val errorMessage: String = "",
    val redirectTo: String? = null,
    val formErrors: Map<FormInputName, String> = emptyMap()
)
