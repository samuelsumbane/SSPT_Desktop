package com.samuelsumbane.ssptdesktop.domain.usecase

import com.samuelsumbane.ssptdesktop.ClientItem
import com.samuelsumbane.ssptdesktop.domain.usecase.repository.ClientRepository
import kotlinx.coroutines.flow.Flow


class GetClientsUseCase(private val clientRepository: ClientRepository) {
    operator fun invoke() : Flow<List<ClientItem>> = clientRepository.getClients()
}


class AddClientUseCase(private val clientRepository: ClientRepository) {
    suspend operator fun invoke(client: ClientItem) = clientRepository.AddClient(client)
}

class EditClientUseCase(private val clientRepository: ClientRepository) {
    suspend operator fun invoke(client: ClientItem) = clientRepository.EditClient(client)
}