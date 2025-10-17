package com.samuelsumbane.ssptdesktop.data.repository

import com.samuelsumbane.ssptdesktop.domain.repository.SalesRepository
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.kclient.SaleItem
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage
import com.samuelsumbane.ssptdesktop.kclient.apiPath

class SalesRepositoryImpl : SalesRepository {
    val token = Session.jwtToken ?: ""
    val kClientRepo = KClientRepository()

    override suspend fun saleProducts(saleItem: SaleItem): StatusAndMessage {
        val (status, message) = kClientRepo.postRequest("$apiPath/sale-products", saleItem)
        return StatusAndMessage(status, message)
    }
}