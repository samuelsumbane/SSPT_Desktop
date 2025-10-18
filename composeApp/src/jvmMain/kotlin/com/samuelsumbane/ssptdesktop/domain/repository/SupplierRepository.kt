package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.SupplierItem

interface SupplierRepository {
    suspend fun getSuppliers(): List<SupplierItem>
    suspend fun addSupplier(supplier: SupplierItem): StatusAndMessage
    suspend fun editSupplier(supplier: SupplierItem): StatusAndMessage
    suspend fun removeSupplier(supplierId: Int): StatusAndMessage
}