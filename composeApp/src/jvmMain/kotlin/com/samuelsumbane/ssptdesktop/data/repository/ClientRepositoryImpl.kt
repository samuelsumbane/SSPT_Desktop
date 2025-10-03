package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.ClientItem
import com.samuelsumbane.ssptdesktop.domain.usecase.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ClientRepositoryImpl : ClientRepository {

    private val _clients = MutableStateFlow(
        listOf(
            ClientItem(1, "dfsa", "8932")
        )
    )

    override fun getClients(): Flow<List<ClientItem>> = _clients

    override suspend fun AddClient(client: ClientItem) {
        _clients.value += client
    }

    override suspend fun EditClient(client: ClientItem) {
        _clients.value = _clients.value.map {
            if (it.id == client.id) client else it
        }
    }
}