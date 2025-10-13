package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.kclient.OwnerItem
import com.samuelsumbane.ssptdesktop.domain.repository.ProOwnerRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.apiOwnersPath
import com.samuelsumbane.ssptdesktop.kclient.apiProductsPath
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ProOwnerRepositoryImpl : ProOwnerRepository{

    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()

    override suspend fun getProOwners(): List<OwnerItem> {
        return try {
            kClientRepo.httpClient.get("$apiOwnersPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch(e: Exception) {
            println("Error fetching data: $e")
            emptyList()
        }
    }

    override suspend fun addOwner(proOwner: OwnerItem): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiProductsPath/create", proOwner)
        return StatusAndMessage(status, message)
    }

    override suspend fun editOwner(proOwner: OwnerItem): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiProductsPath/edit", proOwner, "put")
        return StatusAndMessage(status, message)
    }

    override suspend fun removeOwner(proOwnerId: Int): StatusAndMessage {
        val (status, message) = kClientRepo.deleteRequest("$apiProductsPath/delete/$proOwnerId")
        return StatusAndMessage(status, message)
    }
}