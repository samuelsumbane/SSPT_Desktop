package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.LogItem
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.apiLogsPath
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.LogUiState
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

/**
 * Logs will only have ViewModel and LogUiState
 */

class LogViewModel : ViewModel() {
    val _uiState = MutableStateFlow(LogUiState())
    val uiState= _uiState.asStateFlow()
    val kClientRepo = KClientRepository()
    val token = Session.jwtToken ?: ""

    init {
        loadLogs()
    }

    private suspend fun getLogs(): List<LogItem> {
        return try {
            kClientRepo.httpClient.get("$apiLogsPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching logs $e")
            emptyList()
        }
    }

    fun loadLogs() {
        viewModelScope.launch {
            val logs = getLogs()
            _uiState.update { it.copy(logs = logs) }
        }
    }

    fun fillLogs(
        message: String? = null,
        module: String? = null,
        level: String? = null,
        createdAt: String? = null,
        userName: String? = null,
        metadata: String? = null,
        showModal: Boolean = false,
    ) {
        message?.let { newValue -> _uiState.update { it.copy(logMessage = newValue) } }
        module?.let { newValue -> _uiState.update { it.copy(logModule = newValue) } }
        level?.let { newValue -> _uiState.update { it.copy(logLevel = newValue) } }
        createdAt?.let { newValue -> _uiState.update { it.copy(createdLogAt = newValue) } }
        userName?.let { newValue -> _uiState.update { it.copy(logUserName = newValue) } }
        metadata?.let { newValue -> _uiState.update { it.copy(logMetadata = newValue) } }
        showModal?.let { newValue -> _uiState.update { it.copy(showModal = newValue) } }
    }
}

