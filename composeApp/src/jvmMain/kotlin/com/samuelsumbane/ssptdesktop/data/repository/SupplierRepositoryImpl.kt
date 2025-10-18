package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.domain.repository.SupplierRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.SupplierItem
import com.samuelsumbane.ssptdesktop.kclient.apiSupplierPath
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import org.jetbrains.annotations.Async

class SupplierRepositoryImpl : SupplierRepository {
    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()

    override suspend fun getSuppliers(): List<SupplierItem> {
        return try {
            kClientRepo.httpClient.get("$apiSupplierPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching suppliers data: $e")
            emptyList()
        }
    }

    override suspend fun addSupplier(supplier: SupplierItem): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiSupplierPath/create", supplier)
        return StatusAndMessage(status, message)
    }

    override suspend fun editSupplier(supplier: SupplierItem): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiSupplierPath/edit", supplier)
        return StatusAndMessage(status, message)
    }

    override suspend fun removeSupplier(supplierId: Int): StatusAndMessage {
        val (status, message) = kClientRepo.deleteRequest("$apiSupplierPath/delete/$supplierId")
        return StatusAndMessage(status, message)
    }
}