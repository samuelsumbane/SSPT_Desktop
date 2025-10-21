package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.ConfigRepository
import com.samuelsumbane.ssptdesktop.kclient.SysConfigItem
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.ConfigUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConfigViewModel(private val repo: ConfigRepository) : ViewModel() {
    val _uiState = MutableStateFlow(ConfigUiState())
    val uiState = _uiState.asStateFlow()

    fun loadConfigs() {
        viewModelScope.launch {
            val configs = repo.getConfigs()
            _uiState.update { it.copy(configs = configs) }
        }
    }

    fun getPackageName() {
        viewModelScope.launch {
            val (status, message) = repo.getPackageName()
            if (status == 200) _uiState.update { it.copy(packageName = message) }
        }
    }

    fun editConfiguration(
        maxSellValue: Double? = null,
        percentualIva: Int? = null,
        alertMinProQuantity: Boolean? = null,
        isNotifyMaxSellChecked: Boolean? = null,
        activePackage: String? = null
    ) {
        viewModelScope.launch {
            maxSellValue?.let { newValue ->
                _uiState.update { it.copy(notifyMaxSellValue = newValue) }
                if (newValue > 0.0) {
                    val (status, message) = repo.editConfig(
                        SysConfigItem(
                            id = 1,
                            key = "notifyMaxSellValue",
                            stringValue = null,
                            intValue = null,
                            doubleValue = newValue,
                            boolValue = null,
                            lastUpdate = ""
                        )
                    )
                }
            }
            percentualIva?.let { newValue ->
                _uiState.update { it.copy(percentualIva = newValue) }
                if (newValue > 0) {
                    val (status, message) = repo.editConfig(
                        SysConfigItem(
                            id = 1,
                            key = "percentual_iva",
                            stringValue = null,
                            intValue = 1,
                            doubleValue = null,
                            boolValue = null,
                            lastUpdate = ""
                        )
                    )
                }
            }

            alertMinProQuantity?.let { newValue ->
                _uiState.update { it.copy(alertMinProQuantity = newValue) }
                val (status, message) = repo.editConfig(
                    SysConfigItem(
                        id = 1,
                        key = "alert_min_pro_quantity",
                        stringValue = null,
                        intValue = null,
                        doubleValue = null,
                        boolValue = newValue,
                        lastUpdate = ""
                    )
                )
            }

            isNotifyMaxSellChecked?.let { newValue ->
                _uiState.update { it.copy(isNotifyMaxSellChecked = newValue) }
                val (status, message) = repo.editConfig(
                    SysConfigItem(
                        id = 1,
                        key = "notifyMaxSell",
                        stringValue = null,
                        intValue = null,
                        doubleValue = null,
                        boolValue = newValue,
                        lastUpdate = ""
                    )
                )

            }

            activePackage?.let { newValue ->
                _uiState.update { it.copy(activePackage = newValue) }
                val (status, message) = repo.editConfig(
                    SysConfigItem(
                        id = 1,
                        key = "active_package",
                        stringValue = newValue,
                        intValue = null,
                        doubleValue = null,
                        boolValue = null,
                        lastUpdate = ""
                    )
                )
            }
        }
    }

    fun setLoading(value: Boolean) {
        _uiState.update { it.copy(commonUiState = it.commonUiState.copy(isLoading = value)) }
    }
}