package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.NotificationRepository
import com.samuelsumbane.ssptdesktop.kclient.IdAndReadState
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.NotificationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(private val repo: NotificationRepository) : ViewModel() {
    val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            val notifications = repo.getNotifications()
            _uiState.update { it.copy(notifications = notifications) }
        }
    }

    fun editNotification(idAndReadState: IdAndReadState) {
        viewModelScope.launch { repo.editNotification(idAndReadState) }
    }

    fun removeNotification(notificationId: Int) {
        viewModelScope.launch {
            val (status, message) = repo.removeNotificaton(notificationId)
        }
    }

    fun fillNotificationForm(
        id: Int? = null,
        userName: String? = null,
        title: String? = null,
        message: String? = null,
        type: String? = null,
        createdAt: String? = null,
        showModal: Boolean? = null,
        setNotificationReadValue: Boolean? = null,
        markASNotReadCheckBox: Boolean? = null
    ) {
        id?.let { newValue -> _uiState.update { it.copy(notificationId = newValue) } }
        userName?.let { newValue -> _uiState.update { it.copy(notificationUserName = newValue) } }
        title?.let { newValue -> _uiState.update { it.copy(notificationTitle = newValue) } }
        message?.let { newValue -> _uiState.update { it.copy(notificationMessage = newValue) } }
        type?.let { newValue -> _uiState.update { it.copy(notificationType = newValue) } }
        createdAt?.let { newValue -> _uiState.update { it.copy(notificationCreatedAt = newValue) } }
        showModal?.let { newValue -> _uiState.update { it.copy(showModal = newValue) } }
        setNotificationReadValue?.let { newValue -> _uiState.update { it.copy(setNotificationReadValue = newValue) } }
        markASNotReadCheckBox?.let { newValue -> _uiState.update { it.copy(markASNotReadCheckBox = newValue) } }
    }

    fun onDismissNotificationModal() {
        editNotification(
            IdAndReadState(
                uiState.value.notificationId,
                isRead = !uiState.value.setNotificationReadValue
            )
        )
        loadNotifications()
        fillNotificationForm(showModal = false)
    }

}