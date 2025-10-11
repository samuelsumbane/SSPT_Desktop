package com.samuelsumbane.infrastructure.repositories

import com.samuelsumbane.ssptdesktop.repositories.getCurrentTimestamp
import com.samuelsumbane.ssptdesktop.repositories.longTimeToString
import com.samuelsumbane.ssptdesktop.Clients
import com.samuelsumbane.ssptdesktop.Orders
import com.samuelsumbane.ssptdesktop.SysBranches
import com.samuelsumbane.ssptdesktop.Users
import com.samuelsumbane.ssptdesktop.modules.OrderItem
import com.samuelsumbane.ssptdesktop.modules.OrderItemDraft
import com.samuelsumbane.ssptdesktop.modules.SaleReportItem
import kotlinx.coroutines.Dispatchers
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.io.ByteArrayOutputStream
import java.util.*

//import kotlin.collections.forEachIndexed

//@Serializable
//sealed class OrderResult {
//    @Serializable data class Success(val orders: List<OrderItem>) : OrderResult()
//    @Serializable data class Empty(val message: String) : OrderResult()
//}


object OrderRepository {
    suspend fun getOrders(): List<OrderItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            (Orders leftJoin Clients innerJoin Users innerJoin SysBranches).selectAll().map {
                OrderItem(
                    it[Orders.id],
                    it[Orders.client_id],
                    it[Clients.name],
                    it[Orders.total].toDouble(),
                    longTimeToString(it[Orders.orderDateTime]),
                    it[Orders.status],
                    it[Users.id],
                    it[Users.name],
                    it[SysBranches.name]
                )
            }
        }
    }

    fun generateOrdersItemsExcel(orders: List<SaleReportItem>): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Orders_items(sales)")
        // header ------->>
        val header = sheet.createRow(0)
        header.createCell(0).setCellValue("Producto")
        header.createCell(1).setCellValue("Quantidade")
        header.createCell(2).setCellValue("SubTotal")
        header.createCell(3).setCellValue("Lucro")
        header.createCell(4).setCellValue("Proprietário")
        header.createCell(5).setCellValue("Status")
        header.createCell(6).setCellValue("Nome_usuário")
        header.createCell(7).setCellValue("Data_hora")

        // Data ----->>
        orders.forEachIndexed { index, order ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(order.productName)
            row.createCell(1).setCellValue(order.quantity.toDouble())
            row.createCell(2).setCellValue(order.subTotal)
            row.createCell(3).setCellValue(order.profit)
            row.createCell(4).setCellValue(order.ownerName)
            row.createCell(5).setCellValue(order.status)
            row.createCell(6).setCellValue(order.userName)
            row.createCell(7).setCellValue(order.datetime)
        }

        return ByteArrayOutputStream().use {
            workbook.write(it)
            workbook.close()
            it.toByteArray()
        }
    }

    fun generateOrdersCsv(orders: List<SaleReportItem>): ByteArray {
        val output = StringBuilder()
        output.appendLine("Producto,Quantidade,SubTotal,Lucro,Proprietário,Status,Nome_usuário,Data_hora")

        orders.forEach {
            output.appendLine("${it.productName},${it.quantity},${it.subTotal},${it.profit},${it.ownerName},${it.status},${it.userName},${it.datetime}")
        }

        return output.toString().toByteArray(Charsets.UTF_8)
    }



//suspend fun getOrders(): OrderResult {
//    return newSuspendedTransaction(Dispatchers.IO) {
//        val orders = (Orders leftJoin Clients innerJoin Users).selectAll().map {
//            OrderItem(
//                it[Orders.id],
//                it[Orders.client_id],
//                it[Clients.name],
//                it[Orders.total].toDouble(),
//                longTimeToString(it[Orders.orderDateTime]),
//                it[Orders.status],
//                it[Users.name]
//            )
//        }
//        if (orders.isEmpty()) OrderResult.Empty("Nenhum pedido encontrado")
//        else OrderResult.Success(orders)
//    }
//}


    suspend fun getOrderById(id: UUID): OrderItem? {
        return newSuspendedTransaction(Dispatchers.IO) {
            (Orders innerJoin Clients innerJoin Users innerJoin SysBranches).select { Orders.id eq id }.map {
                OrderItem(
                    it[Orders.id],
                    it[Orders.client_id],
                    it[Clients.name],
                    it[Orders.total].toDouble(),
                    longTimeToString(it[Orders.orderDateTime]),
                    it[Orders.status],
                    it[Users.id],
                    it[Users.name],
                    it[SysBranches.name]
                )
            }.firstOrNull()
        }
    }

    suspend fun createOrder(order: OrderItemDraft) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Orders.insert {
                it[client_id] = order.clientId
                it[total] = order.total.toBigDecimal()
                it[orderDateTime] = getCurrentTimestamp()
                it[client_id] = order.clientId
            }
        }
    }

//    suspend fun updateOrder(order: OrderItemDraft) {
//        return newSuspendedTransaction(Dispatchers.IO) {
//            Orders.update({ Orders.id eq order.}) {
//                it[client_id] = order.clientId
//                it[total] = order.total.toBigDecimal()
//                it[client_id] = order.clientId
//            }
//        }
//    }
}