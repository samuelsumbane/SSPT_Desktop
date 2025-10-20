package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.PayableDraft
import com.samuelsumbane.ssptdesktop.kclient.PayableItem
import com.samuelsumbane.ssptdesktop.kclient.ReceivableDraft
import com.samuelsumbane.ssptdesktop.kclient.ReceivableItem
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage

interface FinanceRepository {
    suspend fun getPayables(): List<PayableItem>
    suspend fun getReceivables(): List<ReceivableItem>
    suspend fun addPayable(payable: PayableDraft): StatusAndMessage
//    suspend fun editPayable(payable: PayableDraft): StatusAndMessage
    suspend fun addReceivable(receivable: ReceivableDraft): StatusAndMessage
//    suspend fun editReceivable(receivable: ReceivableDraft): StatusAndMessage
}