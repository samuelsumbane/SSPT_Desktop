package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.BranchItem

data class BranchUiState(
    val branches: List<BranchItem> = emptyList(),
    val branchId: Int = 0,
    val branchName: String = "",
    val branchAddress: String = "",
    val commonUiState: CommonUiState = CommonUiState()
)
