package com.samuelsumbane.infrastructure.repositories


import com.samuelsumbane.ssptdesktop.repositories.getCurrentTimestamp
import com.samuelsumbane.ssptdesktop.repositories.longTimeToString
import com.samuelsumbane.ssptdesktop.Notifications
import com.samuelsumbane.ssptdesktop.Users
import com.samuelsumbane.ssptdesktop.modules.IdAndReadState
import com.samuelsumbane.ssptdesktop.modules.NotificationDraft
import com.samuelsumbane.ssptdesktop.modules.NotificationItem
import com.samuelsumbane.ssptdesktop.returnNotificationType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object NotificationRepository {

    fun notificationsCleaner() = runBlocking {
        deleteExpiredNotifications()
    }

    private suspend fun deleteExpiredNotifications() {
        val now = getCurrentTimestamp()
        return newSuspendedTransaction(Dispatchers.IO) {
            Notifications.deleteWhere {
                Notifications.autoDeleteAfter.lessEq(now) and
                        Notifications.autoDeleteAfter.isNotNull()
            }
        }
    }

    suspend fun getNotifications(): List<NotificationItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            (Notifications leftJoin Users).selectAll().map {
                NotificationItem(
                    it[Notifications.id],
                        it[Users.name],
                    it[Notifications.title],
                    it[Notifications.message],
                    returnNotificationType(it[Notifications.type]),
                    longTimeToString(it[Notifications.createdAt]),
                    it[Notifications.read],
                    it[Notifications.userId]
                )
            }
        }
    }

    suspend fun createNotification(notf: NotificationDraft) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Notifications.insert {
                it[userId] = notf.userId
                it[title] = notf.title
                it[message] = notf.message
                it[type] = notf.type
                it[createdAt] = getCurrentTimestamp()
                it[read] = false
                it[autoDeleteAfter] = notf.autoDeleteAfter
            }
        }
    }

    suspend fun updateReadState(notf: IdAndReadState) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Notifications.update({ Notifications.id eq notf.id }) {
                it[read] = notf.isRead
            }
        }
    }

    suspend fun removeNotification(notfId: Int): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            val afectedRows = Notifications.deleteWhere { id eq notfId }
            if (afectedRows == 1) {
                200
            } else {
                406
            }
        }
    }
}