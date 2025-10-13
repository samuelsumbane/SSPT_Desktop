package com.samuelsumbane.ssptdesktop.domain.repository

import com.samuelsumbane.ssptdesktop.kclient.OwnerItem
import com.samuelsumbane.ssptdesktop.kclient.StatusAndMessage

interface ProOwnerRepository {
    suspend fun getProOwners(): List<OwnerItem>
    suspend fun addOwner(proOwner: OwnerItem): StatusAndMessage
    suspend fun editOwner(proOwner: OwnerItem): StatusAndMessage
    suspend fun removeOwner(proOwnerId: Int): StatusAndMessage
}