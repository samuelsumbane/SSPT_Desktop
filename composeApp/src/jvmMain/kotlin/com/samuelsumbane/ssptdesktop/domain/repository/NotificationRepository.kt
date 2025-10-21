package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.IdAndReadState
import com.samuelsumbane.ssptdesktop.kclient.NotificationItem
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage

interface NotificationRepository {
    suspend fun getNotifications(): List<NotificationItem>
    suspend fun editNotification(idAndReadState: IdAndReadState): StatusAndMessage
    suspend fun removeNotificaton(notificationId: Int): StatusAndMessage
}