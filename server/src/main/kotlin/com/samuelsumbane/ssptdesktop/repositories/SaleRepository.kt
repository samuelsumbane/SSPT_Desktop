package com.samuelsumbane.infrastructure.repositories

//import com.samuelsumbane.infrastructure.models.StockMovementType
import com.samuelsumbane.ssptdesktop.repositories.fifteenDaysInMillis
import com.samuelsumbane.ssptdesktop.repositories.generateTimestamp
import com.samuelsumbane.ssptdesktop.repositories.getCurrentTimestamp
import com.samuelsumbane.ssptdesktop.repositories.longTimeToString
import com.samuelsumbane.ssptdesktop.AddStockMovementsDC
import com.samuelsumbane.ssptdesktop.Orders
import com.samuelsumbane.ssptdesktop.OrdersItems
import com.samuelsumbane.ssptdesktop.Owners
import com.samuelsumbane.ssptdesktop.Products
import com.samuelsumbane.ssptdesktop.StockMovementType
import com.samuelsumbane.ssptdesktop.Users
import com.samuelsumbane.ssptdesktop.NotificationDraft
import com.samuelsumbane.ssptdesktop.SaleItem
import com.samuelsumbane.ssptdesktop.SaleReportItem
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.sum
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.ZoneId
import java.util.*

object SaleRepository {

    suspend fun saleProduct(data: SaleItem) {
        return newSuspendedTransaction(Dispatchers.IO) {
            val orderUUID = UUID.randomUUID()

            Orders.insert {
                it[id] = orderUUID
                it[client_id] = data.order.clientId
                it[total] = data.order.total.toBigDecimal()
                it[orderDateTime] = getCurrentTimestamp()
                it[status] = data.order.status
                it[userId] = data.order.userId
                it[branch] = data.order.branchId
            }

            data.orderItems.forEach { item ->
                OrdersItems.insert {
                    it[id] = UUID.randomUUID()
                    it[order_id] = orderUUID
                    it[product_id] = item.productId
                    it[quantity] = item.quantity
                    it[profit] = item.profit.toBigDecimal()
                    it[sub_total] = item.subTotal.toBigDecimal()
                }

                // Drecrease product quantity
                val productData = ProductRepository.getProductById(item.productId)

                productData?.let { proData ->
                    val newStock = proData.stock - item.quantity

                    Products.update({ Products.id eq item.productId }) {
                        it[stock] = newStock
                    }

//                    if (proData.minStock != null && newStock <= proData.minStock) {
//                        NotificationRepository.createNotification(
//                            NotificationDraft(
//                                userId = null,
//                                title = "Estoque mínimo atingido",
//                                message = "O produto '${proData.name}' atingiu a quantidade mínima: ${proData.stock} unidades.",
//                                type = 2,
//                                autoDeleteAfter = null
//                            )
//                        )
//                    }

                    // Save Stock Movements -------->>
                    val afterQtyValue = productData.stock - item.quantity
                    StockRepository.insertStockMovements(
                        AddStockMovementsDC(
                            item.productId, StockMovementType.Saída,
                            item.quantity, productData.stock,
                            afterQtyValue, item.costPrice,
                            item.sellPrice, data.order.reason,
                            data.order.userId, data.order.branchId
                        )
                    )
                }
            }

            // Post notification with sale value is >= settings maxsalevalue ----->>
            val sysNotifyMaxSellValue = SysSettingsRepository.getConfigDouble("notifyMaxSellValue")
            if (sysNotifyMaxSellValue != null && data.order.total >= sysNotifyMaxSellValue) {
                NotificationRepository.createNotification(
                    NotificationDraft(
                        userId = null,
                        title = "Venda Elevada Registada",
                        message = "A venda com ID $orderUUID no valor de ${data.order.total} MZN ultrapassou o limite definido para vendas elevadas.",
                        type = 1, // Info ---->>
                        autoDeleteAfter = getCurrentTimestamp() + fifteenDaysInMillis
                    )
                )
                // Send email ---->>

//                SysSettingsRepository.getConfigString("active_package")?.let {
//                    if (it != SysPackage.LITE.description) {
//                        // Content
//                    }
//                }
            }

        }
    }

    suspend fun getTotalProfitsByMonthAndYear(): List<JsonObject> {
        return newSuspendedTransaction(Dispatchers.IO) {
            Orders
                .slice(
                    Orders.orderDateTime,
                    Orders.total
                )
                .selectAll()
                .mapNotNull { row ->
                    val selltime: Long = row[Orders.orderDateTime] ?: return@mapNotNull null
                    val dateTime = Instant.ofEpochMilli(selltime).atZone(ZoneId.systemDefault()).toLocalDate()
                    val total = row[Orders.total] ?: BigDecimal.ZERO
                    Pair(dateTime.year to dateTime.monthValue, total)
                }
                .groupBy { it.first } // (year, month)
                .map { (yearMonth, entries) ->
                    val totalProfit = entries.map { it.second }.reduceOrNull(BigDecimal::add) ?: BigDecimal.ZERO
                    JsonObject(
                        mapOf(
                            "month" to JsonPrimitive(yearMonth.second.toString()),
                            "year" to JsonPrimitive(yearMonth.first.toString()),
                            "profit" to JsonPrimitive(totalProfit.toString())
                        )
                    )
                }
        }
    }

    suspend fun getTotalQuantitiesByMonthAndYear(): List<JsonObject> {
        return newSuspendedTransaction(Dispatchers.IO) {
            (Orders innerJoin OrdersItems)
                .slice(Orders.orderDateTime, OrdersItems.quantity)
                .selectAll()
                .mapNotNull { row ->
                    val selltime = row[Orders.orderDateTime]
                    val quantity = row[OrdersItems.quantity]
                    val date = Instant.ofEpochMilli(selltime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    Triple(date.year, date.monthValue, quantity)
                }
                .groupBy { it.first to it.second } // Agrupa por (ano, mês)
                .map { (yearMonth, entries) ->
                    val totalQuantity = entries.sumOf { it.third }
                    JsonObject(
                        mapOf(
                            "month" to JsonPrimitive(yearMonth.second.toString()),
                            "year" to JsonPrimitive(yearMonth.first.toString()),
                            "quantity" to JsonPrimitive(totalQuantity)
                        )
                    )
                }
        }
    }


    suspend fun getTotalProfit(): Double {
        return newSuspendedTransaction(Dispatchers.IO) {
            OrdersItems.slice(OrdersItems.profit.sum())
                .selectAll()
                .firstOrNull()
                ?.get(OrdersItems.profit.sum())?.toDouble()
                ?.let { BigDecimal(it).setScale(2, RoundingMode.HALF_UP).toDouble() }
                ?: 0.0
        }
    }

    suspend fun getTotalSales(): Double {
        return newSuspendedTransaction(Dispatchers.IO) {
            Orders.slice(Orders.total.sum())
                .selectAll()
                .limit(5)
                .firstOrNull()
                ?.get(Orders.total.sum())?.toDouble()
                ?.let { BigDecimal(it).setScale(2, RoundingMode.HALF_UP).toDouble() }
                ?: 0.0
        }
    }



    suspend fun getUsersTotalSales(): List<JsonObject> {
        return newSuspendedTransaction(Dispatchers.IO) {
            (Orders innerJoin Users innerJoin OrdersItems).slice(
                Users.name.alias("name"),
                OrdersItems.quantity.sum().alias("quantity")
            )
                .selectAll()
                .groupBy(Users.name)
                .orderBy(OrdersItems.quantity.sum(), SortOrder.DESC)
                .limit(5)
                .map { row ->
                    val name = row[Users.name.alias("name")]
                    val amount = row[OrdersItems.quantity.sum().alias("quantity")] ?: 0
                    JsonObject(
                        mapOf(
                            "name" to JsonPrimitive(name),
                            "quantity" to JsonPrimitive(amount.toString())
                        )
                    )
                }
        }
    }

    suspend fun getProductsMostBoughts(): List<JsonObject> {
        return newSuspendedTransaction(Dispatchers.IO) {
            (Orders innerJoin OrdersItems innerJoin Products).slice(
                Products.name.alias("productname"),
                OrdersItems.quantity.sum().alias("quantity")
            )
                .selectAll()
                .groupBy(OrdersItems.product_id)
                .limit(5)
                .map { row ->
                    val proname = row[Products.name.alias("productname")]
                    val quantityAmount = row[OrdersItems.quantity.sum().alias("quantity")] ?: 0
                    JsonObject(
                        mapOf(
                            "productname" to JsonPrimitive(proname),
                            "quantity" to JsonPrimitive(quantityAmount.toString()),
                        )
                    )
                }
        }
    }

    suspend fun filterSalesByDateTimes(
        initialDate: String,
        initialTime: String,
        finalDate: String,
        finalTime: String,
        ownerId: String,
        timeZoneId: String,
    ): List<SaleReportItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            val longInitialDateTime = generateTimestamp(initialDate, initialTime, timeZoneId)
            val longFinalDateTime = generateTimestamp(finalDate, finalTime, timeZoneId)
            val ownerIdInt = ownerId.toInt()
            val baseCondition = Orders.orderDateTime.between(longInitialDateTime, longFinalDateTime)
            val finalCondition = if (ownerIdInt != 0) {
                baseCondition and (Products.owner eq ownerIdInt)
            } else {
                baseCondition
            }

            (Orders innerJoin OrdersItems innerJoin Products innerJoin Users innerJoin Owners)
                .select { finalCondition }
                .map {
                    SaleReportItem(
                        it[Products.name],
                        it[OrdersItems.quantity],
                        it[OrdersItems.sub_total].toDouble(),
                        it[OrdersItems.profit].toDouble(),
                        it[Orders.status],
                        it[Owners.name],
                        it[Orders.userId],
                        it[Users.name].toString(),
                        longTimeToString(it[Orders.orderDateTime]),
                    )
                }
        }
    }


}