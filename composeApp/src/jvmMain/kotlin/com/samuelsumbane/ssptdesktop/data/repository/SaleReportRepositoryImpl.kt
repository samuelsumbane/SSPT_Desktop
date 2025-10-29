package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.domain.repository.SaleReportRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.SaleReportItem
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.apiReportPath
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.JsonObject

class SaleReportRepositoryImpl : SaleReportRepository {
    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()

    override suspend fun fetchSalesReports(): List<SaleReportItem> {
        return try {
            kClientRepo.httpClient.get("$apiReportPath/all") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }.body()
        } catch (e: Exception) {
            println("Error fetching sale Reports: $e")
            emptyList()
        }
    }

    override suspend fun getTotalProfitAndSales(): Pair<Double, Double> {
        return kClientRepo.httpClient.get("$apiReportPath/totalProfitAndSales").body()
    }

    override suspend fun getTotalClientAndSuppliers(): Pair<Int, Int> {
        return kClientRepo.httpClient.get("$apiReportPath/totalClientsAndSuppliers").body()
    }

    override suspend fun getUsersTotalSales(): List<JsonObject> {
        return kClientRepo.httpClient.get("$apiReportPath/usersTotalSales").body()
    }

    override suspend fun getEachProductTotalProfit(): List<JsonObject> {
        return kClientRepo.httpClient.get("$apiReportPath/eachProductTotalProfit").body()
    }

    override suspend fun getTotalQuantitiesByMonthAndYear(): List<JsonObject> {
        return kClientRepo.httpClient.get("$apiReportPath/totalQuantitiesByMonthAndYear").body()
    }

    override suspend fun getProductsMostBoughts(): List<JsonObject> {
        return kClientRepo.httpClient.get("$apiReportPath/productsMostBoughts").body()
    }

    override suspend fun fetchDateTimeSales(
        initialDate: String,
        initialTime: String,
        finalDate: String,
        finalTime: String,
        ownerId: String,
    ): List<SaleReportItem> {
        return kClientRepo.httpClient.get("$apiReportPath/filteredSalesByDates/$initialDate/$initialTime/$finalDate/$finalTime/$ownerId?timezoneid=Africa/Harare").body()
    }
}