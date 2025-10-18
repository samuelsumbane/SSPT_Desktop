package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.UserItem

data class UserUiState(
    val users: List<UserItem> = emptyList(),
    val userName: String = "",
    val userEmail: String = "",
    val userRole: String = "",
    val commonUiState: CommonUiState = CommonUiState()
)
