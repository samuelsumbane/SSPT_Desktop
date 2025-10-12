package com.samuelsumbane.ssptdesktop.domain.usecase.repository

import com.samuelsumbane.ssptdesktop.kclient.OwnerItem
import kotlinx.coroutines.flow.Flow

interface ProOwnerRepository {
    fun getProOwners(): Flow<List<OwnerItem>>
    suspend fun addOwner(proOwner: OwnerItem)
    suspend fun editOwner(proOwner: OwnerItem)
    suspend fun removeOwner(proOwnerId: Int)
}