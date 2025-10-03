package com.samuelsumbane.ssptdesktop.domain.usecase.repository

import com.samuelsumbane.ssptdesktop.ClientItem
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun getClients(): Flow<List<ClientItem>>
    suspend fun AddClient(client: ClientItem)
    suspend fun EditClient(client: ClientItem)
}