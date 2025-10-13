package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.kclient.BranchItem
import com.samuelsumbane.ssptdesktop.domain.repository.BranchRepository
import com.samuelsumbane.ssptdesktop.kclient.ClientItem
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.apiBranchesPath
import com.samuelsumbane.ssptdesktop.kclient.apiClientsPath
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class BranchRepositoryImpl : BranchRepository {

    val kClientRepo = KClientRepository()

    val token = Session.jwtToken ?: ""

    override suspend fun getBranchs(): List<BranchItem> {
        return try {
            kClientRepo.httpClient.get("$apiBranchesPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching data: ${e.message}")
            emptyList()
        }
    }

    override suspend fun addBranch(branch: BranchItem): StatusAndMessage {
        return StatusAndMessage(100, "")
    }

    override suspend fun editBranch(branch: BranchItem): StatusAndMessage {
        return StatusAndMessage(100, "")
    }

    override suspend fun removeBranch(branchId: Int): StatusAndMessage {
        return StatusAndMessage(100, "")
    }
}