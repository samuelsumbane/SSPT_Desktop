package com.samuelsumbane.infrastructure.repositories

import com.samuelsumbane.ssptdesktop.Owners
import com.samuelsumbane.ssptdesktop.Products
import com.samuelsumbane.ssptdesktop.modules.OwnerItem
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object ProductOwnerRepository {
    suspend fun getOwners(): List<OwnerItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            Owners.selectAll().map {
                OwnerItem(it[Owners.id], it[Owners.name], it[Owners.telephone])
            }
        }
    }

    suspend fun getOwnerById(id: Int): OwnerItem? {
        return newSuspendedTransaction(Dispatchers.IO) {
            Owners.select { Owners.id eq id }.map {
                OwnerItem(it[Owners.id], it[Owners.name], it[Owners.telephone])
            }.firstOrNull()
        }
    }

    suspend fun createOwner(owner: OwnerItem): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            val sysPackage = SysSettingsRepository.getConfigString("active_package")
            if (sysPackage != null) {
                val ownersCount = Owners.selectAll().count().toInt()
                fun insertOwner() {
                    Owners.insert {
                        it[name] = owner.name
                        it[telephone] = owner.telephone
                    }
                }
                when (sysPackage) {
                    "Lite" -> {
                        if (ownersCount < 1) {
                            insertOwner()
                            201
                        } else 101
                    }

                    "Plus" -> {
                        if (ownersCount < 5) {
                            insertOwner()
                            201
                        } else 102
                    }

                    "Pro" -> {
                        insertOwner()
                        201
                    }

                    else -> 103
                }
            } else 404
        }
    }

    suspend fun updateOwner(owner: OwnerItem): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            val OwnerData = Owners.select { Owners.id eq owner.id!! }.singleOrNull()
            if (OwnerData == null) {
                404
            } else {
                Owners.update({ Owners.id eq owner.id!! }) {
                    it[name] = owner.name
                    it[telephone] = owner.telephone
                }
                201
            }
        }
    }

    suspend fun removeOwner(OwnerId: Int): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            val OwnerData = Products.select { Products.owner eq OwnerId }.singleOrNull()
            if (OwnerData == null) {
                val afectedRows = Owners.deleteWhere { id eq OwnerId }
                if (afectedRows == 1) {
                    200 // Owner delete --------->>
                } else 404 // Id not found -------->>
            } else {
                406 // Can not delete this owner ------>>
            }
        }
    }

}