package com.samuelsumbane.ssptdesktop.repositories.finnances


import com.samuelsumbane.infrastructure.repositories.NotificationRepository
import com.samuelsumbane.ssptdesktop.repositories.dateLongTimeToString
import com.samuelsumbane.ssptdesktop.repositories.fifteenDaysInMillis
import com.samuelsumbane.ssptdesktop.repositories.getCurrentTimestamp
import com.samuelsumbane.ssptdesktop.repositories.longTimeToString
import com.samuelsumbane.ssptdesktop.repositories.stringDateToLong
import com.samuelsumbane.ssptdesktop.IdAndStatus
import com.samuelsumbane.ssptdesktop.NotificationDraft
import com.samuelsumbane.ssptdesktop.PayableDraft
import com.samuelsumbane.ssptdesktop.PayableItem
import com.samuelsumbane.ssptdesktop.payables
import com.samuelsumbane.ssptdesktop.returnPayablesStatus
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

object PayableRepository {

    suspend fun getAllPayables(): List<PayableItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            payables.selectAll().map {
                PayableItem(
                    it[payables.id].toString(),
                    it[payables.supplier],
                    it[payables.description],
                    it[payables.value].toDouble(),
                    dateLongTimeToString(it[payables.expiration_date]),
                    dateLongTimeToString(it[payables.payment_date] ?: 0L),
                    it[payables.payment_method],
                    returnPayablesStatus(it[payables.status]),
                )
            }
        }
    }

    suspend fun addPayable(data: PayableDraft) {
        return newSuspendedTransaction(Dispatchers.IO) {
            payables.insert {
                it[supplier] = data.supplier
                it[description] = data.description
                it[value] = data.value.toBigDecimal()
                it[expiration_date] = stringDateToLong(data.expiration_date)
                it[payment_date] = null
                it[payment_method] = data.payment_method
                it[status] = 0
            }

            // Post notification ------>>
            NotificationRepository.createNotification(
                NotificationDraft(
                    userId = null,
                    title = "Conta a pagar Registada",
                    message = "Houve registro de conta a pagar com valor de ${data.value} aos ${
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

    suspend fun payTheBill(bill: IdAndStatus) {
        return newSuspendedTransaction(Dispatchers.IO) {
            payables.update({ payables.id eq bill.id }) {
                it[payment_date] = getCurrentTimestamp()
                it[status] = bill.status
            }



            // Post notification ------>>
            NotificationRepository.createNotification(
                NotificationDraft(
                    userId = null,
                    title = "Conta paga",
                    message = "Houve registro de conta paga aos ${longTimeToString(getCurrentTimestamp())}",
                    type = 1, // Info ---->>
                    autoDeleteAfter = getCurrentTimestamp() + fifteenDaysInMillis
                )
            )
        }
    }


}