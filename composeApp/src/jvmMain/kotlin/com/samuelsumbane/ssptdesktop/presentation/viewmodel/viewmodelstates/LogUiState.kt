package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.LogItem

data class LogUiState(
    val logs: List<LogItem> = emptyList(),
    val logMessage: String = "",
    val logModule: String = "",
    val logLevel: String = "",
    val createdLogAt: String = "",
    val logUserName: String = "",
    val logMetadata: String = "",
    val showModal: Boolean = false
)

