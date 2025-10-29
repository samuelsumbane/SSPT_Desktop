package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.SaleReportRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.SaleReportItem
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.apiReportPath
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.SaleReportUiState
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SaleReportViewModel(private val repo: SaleReportRepository) : ViewModel() {
    val _uiState = MutableStateFlow(SaleReportUiState())
    val uiState = _uiState.asStateFlow()
    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()


    init {
        loadSaleReports()
    }

    fun loadSaleReports() {
        viewModelScope.launch {
            val reportSales = repo.fetchSalesReports()
            _uiState.update { it.copy(reportSales = reportSales) }
        }
    }

    fun fillSaleReportFields(
        expandOptionsTodaySales: Boolean? = null,
        expandOptionsExportData: Boolean? = null,
    ) {
        expandOptionsTodaySales?.let { newValue -> _uiState.update { it.copy(expandOptionsTodaySales = newValue) }}
        expandOptionsExportData?.let { newValue -> _uiState.update { it.copy(expandOptionsExportData = newValue) }}
    }
}