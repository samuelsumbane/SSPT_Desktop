package com.samuelsumbane.ssptdesktop.repositories

import com.samuelsumbane.ssptdesktop.*
import com.samuelsumbane.ssptdesktop.modules.SaleReportItem
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.sum
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.math.BigDecimal


object ReportRepository {

    suspend fun allReports(): List<SaleReportItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            (OrdersItems innerJoin Orders innerJoin Products innerJoin Owners innerJoin Users).selectAll().map {
                SaleReportItem(
                    it[Products.name],
                    it[OrdersItems.quantity],
                    it[OrdersItems.sub_total].toDouble(),
                    it[OrdersItems.profit].toDouble(),
                    it[Orders.status],
                    it[Owners.name],
                    it[Orders.userId],
                    it[Users.name],
                    longTimeToString(it[Orders.orderDateTime]),
                )
            }
        }
    }

    suspend fun getEachProductTotalProfit(): List<JsonObject> {
    return newSuspendedTransaction(Dispatchers.IO) {
        val productName = OrdersItems.product_id.alias("productname")
        val profitSum = OrdersItems.profit.sum().alias("profit")

        (OrdersItems innerJoin Products)
            .slice(productName, profitSum)  // Certifique-se de chamar `slice` na TABELA
            .selectAll()
            .groupBy(Products.name)
            .map { row ->
                val proname = row[Products.name.alias("productname")]
                val amount = row[profitSum] ?: BigDecimal.ZERO
                JsonObject(
                    mapOf(
                        "productname" to JsonPrimitive(proname),
                        "profit" to JsonPrimitive(amount.toPlainString())
                    )
                )
            }
    }
}

}