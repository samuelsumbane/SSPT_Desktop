package com.samuelsumbane.ssptdesktop.data.repository

//import com.samuelsumbane.ssptdesktop.ClientItem
//import com.samuelsumbane.ssptdesktop.module
import com.samuelsumbane.ssptdesktop.kclient.ClientItem
import com.samuelsumbane.ssptdesktop.domain.repository.ClientRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.apiClientsPath
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class ClientRepositoryImpl : ClientRepository {
    val token = Session.jwtToken ?: ""

    val kClientRepo = KClientRepository()


    override suspend fun getClients(): List<ClientItem> {
        return try {
            kClientRepo.httpClient.get("$apiClientsPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching data: ${e.message}")
            emptyList()
        }
    }

    override suspend fun AddClient(client: ClientItem): StatusAndMessage {
//        _clients.value += client
        val (status, message) = kClientRepo.postRequest("$apiClientsPath/create", client)
        return StatusAndMessage(status, message)
    }

    override suspend fun EditClient(client: ClientItem): StatusAndMessage {
//        _clients.value = _clients.value.map {
//            if (it.id == client.id) client else it
//        }
        val (status, message) = kClientRepo.postRequest("$apiClientsPath/edit", client, "put")
        return StatusAndMessage(status, message)
    }
}