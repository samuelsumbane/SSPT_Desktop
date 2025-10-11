package com.samuelsumbane.infrastructure.repositories

import com.samuelsumbane.ssptdesktop.OrdersItems
import com.samuelsumbane.ssptdesktop.Owners
import com.samuelsumbane.ssptdesktop.Products
import com.samuelsumbane.ssptdesktop.modules.OrderItemsItem
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.util.*

object OrderItemRepository {
    suspend fun getOrdersItems(): List<OrderItemsItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            (OrdersItems innerJoin Products innerJoin Owners).selectAll().map {
                OrderItemsItem(
                    it[OrdersItems.id],
                    it[OrdersItems.order_id],
                    it[OrdersItems.product_id],
                    it[Products.name],
                    it[OrdersItems.quantity],
                    it[OrdersItems.sub_total].toDouble(),
                    it[OrdersItems.profit].toDouble(),
                    it[Owners.name],
                )
            }
        }
    }

    suspend fun getOrdersItemsById(id: UUID): List<OrderItemsItem?>  {
        return newSuspendedTransaction(Dispatchers.IO) {
            (OrdersItems innerJoin Products innerJoin Owners).select { OrdersItems.order_id eq id }.map {
                OrderItemsItem(
                    it[OrdersItems.id],
                    it[OrdersItems.order_id],
                    it[OrdersItems.product_id],
                    it[Products.name],
                    it[OrdersItems.quantity],
                    it[OrdersItems.sub_total].toDouble(),
                    it[OrdersItems.profit].toDouble(),
                    it[Owners.name],
                )
            }
        }
    }

    suspend fun updateOrdersItems(orderItem: OrderItemsItem) {
        return newSuspendedTransaction(Dispatchers.IO) {
            OrdersItems.update({ OrdersItems.id eq orderItem.id!!}) {
                it[order_id] = orderItem.orderId
                it[product_id] = orderItem.productId
                it[quantity] = orderItem.quantity
                it[sub_total] = orderItem.subTotal.toBigDecimal()
            }
        }
    }
}