package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.ClientItem
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    suspend fun getClients(): List<ClientItem>
    suspend fun AddClient(client: ClientItem): StatusAndMessage
    suspend fun EditClient(client: ClientItem): StatusAndMessage
}