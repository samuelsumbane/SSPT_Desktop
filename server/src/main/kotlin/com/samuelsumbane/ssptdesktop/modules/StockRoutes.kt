package com.samuelsumbane.ssptdesktop.modules

import com.samuelsumbane.infrastructure.repositories.StockRepository
import com.samuelsumbane.infrastructure.repositories.StockRepository.generateStocksCsv
import com.samuelsumbane.infrastructure.repositories.StockRepository.generateStocksExcel
import com.samuelsumbane.ssptdesktop.repositories.getCurrentTimestamp
import com.samuelsumbane.ssptdesktop.repositories.longTimeToString
import com.samuelsumbane.ssptdesktop.repositories.respondWithCache
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import java.net.URLDecoder

fun Route.stockRoutes() {
    val currentDateTime = longTimeToString(getCurrentTimestamp())

    route("/stocks") {
        authenticate("auth-jwt") {
            get("/all-stocks") {
                val stocks = StockRepository.allStockItems()
                call.respondWithCache(stocks)
            }
        }

        get("/export/stocks/excel") {
            val stocks = StockRepository.allStockItems()
            val fileBytes = generateStocksExcel(stocks)

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "Estoques_movimentos-$currentDateTime.xlsx").toString()
            )
            val excelContentType = ContentType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            call.respondBytes(fileBytes, excelContentType)
        }


        get("/export/stocks/csv") {
            val stocks = StockRepository.allStockItems()
            val fileBytes = generateStocksCsv(stocks)

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "Estoques_movimentos-$currentDateTime.csv").toString()
            )
            call.respondBytes(fileBytes, ContentType.Text.CSV)
        }

        get("/export/stocks/json") {
            val stocks = StockRepository.allStockItems()
            val json = Json.encodeToString(stocks)
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "Estoques_movimentos-$currentDateTime.json").toString()
            )
            call.respondText(json, ContentType.Application.Json)
        }

        get("/filteredStocksByDates/{initialDate}/{initialTime}/{finalDate}/{finalTime}/{ownerId}") {
            val timeZoneId = call.request.queryParameters["timezoneid"]
            if (timeZoneId == null) {
                call.respond(HttpStatusCode.NotFound, "Timezoneid not found")
                return@get
            }
            val decodedTimeZodeId = URLDecoder.decode(timeZoneId, "UTF-8")

            val initialDate = call.parameters["initialDate"]
            val initialTime = call.parameters["initialTime"]
            val finalDate = call.parameters["finalDate"]
            val finalTime = call.parameters["finalTime"]
            val ownerId = call.parameters["ownerId"]

            if (initialDate == null || finalDate == null || initialTime == null || finalTime == null || ownerId == null) {
                call.respond(HttpStatusCode.BadRequest, "Timestamp is null")
                return@get
            }

            val stocks = StockRepository.filterStockByDateTimes(initialDate, initialTime, finalDate, finalTime, ownerId, decodedTimeZodeId)
            call.respond(stocks)
        }
    }

}