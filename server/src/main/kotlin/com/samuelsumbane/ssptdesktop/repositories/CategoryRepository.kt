package com.samuelsumbane.infrastructure.repositories

import com.samuelsumbane.ssptdesktop.Categories
import com.samuelsumbane.ssptdesktop.Products
import com.samuelsumbane.ssptdesktop.Suppliers
import com.samuelsumbane.ssptdesktop.modules.CategoryItem
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object CategoryRepository {


    suspend fun verifyCategories() {
        newSuspendedTransaction(Dispatchers.IO) {
            val existingCategories = Categories.selectAll().empty()
            if (existingCategories) {
                defaultCategories.forEach { categoryName ->
                    Categories.insert {
                        it[name] = categoryName
                        it[isDeault] = true
                    }
                }
            }
        }
    }

    suspend fun getCategories(): List<CategoryItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            Categories.selectAll().map {
                CategoryItem(
                    it[Categories.id],
                 it[Categories.name],
                it[Categories.isDeault],
                )
            }
        }
    }

    suspend fun getCategoryById(id: Int): CategoryItem? {
        return newSuspendedTransaction(Dispatchers.IO) {
            Categories.select { Categories.id eq id }.map {
                CategoryItem(it[Categories.id], it[Categories.name], it[Categories.isDeault])
            }.firstOrNull()
        }
    }

    suspend fun createCategory(category: CategoryItem) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Categories.insert {
                it[name] = category.name
                it[isDeault] = category.isDefault
            }
        }
    }

    suspend fun updateCategory(category: CategoryItem) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Categories.update({ Categories.id eq category.id!!}) {
                it[name] = category.name
            }
        }
    }

    suspend fun getTotalSuppliers(): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            Suppliers.selectAll().count().toInt()
        }
    }

    suspend fun removeCategory(categoryId: Int): Int {
        return newSuspendedTransaction(Dispatchers.IO) {
            val proData = Products.select { Products.category eq categoryId }.singleOrNull()
            if (proData != null) { // Not delete ----->>
                406
            } else { // Can delete ------->>
                val afectedRows = Categories.deleteWhere { id eq categoryId }
                if (afectedRows == 1) {
                    200
                } else {
                    404
                }
            }
        }
    }

}

val defaultCategories = listOf(
    "Alimentos e Bebidas",
    "Higiene e Limpeza",
    "Cuidados Infantis",
    "Productos para Casa",
    "Roupas",
    "Electrônicos",
)
