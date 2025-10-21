package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.SysConfigItem

data class ConfigUiState(
    val configs: List<SysConfigItem> = emptyList(),
    val packageName: String = "",
    val notifyMaxSellValue: Double = 0.0,
    val percentualIva: Int = 0,
    val alertMinProQuantity: Boolean = false,
    val isNotifyMaxSellChecked: Boolean = false,
    val activePackage: String = "",
    val commonUiState: CommonUiState = CommonUiState()
)
