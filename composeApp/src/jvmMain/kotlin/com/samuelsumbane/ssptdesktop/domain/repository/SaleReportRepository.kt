package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.SaleReportItem
import kotlinx.serialization.json.JsonObject

interface SaleReportRepository {
    suspend fun fetchSalesReports(): List<SaleReportItem>
    suspend fun getTotalProfitAndSales(): Pair<Double, Double>
    suspend fun getTotalClientAndSuppliers(): Pair<Int, Int>
    suspend fun getUsersTotalSales(): List<JsonObject>
    suspend fun getEachProductTotalProfit(): List<JsonObject>
    suspend fun getTotalQuantitiesByMonthAndYear(): List<JsonObject>
    suspend fun getProductsMostBoughts(): List<JsonObject>
    suspend fun fetchDateTimeSales(
        initialDate: String,
        initialTime: String,
        finalDate: String,
        finalTime: String,
        ownerId: String,
    ): List<SaleReportItem>
}