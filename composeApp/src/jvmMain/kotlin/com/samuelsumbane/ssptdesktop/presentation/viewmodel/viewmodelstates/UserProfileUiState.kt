package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.UserItem

data class UserProfileUiState(
    val userId: Int = 0,
    val newUserName: String = "",
    val newUserEmail: String = "",
//    val userRole: String = "",
//    val userState: String = "",
//    val lastLogin: String = "",
    val showUserPersonalDataModal: Boolean = false,
    val passwordMatches: Boolean = false,
    val actualPassword: String = "",
    val newPassword: String = "",
    val confirmationPassword: String = "",
    val userData: UserItem = UserItem(0, "", "", "", "", "", ""),
    val commonUiState: CommonUiState = CommonUiState()
)