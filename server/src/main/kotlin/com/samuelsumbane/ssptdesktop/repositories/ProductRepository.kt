package com.samuelsumbane.infrastructure.repositories


import com.samuelsumbane.ssptdesktop.*
import com.samuelsumbane.ssptdesktop.ChangeProductPriceDraft
import com.samuelsumbane.ssptdesktop.IncreaseProductStockDraft
import com.samuelsumbane.ssptdesktop.ProductItem
import com.samuelsumbane.ssptdesktop.ProductNameAndCategory
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object ProductRepository {
    suspend fun getProducts(): List<ProductItem> {
        return newSuspendedTransaction(Dispatchers.IO) {
            (Products innerJoin Categories innerJoin Owners).selectAll().map {
                ProductItem(
                    it[Products.id],
                    it[Products.name],
                    it[Products.productType],
                    it[Products.productRelationId],
                    it[Products.name],
                    it[Products.cost].toDouble(),
                    it[Products.price].toDouble(),
                    it[Products.stock],
                    it[Products.minStock],
                    it[Products.category],
                    it[Categories.name],
                    it[Products.barcode],
                    it[Owners.id],
                    it[Owners.name],
                )
            }
        }
    }

    suspend fun getProductById(id: Int): ProductItem? {
        return newSuspendedTransaction(Dispatchers.IO) {
            (Products innerJoin Categories innerJoin Owners).select { Products.id eq id }.map {
                ProductItem(
                    it[Products.id],
                    it[Products.name],
                    it[Products.productType],
                    it[Products.productRelationId],
                    it[Products.name],
                    it[Products.cost].toDouble(),
                    it[Products.price].toDouble(),
                    it[Products.stock],
                    it[Products.minStock],
                    it[Products.category],
                    it[Categories.name],
                    it[Products.barcode],
                    it[Owners.id],
                    it[Owners.name]
                )
            }.firstOrNull()
        }
    }

    suspend fun createProduct(product: ProductItem) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Products.insert {
                it[name] = product.name
                it[productType] = product.type
                it[productRelationId] = product.productRelationId
                it[cost] = product.cost.toBigDecimal()
                it[price] = product.price.toBigDecimal()
                it[stock] = product.stock
                it[minStock] = product.minStock
                it[category] = product.categoryId
                it[barcode] = product.barcode
                it[owner] = product.ownerId
            }
        }
    }

    suspend fun updateProduct(product: ProductNameAndCategory) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Products.update({ Products.id eq product.productId}) {
                it[name] = product.productName
                it[category] = product.categoryId
                it[barcode] = product.barcode
            }
        }
    }

    suspend fun increaseStock(product: IncreaseProductStockDraft) {
        return newSuspendedTransaction(Dispatchers.IO) {

            val productData = Products.select { Products.id eq product.productId }
                .map { it[Products.stock] } // Get Stock ----->>
                .singleOrNull()
            println(productData)
            if (productData != null) {
                val afterQtyValue = productData + product.newStock

                Products.update({ Products.id eq product.productId }) {
                    it[cost] = product.cost.toBigDecimal()
                    it[price] = product.price.toBigDecimal()
                    with(SqlExpressionBuilder) {
                        it.update(stock, stock + product.newStock)
                    }
                }

                StockRepository.insertStockMovements(
                    AddStockMovementsDC(
                        product.productId,
                        StockMovementType.Entrada,
                        product.newStock,
                        productData, // Stock ------>>
                        afterQtyValue,
                        product.cost,
                        product.price,
                        product.reason,
                        product.userId,
                        product.branchId
                    )
                )
            }
        }
    }

    suspend fun changeProductPrice(product: ChangeProductPriceDraft) {
        return newSuspendedTransaction(Dispatchers.IO) {
            Products.update({ Products.id eq product.productId }) {
                it[price] = product.newPrice.toBigDecimal()
            }
        }
    }
}