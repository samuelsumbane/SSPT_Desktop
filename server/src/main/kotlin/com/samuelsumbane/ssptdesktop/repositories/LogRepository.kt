package com.samuelsumbane.infrastructure.repositories

import com.samuelsumbane.ssptdesktop.repositories.getCurrentTimestamp
import com.samuelsumbane.ssptdesktop.repositories.longTimeToString
import com.samuelsumbane.ssptdesktop.Logs
import com.samuelsumbane.ssptdesktop.Users
import com.samuelsumbane.ssptdesktop.LogDraft
import com.samuelsumbane.ssptdesktop.LogItem
import com.samuelsumbane.ssptdesktop.LogLevel
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

object LogRepository {
    suspend fun getLogs() : List<LogItem> {
        return newSuspendedTransaction {
            (Logs leftJoin Users).selectAll().map {
                LogItem(
                    it[Logs.id].toString(),
                    longTimeToString(it[Logs.datetime]),
                    it[Logs.level],
                    it[Logs.message],
                    it[Logs.module],
                    it[Users.name],
                    it[Logs.metadata_json],
                )
            }
        }
    }

    suspend fun createLog(log: LogDraft) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Logs.insert {
                it[datetime] = getCurrentTimestamp()
                it[level] = log.level
                it[message] = log.message
                it[module] = log.module
                it[user_id] = log.userId
                it[metadata_json] = log.metadataJson
            }
        }
    }

    //        DEBUG: after 7 days
    //        INFO: after 30 days
    //        WARN: after 6 months

    suspend fun cleanInfoLogs() = deleteExpiredLogs(LogLevel.INFO, 30.days)
    suspend fun cleanWarnLogs() = deleteExpiredLogs(LogLevel.WARN, 180.days)
    suspend fun cleanDebugLogs() = deleteExpiredLogs(LogLevel.DEBUG, 7.days)

    @OptIn(ExperimentalTime::class)
    private suspend fun deleteExpiredLogs(level: LogLevel, retention: Duration) {
        newSuspendedTransaction(Dispatchers.IO) {
            Logs.deleteWhere {
                (Logs.level eq level.description) and
                        (Logs.datetime lessEq (Clock.System.now() - retention).toEpochMilliseconds())
            }
        }
    }


}