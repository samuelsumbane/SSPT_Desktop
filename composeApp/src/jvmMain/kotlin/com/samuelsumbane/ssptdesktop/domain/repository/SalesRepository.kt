package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.SaleItem
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage

interface SalesRepository {
    suspend fun saleProducts(saleItem: SaleItem): StatusAndMessage
}