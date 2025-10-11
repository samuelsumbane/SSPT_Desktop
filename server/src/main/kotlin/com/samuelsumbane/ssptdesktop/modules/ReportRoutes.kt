package com.samuelsumbane.infrastructure.modules

import com.samuelsumbane.infrastructure.repositories.ClientRepository
import com.samuelsumbane.ssptdesktop.repositories.ReportRepository
import com.samuelsumbane.infrastructure.repositories.SaleRepository
import com.samuelsumbane.infrastructure.repositories.SupplierRepository
import com.samuelsumbane.ssptdesktop.repositories.respondWithCache
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.URLDecoder

fun Route.reportRoutes() {
    route("/reports") {

        authenticate("auth-jwt") {
            get("/sales_reports") {
                val salesReports = ReportRepository.allReports()
                call.respondWithCache(salesReports)
            }
        }

        get("/eachProductTotalProfit") {
            val productsProfits = SaleRepository.getTotalProfitsByMonthAndYear()
            call.respond(productsProfits)
        }

        get("/totalQuantitiesByMonthAndYear") {
            val productsQuantities = SaleRepository.getTotalQuantitiesByMonthAndYear()
            call.respond(productsQuantities)
        }

        get("/usersTotalSales") {
            val usersTotalSales = SaleRepository.getUsersTotalSales()
            call.respond(usersTotalSales)
        }

        get("/totalProfitAndSales") {
            val totalProfit = SaleRepository.getTotalProfit()
            val totalSales = SaleRepository.getTotalSales()
            call.respond(HttpStatusCode.OK, Pair(totalProfit, totalSales))
        }


        get("/totalClientsAndSuppliers") {
            val clients = ClientRepository.getTotalClients()
            val suppliers = SupplierRepository.getTotalSuppliers()
            call.respond(Pair(clients, suppliers))
        }

        get("/productsMostBoughts") {
            val mostBoughtsProducts = SaleRepository.getProductsMostBoughts()
            call.respond(mostBoughtsProducts)
        }

//        get("/date_time/{initialDate}/{initialTime}/{finalDate}/{finalTime}") {
//                val timeZoneId = call.request.queryParameters["timezoneid"]
//                if (timeZoneId == null) {
//                    call.respond(HttpStatusCode.NotFound, "Timezoneid not found")
//                    return@get
//                }
//                val decodedTimeZodeId = URLDecoder.decode(timeZoneId, "UTF-8")
//
//                val initialDate = call.parameters["initialDate"]
//                val initialTime = call.parameters["initialTime"]
//                val finalDate = call.parameters["finalDate"]
//                val finalTime = call.parameters["finalTime"]
//
//                if (initialDate == null || finalDate == null || initialTime == null || finalTime == null) {
//                    call.respond(HttpStatusCode.BadRequest, "Timestamp is null")
//                    return@get
//                }
////                val reports = ActivityRepository.filterActivitiesByDateTimes(initialDate, initialTime, finalDate, finalTime, decodedTimeZodeId)
////                call.respond(reports)
//        }


        get("/filteredSalesByDates/{initialDate}/{initialTime}/{finalDate}/{finalTime}/{ownerId}") {
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

            val sales = SaleRepository.filterSalesByDateTimes(initialDate, initialTime, finalDate, finalTime, ownerId, decodedTimeZodeId)
            call.respond(sales)
        }
    }
}