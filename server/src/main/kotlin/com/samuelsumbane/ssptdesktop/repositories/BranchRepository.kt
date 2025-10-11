package com.samuelsumbane.ssptdesktop.repositories

import com.samuelsumbane.infrastructure.repositories.SysSettingsRepository
import com.samuelsumbane.ssptdesktop.SysBranches
import com.samuelsumbane.ssptdesktop.modules.BranchItem
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

object BranchRepository {

    suspend fun getAllBranches(): List<BranchItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            SysBranches.selectAll().map {
                BranchItem(
                    it[SysBranches.id],
                    it[SysBranches.name],
                    it[SysBranches.address],
                )
            }
        }
    }

    suspend fun createBranch(branch: BranchItem): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            val sysPackage = SysSettingsRepository.getConfigString("active_package")
            if (sysPackage != null) {
                val branchesCount = SysBranches.selectAll().count().toInt()
                when (sysPackage) {
                    "Lite" -> {
                        if (branchesCount < 1) {
                            SysBranches.insert {
                                it[name] = branch.name
                                it[address] = branch.address
                            }
                            201
                        } else 101
                    }

                    "Plus" -> {
                        if (branchesCount < 5) {
                            SysBranches.insert {
                                it[name] = branch.name
                                it[address] = branch.address
                            }
                            201
                        } else 102
                    }

                    "Pro" -> {
                        SysBranches.insert {
                            it[name] = branch.name
                            it[address] = branch.address
                        }
                        201
                    }

                    else -> 103

                }
            } else 404

        }
    }


    suspend fun updateBranch(branch: BranchItem) {
        return newSuspendedTransaction(Dispatchers.IO) {
            SysBranches.update({ SysBranches.id eq branch.id!! }) {
                it[name] = branch.name
                it[address] = branch.address
            }
        }
    }

    suspend fun removeBranch(branchId: Int): Boolean = newSuspendedTransaction(Dispatchers.IO) {
        val rowsDeleted = SysBranches.deleteWhere { id eq branchId }
        rowsDeleted == 1
    }



}