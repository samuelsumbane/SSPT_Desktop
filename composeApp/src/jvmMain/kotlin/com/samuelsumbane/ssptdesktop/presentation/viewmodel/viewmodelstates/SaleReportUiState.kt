package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.SaleReportItem

data class SaleReportUiState(
    val reportSales: List<SaleReportItem> = emptyList(),

)