package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.ClientItem
import com.samuelsumbane.ssptdesktop.domain.usecase.AddClientUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.EditClientUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetClientsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClientViewModel(
    private val getClients: GetClientsUseCase,
    private val addClient: AddClientUseCase,
    private val editClient: EditClientUseCase
) : ViewModel() {

    val allClients: StateFlow<List<ClientItem>> = getClients()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addTheClient(client: ClientItem) = viewModelScope.launch { addClient(client) }
    fun editTheClient(client: ClientItem) = viewModelScope.launch { editClient(client) }

}