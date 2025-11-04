package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.NotificationItem

data class NotificationUiState(
    val notifications: List<NotificationItem> = emptyList(),
    val notificationId: Int = 0,
    val notificationUserName: String = "",
    val notificationTitle: String = "",
    val notificationMessage: String = "",
    val notificationType: String = "",
    val notificationCreatedAt: String = "",
    val showModal: Boolean = false,
    val setNotificationReadValue: Boolean = true,
    val markASNotReadCheckBox: Boolean = false,
)
