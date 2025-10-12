package com.samuelsumbane.ssptdesktop.data.repository

//import com.samuelsumbane.ssptdesktop.ClientItem
//import com.samuelsumbane.ssptdesktop.module
import com.samuelsumbane.ssptdesktop.kclient.ClientItem
import com.samuelsumbane.ssptdesktop.domain.repository.ClientRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.apiClientsPath
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class ClientRepositoryImpl : ClientRepository {
    val token = Session.jwtToken ?: ""

    val common = KClientRepository()

    val httpClient = HttpClient(io.ktor.client.engine.cio.CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

//    override fun getClients(): Flow<List<ClientItem>> = _clients
    override fun getClients(): Flow<List<ClientItem>> = flow {
        try {
            val clients: List<ClientItem> = httpClient.get("$apiClientsPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
            println("data are: $clients")
            emit(clients)
        } catch (e: Exception) {
            println("Erro ao buscar pedidos: ${e.message}")
            emit(emptyList())
        }
    }

    override suspend fun AddClient(client: ClientItem): StatusAndMessage {
//        _clients.value += client
        val (status, message) = common.postRequest("$apiClientsPath/create", client)
        return StatusAndMessage(status, message)
    }

    override suspend fun EditClient(client: ClientItem): StatusAndMessage {
//        _clients.value = _clients.value.map {
//            if (it.id == client.id) client else it
//        }
        val (status, message) = common.postRequest("$apiClientsPath/edit", client, "put")
        return StatusAndMessage(status, message)
    }
}