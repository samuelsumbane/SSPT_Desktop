package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class SaleReportViewModel : ViewModel() {
    val _uiState = MutableStateFlow(SaleReportUiState())
    val uiState = _uiState.asStateFlow()
    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()


    init {

    }

    suspend fun getSaleReports(): List<SaleReportItem> {
        return try {
            kClientRepo.httpClient.get("$apiReportPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching sale Reports: $e")
            emptyList()
        }
    }

    fun loadSaleReports() {
        viewModelScope.launch {
            val reportSales = getSaleReports()
            _uiState.update { it.copy(reportSales = reportSales) }
        }
    }
}