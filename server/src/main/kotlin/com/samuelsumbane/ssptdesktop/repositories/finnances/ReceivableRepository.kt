package com.samuelsumbane.ssptdesktop.repositories.finnances


import com.samuelsumbane.infrastructure.repositories.NotificationRepository
import com.samuelsumbane.ssptdesktop.repositories.dateLongTimeToString
import com.samuelsumbane.ssptdesktop.repositories.fifteenDaysInMillis
import com.samuelsumbane.ssptdesktop.repositories.getCurrentTimestamp
import com.samuelsumbane.ssptdesktop.repositories.longTimeToString
import com.samuelsumbane.ssptdesktop.repositories.stringDateToLong
import com.samuelsumbane.ssptdesktop.IdAndStatus
import com.samuelsumbane.ssptdesktop.NotificationDraft
import com.samuelsumbane.ssptdesktop.ReceivableDraft
import com.samuelsumbane.ssptdesktop.ReceivableItem
import com.samuelsumbane.ssptdesktop.receivables
import com.samuelsumbane.ssptdesktop.returnReceivablesStatus
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

object ReceivableRepository {

    suspend fun getAllReceivables(): List<ReceivableItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            receivables.selectAll().map {
                ReceivableItem(
                    it[receivables.id].toString(),
                    it[receivables.client],
                    it[receivables.description],
                    it[receivables.value].toDouble(),
                    dateLongTimeToString(it[receivables.expiration_date]),
                    dateLongTimeToString(it[receivables.received_date] ?: 0L),
                    it[receivables.received_method],
                    returnReceivablesStatus(it[receivables.status]),
                )
            }
        }
    }

    suspend fun addReceivable(data: ReceivableDraft) {
        return newSuspendedTransaction(Dispatchers.IO) {
            receivables.insert {
                it[client] = data.client
                it[description] = data.description
                it[value] = data.value.toBigDecimal()
                it[expiration_date] = stringDateToLong(data.expiration_date)
                it[received_method] = data.received_method
                it[status] = 0
            }

            // Post notification ------>>
            NotificationRepository.createNotification(
                NotificationDraft(
                    userId = null,
                    title = "Conta a receber Registada",
                    message = "Houve registro de conta a receber com valor de ${data.value} aos ${
                        longTimeToString(
                            getCurrentTimestamp()
                        )
                    }",
                    type = 1, // Info ---->>
                    autoDeleteAfter = getCurrentTimestamp() + fifteenDaysInMillis
                )
            )
        }
    }

    suspend fun receiveTheBill(bill: IdAndStatus) {
        return newSuspendedTransaction(Dispatchers.IO) {
            receivables.update({ receivables.id eq bill.id }) {
                it[received_date] = getCurrentTimestamp()
                it[status] = bill.status
            }

            // Post notification ------>>
            NotificationRepository.createNotification(
                NotificationDraft(
                    userId = null,
                    title = "Conta recebida",
                    message = "Houve registro de conta recebida aos ${longTimeToString(getCurrentTimestamp())}",
                    type = 1, // Info ---->>
                    autoDeleteAfter = getCurrentTimestamp() + fifteenDaysInMillis
                )
            )
        }
    }
//    suspend fun updateBranch(branch: BranchItem) {
//        return newSuspendedTransaction(Dispatchers.IO) {
//            SysBranches.update({ SysBranches.id eq branch.id!!}) {
//                it[name] = branch.name
//                it[address] = branch.address
//            }
//        }
//    }

}