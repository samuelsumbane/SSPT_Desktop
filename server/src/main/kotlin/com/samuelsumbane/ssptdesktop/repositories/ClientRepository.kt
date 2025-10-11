package com.samuelsumbane.infrastructure.repositories

import com.samuelsumbane.ssptdesktop.Clients
import com.samuelsumbane.ssptdesktop.modules.ClientItem
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object ClientRepository {
    suspend fun getClients(): List<ClientItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            Clients.selectAll().map {
                ClientItem(it[Clients.id], it[Clients.name], it[Clients.telephone])
            }
        }
    }

    suspend fun getClientById(id: Int): ClientItem? {
        return newSuspendedTransaction(Dispatchers.IO) {
            Clients.select { Clients.id eq id }.map {
                ClientItem(it[Clients.id], it[Clients.name], it[Clients.telephone])
            }.firstOrNull()
        }
    }

    suspend fun createClient(client: ClientItem) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Clients.insert {
                it[name] = client.name
                it[telephone] = client.telephone
                it[total] = "0.0".toBigDecimal()
            }
        }
    }

    suspend fun updateClient(client: ClientItem) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Clients.update({ Clients.id eq client.id!!}) {
                it[name] = client.name
                it[telephone] = client.telephone
            }
        }
    }

    suspend fun getTotalClients(): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            Clients.selectAll().count().toInt()
        }
    }
//    suspend fun getTotalProfit(): Double {
//        return newSuspendedTransaction(Dispatchers.IO) {
//            OrdersItems.slice(OrdersItems.profit.sum())
//                .selectAll()
//                .mapNotNull { it[OrdersItems.profit.sum()] }
//                .firstOrNull()
//                ?.let { BigDecimal(it.toDouble()).setScale(2, RoundingMode.HALF_UP).toDouble() }
//                ?: 0.0
//        }
//    }

    suspend fun removeClient(clientId: Int): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            val clientData = getClientById(clientId)
            if (clientData != null) {
                val afectedRows = Clients.deleteWhere { id eq clientId }
                if (afectedRows == 1) {
                    200
                } else {
                    406
                }
            } else {
                404 // Not found ----->>
            }


        }
    }

}
