package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.domain.repository.ConfigRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.SysConfigItem
import com.samuelsumbane.ssptdesktop.kclient.apiSettingsPath
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class ConfigRepositoryImpl : ConfigRepository {
    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()

    override suspend fun getConfigs(): List<SysConfigItem> {
        return try {
            kClientRepo.httpClient.get("$apiSettingsPath/get") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching configurations data $e")
            emptyList()
        }
    }

    override suspend fun getPackageName(): StatusAndMessage {
        return try {
            kClientRepo.httpClient.get("$apiSettingsPath/package-name") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching configurations data $e")
            StatusAndMessage(0, "")
        }
    }

    override suspend fun editConfig(sysConfigItem: SysConfigItem): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiSettingsPath/edit", "put")
        return StatusAndMessage(status, message)
    }
}