package com.samuelsumbane.infrastructure.repositories

import com.samuelsumbane.ssptdesktop.repositories.generateTimestamp
import com.samuelsumbane.ssptdesktop.repositories.getCurrentTimestamp
import com.samuelsumbane.ssptdesktop.repositories.longTimeToString
import com.samuelsumbane.ssptdesktop.*
import com.samuelsumbane.ssptdesktop.modules.StockItem
import kotlinx.coroutines.Dispatchers
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.io.ByteArrayOutputStream

object StockRepository {

    suspend fun allStockItems(): List<StockItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            (StockMovements innerJoin Products innerJoin Owners innerJoin Users innerJoin SysBranches).selectAll().map {
                StockItem(
                    it[Products.name],
                    it[StockMovements.type].toString(),
                    it[StockMovements.quantity],
                    it[StockMovements.beforeQty],
                    it[StockMovements.afterQty],
                    it[StockMovements.costPrice]?.toDouble(),
                    it[StockMovements.sellPrice]?.toDouble(),
                    it[StockMovements.reason].toString(),
                    it[Owners.name],
                    longTimeToString(it[StockMovements.datetime]),
                    it[Users.id],
                    it[Users.name],
                    it[SysBranches.name]
                )
            }
        }
    }

    suspend fun filterStockByDateTimes(
        initialDate: String,
        initialTime: String,
        finalDate: String,
        finalTime: String,
        ownerId: String,
        timeZoneId: String
    ): List<StockItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            val longInitialDateTime = generateTimestamp(initialDate, initialTime, timeZoneId)
            val longFinalDateTime = generateTimestamp(finalDate, finalTime, timeZoneId)
            val ownerIdInt = ownerId.toInt()
            val baseCondition = StockMovements.datetime.between(longInitialDateTime, longFinalDateTime)
            val finalCondition = if (ownerIdInt != 0) {
                baseCondition and (Products.owner eq ownerIdInt)
            } else {
                baseCondition
            }

            (StockMovements innerJoin Products innerJoin Owners innerJoin  Users innerJoin SysBranches).select { finalCondition }
                .map {
                StockItem(
                    it[Products.name],
                    it[StockMovements.type].toString(),
                    it[StockMovements.quantity],
                    it[StockMovements.beforeQty],
                    it[StockMovements.afterQty],
                    it[StockMovements.costPrice]?.toDouble(),
                    it[StockMovements.sellPrice]?.toDouble(),
                    it[StockMovements.reason].toString(),
                    it[Owners.name],
                    longTimeToString(it[StockMovements.datetime]),
                    it[Users.id],
                    it[Users.name],
                    it[SysBranches.name]
                )
            }
        }
    }

    fun generateStocksExcel(stocks: List<StockItem>): ByteArray {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Orders_items(sales)")

        // header ------->>
        val header = sheet.createRow(0)
        header.createCell(0).setCellValue("Producto")
        header.createCell(1).setCellValue("Tipo")
        header.createCell(2).setCellValue("Quantidade")
        header.createCell(3).setCellValue("Qtd. Antes")
        header.createCell(4).setCellValue("Qtd. Depois")
        header.createCell(5).setCellValue("Custo")
        header.createCell(6).setCellValue("Preço")
        header.createCell(7).setCellValue("Razão")
        header.createCell(8).setCellValue("Proprietário")
        header.createCell(9).setCellValue("Data_hora")
        header.createCell(10).setCellValue("Nome_usuário")
        header.createCell(11).setCellValue("Sucursal")

        // Data ----->>
        stocks.forEachIndexed { index, order ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(order.productName)
            row.createCell(1).setCellValue(order.type)
            row.createCell(2).setCellValue(order.quantity.toDouble())
            row.createCell(3).setCellValue(order.beforeQty.toString())
            row.createCell(4).setCellValue(order.afterQty.toString())
            row.createCell(5).setCellValue(order.cost!!)
            row.createCell(6).setCellValue(order.price!!)
            row.createCell(7).setCellValue(order.reason)
            row.createCell(8).setCellValue(order.ownerName)
            row.createCell(9).setCellValue(order.datetime)
            row.createCell(10).setCellValue(order.userName)
            row.createCell(11).setCellValue(order.branchName)
        }

        return ByteArrayOutputStream().use {
            workbook.write(it)
            workbook.close()
            it.toByteArray()
        }
    }

    fun generateStocksCsv(orders: List<StockItem>): ByteArray {
        val output = StringBuilder()

        output.appendLine("Producto,Tipo,Quantidade,Qtd. Antes,Qtd. Depois,Custo,Preço,Razão,Proprietário,Data_hora,Nome_usuário,Sucursal")

        orders.forEach {
            output.appendLine("${it.productName},${it.type},${it.quantity},${it.beforeQty},${it.afterQty},${it.cost},${it.price},${it.reason},${it.ownerName},${it.datetime},${it.userName},${it.branchName}")
        }

        return output.toString().toByteArray(Charsets.UTF_8)
    }

    fun insertStockMovements(item: AddStockMovementsDC) {
        StockMovements.insert {
            it[productId] = item.productId
            it[type] = item.type
            it[quantity] = item.quantity
            it[beforeQty] = item.beforeQty
            it[afterQty] = item.afterQty
            it[costPrice] = item.costPrice.toBigDecimal()
            it[sellPrice] = item.sellPrice.toBigDecimal()
            it[reason] = item.reason
            it[datetime] = getCurrentTimestamp()
            it[userId] = item.userId
            it[branch] = item.branchId
        }
    }
}