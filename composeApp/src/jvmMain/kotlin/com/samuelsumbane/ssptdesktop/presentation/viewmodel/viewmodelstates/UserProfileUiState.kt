package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

data class UserProfileUiState(
    val userId: Int = 0,
    val userName: String = "",
    val userEmail: String = "",
    val userRole: String = "",
    val userState: String = "",
    val lastLogin: String = "",
    val showUserPersonalDataModal: Boolean = false,
    val passwordMatches: Boolean = false,
    val actualPassword: String = "",
    val newPassword: String = "",
    val confirmationPassword: String = "",

    val commonUiState: CommonUiState = CommonUiState()
)