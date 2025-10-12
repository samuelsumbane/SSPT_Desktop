package com.samuelsumbane.infrastructure.repositories

import com.samuelsumbane.ssptdesktop.Suppliers
import com.samuelsumbane.ssptdesktop.SupplierItem
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

object SupplierRepository {
    suspend fun getSuppliers(): List<SupplierItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            Suppliers.selectAll().map {
                SupplierItem(
                    it[Suppliers.id],
                    it[Suppliers.name],
                    it[Suppliers.contact],
                    it[Suppliers.address],
                )
            }
        }
    }

    suspend fun getSupplierById(id: Int): SupplierItem? {
        return newSuspendedTransaction(Dispatchers.IO) {
            Suppliers.select { Suppliers.id eq id }.map {
                SupplierItem(
                    it[Suppliers.id],
                    it[Suppliers.name],
                    it[Suppliers.contact],
                    it[Suppliers.address],
                )
            }.firstOrNull()
        }
    }

    suspend fun createSupplier(supplier: SupplierItem) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Suppliers.insert {
                it[name] = supplier.name
                it[contact] = supplier.contact
                it[address] = supplier.address
            }
        }
    }

    suspend fun updateSupplier(supplier: SupplierItem) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Suppliers.update({ Suppliers.id eq supplier.id!!}) {
                it[name] = supplier.name
                it[contact] = supplier.contact
                it[address] = supplier.address
            }
        }
    }

    suspend fun getTotalSuppliers(): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            Suppliers.selectAll().count().toInt()
        }
    }
}