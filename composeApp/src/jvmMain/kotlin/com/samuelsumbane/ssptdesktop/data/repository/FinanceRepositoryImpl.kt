package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.domain.repository.FinanceRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.PayableDraft
import com.samuelsumbane.ssptdesktop.kclient.PayableItem
import com.samuelsumbane.ssptdesktop.kclient.ReceivableDraft
import com.samuelsumbane.ssptdesktop.kclient.ReceivableItem
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.apiPayablesPath
import com.samuelsumbane.ssptdesktop.kclient.apiReceivablesPath
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class FinanceRepositoryImpl : FinanceRepository {
    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()

    override suspend fun getPayables(): List<PayableItem> {
        return try {
            kClientRepo.httpClient.get("$apiPayablesPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching payable data : $e")
            emptyList()
        }
    }

    override suspend fun getReceivables(): List<ReceivableItem> {
        return try {
            kClientRepo.httpClient.get("$apiReceivablesPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching receivable data : $e")
            emptyList()
        }
    }

    override suspend fun addPayable(payable: PayableDraft): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiPayablesPath/create", payable)
        return StatusAndMessage(status, message)
    }

    override suspend fun addReceivable(receivable: ReceivableDraft): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiReceivablesPath/create", receivable, "put")
        return StatusAndMessage(status, message)
    }
}