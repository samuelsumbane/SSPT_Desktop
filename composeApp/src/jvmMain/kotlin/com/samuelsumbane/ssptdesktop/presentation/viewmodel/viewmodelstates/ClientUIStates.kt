package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.ClientItem

data class ClientUIStates(
    val clientId: Int = 0,
    val clientName: String = "",
    val clientPhone: String = "",
    val clients: List<ClientItem> = emptyList(),
    val common: CommonUiState = CommonUiState(),
)
