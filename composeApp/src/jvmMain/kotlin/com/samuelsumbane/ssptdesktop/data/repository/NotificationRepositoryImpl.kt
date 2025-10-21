package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.domain.repository.NotificationRepository
import com.samuelsumbane.ssptdesktop.kclient.IdAndReadState
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.NotificationItem
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.apiNotificationsPath
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

class NotificationRepositoryImpl : NotificationRepository{
    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()

    override suspend fun getNotifications(): List<NotificationItem> {
        return try {
            kClientRepo.httpClient.get("$apiNotificationsPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching notifications data $e")
            emptyList()
        }
    }

    override suspend fun editNotification(idAndReadState: IdAndReadState): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiNotificationsPath/edit", idAndReadState)
        return StatusAndMessage(status, message)
    }

    override suspend fun removeNotificaton(notificationId: Int): StatusAndMessage {
        val (status, message) = kClientRepo.deleteRequest("$apiNotificationsPath/delete/$notificationId")
        return StatusAndMessage(status, message)
    }
}