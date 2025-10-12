package com.samuelsumbane.infrastructure.repositories

import com.samuelsumbane.ssptdesktop.repositories.getCurrentTimestamp
import com.samuelsumbane.ssptdesktop.repositories.longTimeToString
import com.samuelsumbane.ssptdesktop.SysConfigurations
import com.samuelsumbane.ssptdesktop.SysConfigItem
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

object SysSettingsRepository {

    private fun saveDefaultsConfigs() {
        val notExistingConfigs = SysConfigurations.selectAll().empty()
        if (notExistingConfigs) {
            for((k, v) in defaultStringConfigs){
                SysConfigurations.insert {
                    it[SysConfigurations.key] = k
                    it[stringValue] = v
                    it[lastUpdate] = getCurrentTimestamp()
                }
            }

            for((k, v) in defaultBooleanConfigs){
                SysConfigurations.insert {
                    it[SysConfigurations.key] = k
                    it[boolValue] = v
                    it[lastUpdate] = getCurrentTimestamp()
                }
            }

            for((k, v) in defaultIntConfigs){
                SysConfigurations.insert {
                    it[SysConfigurations.key] = k
                    it[intValue] = v
                    it[lastUpdate] = getCurrentTimestamp()
                }
            }

            for((k, v) in defaultDoubleConfigs){
                SysConfigurations.insert {
                    it[SysConfigurations.key] = k
                    it[doubleValue] = v
                    it[lastUpdate] = getCurrentTimestamp()
                }
            }
        }
    }

    suspend fun verifyConfigs() {
        newSuspendedTransaction(Dispatchers.IO) {
            saveDefaultsConfigs()
        }
    }

    suspend fun sysConfigtems(): List<SysConfigItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            SysConfigurations.selectAll().map {
                SysConfigItem(
                    it[SysConfigurations.id],
                    it[SysConfigurations.key],
                    it[SysConfigurations.stringValue],
                    it[SysConfigurations.intValue],
                    it[SysConfigurations.doubleValue],
                    it[SysConfigurations.boolValue],
                    longTimeToString(it[SysConfigurations.lastUpdate])
                )
            }
        }
    }

    suspend fun editSysConfig(config: SysConfigItem) {
        return newSuspendedTransaction(Dispatchers.IO) {
            for (value in listOf(
                config.stringValue,
                config.intValue,
                config.doubleValue,
                config.boolValue
            )) {
                value?.let { configValue ->
                    SysConfigurations.update({ SysConfigurations.id eq config.id }) {
                        it[key] = config.key
                        when (configValue) {
                            is Boolean -> it[boolValue] = configValue
                            is Double -> it[doubleValue] = configValue
                            is Int -> it[intValue] = configValue
                            is String -> it[stringValue] = configValue
                        }
                        it[lastUpdate] = getCurrentTimestamp()
                    }
                }
            }
        }
    }


    fun getConfigDouble(key: String): Double? {
        return SysConfigurations
            .select { SysConfigurations.key eq key }
            .map { it[SysConfigurations.doubleValue] }
            .firstOrNull()
    }

    fun getConfigString(key: String): String? {
        return SysConfigurations
            .select { SysConfigurations.key eq key }
            .map { it[SysConfigurations.stringValue] }
            .firstOrNull()
    }

    suspend fun getPackageName(): String? {
        return newSuspendedTransaction(Dispatchers.IO) {
            SysConfigurations
                .select { SysConfigurations.key eq "active_package" }
                .map { it[SysConfigurations.stringValue] }
                .firstOrNull()
        }
    }


    fun getConfigInt(key: String): Int? {
        return SysConfigurations
            .select { SysConfigurations.key eq key }
            .map { it[SysConfigurations.intValue] }
            .firstOrNull()
    }

    fun getConfigBoolean(key: String): Boolean? {
        return SysConfigurations
            .select { SysConfigurations.key eq key }
            .map { it[SysConfigurations.boolValue] }
            .firstOrNull()
    }

}


val defaultStringConfigs = mapOf(
    "active_package" to SysPackage.PLUS.description,
)

val defaultBooleanConfigs = mapOf(
    "alert_min_pro_quantity" to true,
    "notifyMaxSell" to true,
)

val defaultDoubleConfigs = mapOf(
    "notifyMaxSellValue" to 5000.00
)

val defaultIntConfigs = mapOf(
    "percentual_iva" to 17,
)

enum class SysPackage(val description: String) {
    LITE("Lite"),
    PLUS("Plus"),
    PRO("Pro"),
}
