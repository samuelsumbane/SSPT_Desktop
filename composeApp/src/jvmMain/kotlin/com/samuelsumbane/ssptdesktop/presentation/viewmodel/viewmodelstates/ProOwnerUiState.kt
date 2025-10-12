package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.OwnerItem

data class ProOwnerUiState(
    val proOwnerId: Int = 0,
    val proOwnerName: String = "",
    val proOwnerTelephone: String = "",
    val allProOwners: List<OwnerItem> = emptyList(),
    val commonUiState: CommonUiState = CommonUiState()
)
